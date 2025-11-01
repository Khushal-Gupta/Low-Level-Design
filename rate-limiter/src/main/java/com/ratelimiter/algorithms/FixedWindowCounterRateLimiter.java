package com.ratelimiter.algorithms;

import com.ratelimiter.core.RateLimitConfig;
import com.ratelimiter.core.RateLimitResult;
import com.ratelimiter.core.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class FixedWindowCounterRateLimiter implements RateLimiter {
    
    private final RateLimitConfig config;
    private final ConcurrentHashMap<String, WindowCounter> counters;
    private final ConcurrentHashMap<String, ReentrantLock> locks;
    
    public FixedWindowCounterRateLimiter(RateLimitConfig config) {
        this.config = config;
        this.counters = new ConcurrentHashMap<>();
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
            WindowCounter counter = counters.computeIfAbsent(key, k -> new WindowCounter());
            return counter.tryConsume(tokens, config.getCapacity(), config.getTimeWindow().toMillis());
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
                counters.remove(key);
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
    
    private static class WindowCounter {
        private final AtomicLong count;
        private volatile long windowStart;
        
        public WindowCounter() {
            this.count = new AtomicLong(0);
            this.windowStart = getCurrentWindow(System.currentTimeMillis(), 0);
        }
        
        public RateLimitResult tryConsume(int tokensRequested, long capacity, long windowSizeMs) {
            long currentTime = System.currentTimeMillis();
            long currentWindow = getCurrentWindow(currentTime, windowSizeMs);
            
            if (currentWindow != windowStart) {
                windowStart = currentWindow;
                count.set(0);
            }
            
            long currentCount = count.get();
            if (currentCount + tokensRequested <= capacity) {
                count.addAndGet(tokensRequested);
                long remainingTokens = capacity - count.get();
                long resetTime = windowStart + windowSizeMs;
                return RateLimitResult.allowed(remainingTokens, resetTime);
            } else {
                long resetTime = windowStart + windowSizeMs;
                long retryAfterMs = resetTime - currentTime;
                return RateLimitResult.denied(retryAfterMs, resetTime);
            }
        }
        
        private long getCurrentWindow(long currentTime, long windowSizeMs) {
            if (windowSizeMs == 0) {
                return currentTime;
            }
            return (currentTime / windowSizeMs) * windowSizeMs;
        }
    }
}
