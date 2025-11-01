package com.ratelimiter.algorithms;

import com.ratelimiter.core.RateLimitAlgorithm;
import com.ratelimiter.core.RateLimitConfig;
import com.ratelimiter.core.RateLimitResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenBucketRateLimiterTest {
    
    private TokenBucketRateLimiter rateLimiter;
    
    @BeforeEach
    void setUp() {
        RateLimitConfig config = RateLimitConfig.builder()
                .capacity(10)
                .refillRate(1)
                .algorithm(RateLimitAlgorithm.TOKEN_BUCKET)
                .build();
        rateLimiter = new TokenBucketRateLimiter(config);
    }
    
    @Test
    void testAllowRequestWhenTokensAvailable() {
        RateLimitResult result = rateLimiter.isAllowed("user1");
        assertTrue(result.isAllowed());
    }
    
    @Test
    void testDenyRequestWhenTokensExhausted() {
        for (int i = 0; i < 10; i++) {
            rateLimiter.isAllowed("user1");
        }
        
        RateLimitResult result = rateLimiter.isAllowed("user1");
        assertFalse(result.isAllowed());
    }
    
    @Test
    void testMultipleTokenConsumption() {
        RateLimitResult result = rateLimiter.isAllowed("user1", 5);
        assertTrue(result.isAllowed());
        
        result = rateLimiter.isAllowed("user1", 6);
        assertFalse(result.isAllowed());
    }
    
    @Test
    void testTokenRefill() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            rateLimiter.isAllowed("user1");
        }
        
        RateLimitResult result = rateLimiter.isAllowed("user1");
        assertFalse(result.isAllowed());
        
        Thread.sleep(1100);
        
        result = rateLimiter.isAllowed("user1");
        assertTrue(result.isAllowed());
    }
    
    @Test
    void testReset() {
        for (int i = 0; i < 10; i++) {
            rateLimiter.isAllowed("user1");
        }
        
        RateLimitResult result = rateLimiter.isAllowed("user1");
        assertFalse(result.isAllowed());
        
        rateLimiter.reset("user1");
        
        result = rateLimiter.isAllowed("user1");
        assertTrue(result.isAllowed());
    }
    
    @Test
    void testDifferentKeys() {
        for (int i = 0; i < 10; i++) {
            rateLimiter.isAllowed("user1");
        }
        
        RateLimitResult result1 = rateLimiter.isAllowed("user1");
        RateLimitResult result2 = rateLimiter.isAllowed("user2");
        
        assertFalse(result1.isAllowed());
        assertTrue(result2.isAllowed());
    }
}
