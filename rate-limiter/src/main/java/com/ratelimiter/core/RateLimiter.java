package com.ratelimiter.core;

public interface RateLimiter {
    
    RateLimitResult isAllowed(String key);
    
    RateLimitResult isAllowed(String key, int tokens);
    
    void reset(String key);
    
    RateLimitConfig getConfig();
}
