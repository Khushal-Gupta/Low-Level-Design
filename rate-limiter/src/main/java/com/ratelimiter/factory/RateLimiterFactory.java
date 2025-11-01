package com.ratelimiter.factory;

import com.ratelimiter.algorithms.FixedWindowCounterRateLimiter;
import com.ratelimiter.algorithms.SlidingWindowCounterRateLimiter;
import com.ratelimiter.algorithms.SlidingWindowLogRateLimiter;
import com.ratelimiter.algorithms.TokenBucketRateLimiter;
import com.ratelimiter.core.RateLimitAlgorithm;
import com.ratelimiter.core.RateLimitConfig;
import com.ratelimiter.core.RateLimiter;

public class RateLimiterFactory {
    
    public static RateLimiter create(RateLimitConfig config) {
        switch (config.getAlgorithm()) {
            case TOKEN_BUCKET:
                return new TokenBucketRateLimiter(config);
            case SLIDING_WINDOW_LOG:
                return new SlidingWindowLogRateLimiter(config);
            case FIXED_WINDOW_COUNTER:
                return new FixedWindowCounterRateLimiter(config);
            case SLIDING_WINDOW_COUNTER:
                return new SlidingWindowCounterRateLimiter(config);
            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + config.getAlgorithm());
        }
    }
    
    public static RateLimiter createTokenBucket(long capacity, long refillRate) {
        RateLimitConfig config = RateLimitConfig.builder()
                .capacity(capacity)
                .refillRate(refillRate)
                .algorithm(RateLimitAlgorithm.TOKEN_BUCKET)
                .build();
        return new TokenBucketRateLimiter(config);
    }
    
    public static RateLimiter createSlidingWindowLog(long capacity, java.time.Duration timeWindow) {
        RateLimitConfig config = RateLimitConfig.builder()
                .capacity(capacity)
                .timeWindow(timeWindow)
                .algorithm(RateLimitAlgorithm.SLIDING_WINDOW_LOG)
                .build();
        return new SlidingWindowLogRateLimiter(config);
    }
    
    public static RateLimiter createFixedWindowCounter(long capacity, java.time.Duration timeWindow) {
        RateLimitConfig config = RateLimitConfig.builder()
                .capacity(capacity)
                .timeWindow(timeWindow)
                .algorithm(RateLimitAlgorithm.FIXED_WINDOW_COUNTER)
                .build();
        return new FixedWindowCounterRateLimiter(config);
    }
    
    public static RateLimiter createSlidingWindowCounter(long capacity, java.time.Duration timeWindow) {
        RateLimitConfig config = RateLimitConfig.builder()
                .capacity(capacity)
                .timeWindow(timeWindow)
                .algorithm(RateLimitAlgorithm.SLIDING_WINDOW_COUNTER)
                .build();
        return new SlidingWindowCounterRateLimiter(config);
    }
}
