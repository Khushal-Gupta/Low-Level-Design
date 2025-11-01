package com.ratelimiter.algorithms;

import com.ratelimiter.core.RateLimitConfig;
import com.ratelimiter.core.RateLimitResult;
import com.ratelimiter.core.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class SlidingWindowCounterRateLimiter implements RateLimiter {
    
    private final RateLimitConfig config;
    private final ConcurrentHashMap<String, SlidingWindowCounter> counters;
    private final ConcurrentHashMap<String, ReentrantLock> locks;
    
    public SlidingWindowCounterRateLimiter(RateLimitConfig config) {
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
            SlidingWindowCounter counter = counters.computeIfAbsent(key, k -> new SlidingWindowCounter());
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
    
    private static class SlidingWindowCounter {
        private final AtomicLong previousWindowCount;
        private final AtomicLong currentWindowCount;
        private volatile long currentWindowStart;
        
        public SlidingWindowCounter() {
            this.previousWindowCount = new AtomicLong(0);
            this.currentWindowCount = new AtomicLong(0);
            long currentTime = System.currentTimeMillis();
            this.currentWindowStart = currentTime;
        }
        
        public RateLimitResult tryConsume(int tokensRequested, long capacity, long windowSizeMs) {
            long currentTime = System.currentTimeMillis();
            updateWindows(currentTime, windowSizeMs);
            
            long estimatedCount = getEstimatedCount(currentTime, windowSizeMs);
            
            if (estimatedCount + tokensRequested <= capacity) {
                currentWindowCount.addAndGet(tokensRequested);
                long remainingTokens = capacity - (estimatedCount + tokensRequested);
                long resetTime = currentWindowStart + windowSizeMs;
                return RateLimitResult.allowed(remainingTokens, resetTime);
            } else {
                long resetTime = currentWindowStart + windowSizeMs;
                long retryAfterMs = resetTime - currentTime;
                return RateLimitResult.denied(retryAfterMs, resetTime);
            }
        }
        
        private void updateWindows(long currentTime, long windowSizeMs) {
            long expectedCurrentWindowStart = (currentTime / windowSizeMs) * windowSizeMs;
            
            if (expectedCurrentWindowStart != currentWindowStart) {
                if (expectedCurrentWindowStart == currentWindowStart + windowSizeMs) {
                    previousWindowCount.set(currentWindowCount.get());
                    currentWindowStart = expectedCurrentWindowStart;
                    currentWindowCount.set(0);
                } else {
                    previousWindowCount.set(0);
                    currentWindowStart = expectedCurrentWindowStart;
                    currentWindowCount.set(0);
                }
            }
        }
        
        private long getEstimatedCount(long currentTime, long windowSizeMs) {
            long timeIntoCurrentWindow = currentTime - currentWindowStart;
            double weightOfPreviousWindow = 1.0 - (double) timeIntoCurrentWindow / windowSizeMs;
            
            long weightedPreviousCount = (long) (previousWindowCount.get() * Math.max(0, weightOfPreviousWindow));
            return weightedPreviousCount + currentWindowCount.get();
        }
    }
}
