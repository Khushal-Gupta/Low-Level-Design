package com.ratelimiter.algorithms;

import com.ratelimiter.core.RateLimitConfig;
import com.ratelimiter.core.RateLimitResult;
import com.ratelimiter.core.RateLimiter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class TokenBucketRateLimiter implements RateLimiter {
    
    private final RateLimitConfig config;
    private final ConcurrentHashMap<String, TokenBucket> buckets;
    private final ConcurrentHashMap<String, ReentrantLock> locks;
    
    public TokenBucketRateLimiter(RateLimitConfig config) {
        this.config = config;
        this.buckets = new ConcurrentHashMap<>();
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
            TokenBucket bucket = buckets.computeIfAbsent(key, k -> new TokenBucket(config.getCapacity(), config.getRefillRate()));
            return bucket.tryConsume(tokens);
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
                buckets.remove(key);
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
    
    private static class TokenBucket {
        private final long capacity;
        private final long refillRate;
        private long tokens;
        private long lastRefillTime;
        
        public TokenBucket(long capacity, long refillRate) {
            this.capacity = capacity;
            this.refillRate = refillRate;
            this.tokens = capacity;
            this.lastRefillTime = System.currentTimeMillis();
        }
        
        public RateLimitResult tryConsume(int tokensRequested) {
            refill();
            
            if (tokens >= tokensRequested) {
                tokens -= tokensRequested;
                return RateLimitResult.allowed(tokens, getNextRefillTime());
            } else {
                long retryAfterMs = calculateRetryAfter(tokensRequested);
                return RateLimitResult.denied(retryAfterMs, getNextRefillTime());
            }
        }
        
        private void refill() {
            long currentTime = System.currentTimeMillis();
            long timePassed = currentTime - lastRefillTime;
            long tokensToAdd = (timePassed * refillRate) / 1000;
            
            if (tokensToAdd > 0) {
                tokens = Math.min(capacity, tokens + tokensToAdd);
                lastRefillTime = currentTime;
            }
        }
        
        private long calculateRetryAfter(int tokensRequested) {
            long tokensNeeded = tokensRequested - tokens;
            return (tokensNeeded * 1000) / refillRate;
        }
        
        private long getNextRefillTime() {
            if (tokens < capacity) {
                long tokensNeeded = capacity - tokens;
                return lastRefillTime + (tokensNeeded * 1000) / refillRate;
            }
            return lastRefillTime;
        }
    }
}
