package com.ratelimiter.algorithms;

import com.ratelimiter.core.RateLimitAlgorithm;
import com.ratelimiter.core.RateLimitConfig;
import com.ratelimiter.core.RateLimitResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SlidingWindowCounterRateLimiterTest {
    
    private SlidingWindowCounterRateLimiter rateLimiter;
    
    @BeforeEach
    void setUp() {
        RateLimitConfig config = RateLimitConfig.builder()
                .capacity(5)
                .timeWindow(Duration.ofSeconds(2))
                .algorithm(RateLimitAlgorithm.SLIDING_WINDOW_COUNTER)
                .build();
        rateLimiter = new SlidingWindowCounterRateLimiter(config);
    }
    
    @Test
    void testAllowRequestWithinLimit() {
        for (int i = 0; i < 5; i++) {
            RateLimitResult result = rateLimiter.isAllowed("user1");
            assertTrue(result.isAllowed());
        }
    }
    
    @Test
    void testDenyRequestExceedingLimit() {
        for (int i = 0; i < 5; i++) {
            rateLimiter.isAllowed("user1");
        }
        
        RateLimitResult result = rateLimiter.isAllowed("user1");
        assertFalse(result.isAllowed());
    }
    
    @Test
    void testSlidingWindowBehavior() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            rateLimiter.isAllowed("user1");
        }
        
        RateLimitResult result = rateLimiter.isAllowed("user1");
        assertFalse(result.isAllowed());
        
        Thread.sleep(1000);
        
        result = rateLimiter.isAllowed("user1");
        assertTrue(result.isAllowed());
    }
    
    @Test
    void testMultipleTokenConsumption() {
        RateLimitResult result = rateLimiter.isAllowed("user1", 3);
        assertTrue(result.isAllowed());
        
        result = rateLimiter.isAllowed("user1", 3);
        assertFalse(result.isAllowed());
        
        result = rateLimiter.isAllowed("user1", 2);
        assertTrue(result.isAllowed());
    }
    
    @Test
    void testReset() {
        for (int i = 0; i < 5; i++) {
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
        for (int i = 0; i < 5; i++) {
            rateLimiter.isAllowed("user1");
        }
        
        RateLimitResult result1 = rateLimiter.isAllowed("user1");
        RateLimitResult result2 = rateLimiter.isAllowed("user2");
        
        assertFalse(result1.isAllowed());
        assertTrue(result2.isAllowed());
    }
}
