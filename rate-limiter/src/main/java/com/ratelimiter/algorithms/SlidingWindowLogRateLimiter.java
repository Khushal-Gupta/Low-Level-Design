package com.ratelimiter.algorithms;

import com.ratelimiter.core.RateLimitConfig;
import com.ratelimiter.core.RateLimitResult;
import com.ratelimiter.core.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

public class SlidingWindowLogRateLimiter implements RateLimiter {
    
    private final RateLimitConfig config;
    private final ConcurrentHashMap<String, RequestLog> logs;
    private final ConcurrentHashMap<String, ReentrantLock> locks;
    
    public SlidingWindowLogRateLimiter(RateLimitConfig config) {
        this.config = config;
        this.logs = new ConcurrentHashMap<>();
        this.locks = new ConcurrentHashMap<>();
    }
    
    @Override
    public RateLimitResult isAllowed(String key) {
        return isAllowed(key, 1);
    }
    
    @Override
    public RateLimitResult isAllowed(String key, int tokens) {
        ReentrantLock lock = locks.computeIfAbsent(key, k -> new ReentrantLock());
        lock.lock();
        try {
            RequestLog log = logs.computeIfAbsent(key, k -> new RequestLog());
            return log.tryConsume(tokens, config.getCapacity(), config.getTimeWindow().toMillis());
        } finally {
            lock.unlock();
        }
    }
    
    @Override
    public void reset(String key) {
        ReentrantLock lock = locks.get(key);
        if (lock != null) {
            lock.lock();
            try {
                logs.remove(key);
                locks.remove(key);
            } finally {
                lock.unlock();
            }
        }
    }
    
    @Override
    public RateLimitConfig getConfig() {
        return config;
    }
    
    private static class RequestLog {
        private final ConcurrentLinkedQueue<Long> timestamps;
        
        public RequestLog() {
            this.timestamps = new ConcurrentLinkedQueue<>();
        }
        
        public RateLimitResult tryConsume(int tokensRequested, long capacity, long windowSizeMs) {
            long currentTime = System.currentTimeMillis();
            long windowStart = currentTime - windowSizeMs;
            
            removeExpiredEntries(windowStart);
            
            if (timestamps.size() + tokensRequested <= capacity) {
                for (int i = 0; i < tokensRequested; i++) {
                    timestamps.offer(currentTime);
                }
                long remainingTokens = capacity - timestamps.size();
                long resetTime = calculateResetTime(windowSizeMs);
                return RateLimitResult.allowed(remainingTokens, resetTime);
            } else {
                Long oldestTimestamp = timestamps.peek();
                long retryAfterMs = oldestTimestamp != null ? 
                    Math.max(0, oldestTimestamp + windowSizeMs - currentTime) : 0;
                long resetTime = calculateResetTime(windowSizeMs);
                return RateLimitResult.denied(retryAfterMs, resetTime);
            }
        }
        
        private void removeExpiredEntries(long windowStart) {
            while (!timestamps.isEmpty() && timestamps.peek() < windowStart) {
                timestamps.poll();
            }
        }
        
        private long calculateResetTime(long windowSizeMs) {
            Long oldestTimestamp = timestamps.peek();
            if (oldestTimestamp != null) {
                return oldestTimestamp + windowSizeMs;
            }
            return System.currentTimeMillis() + windowSizeMs;
        }
    }
}
