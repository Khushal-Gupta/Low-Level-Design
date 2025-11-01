package com.ratelimiter.demo;

import com.ratelimiter.core.RateLimitResult;
import com.ratelimiter.core.RateLimiter;
import com.ratelimiter.factory.RateLimiterFactory;

import java.time.Duration;

public class RateLimiterDemo {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Rate Limiter Demo ===\n");
        
        demonstrateTokenBucket();
        System.out.println();
        
        demonstrateSlidingWindowLog();
        System.out.println();
        
        demonstrateFixedWindowCounter();
        System.out.println();
        
        demonstrateSlidingWindowCounter();
    }
    
    private static void demonstrateTokenBucket() throws InterruptedException {
        System.out.println("1. Token Bucket Algorithm Demo");
        System.out.println("Configuration: 5 tokens capacity, 2 tokens/second refill rate");
        
        RateLimiter rateLimiter = RateLimiterFactory.createTokenBucket(5, 2);
        String userId = "user123";
        
        System.out.println("\nMaking 6 requests rapidly:");
        for (int i = 1; i <= 6; i++) {
            RateLimitResult result = rateLimiter.isAllowed(userId);
            System.out.printf("Request %d: %s (Remaining: %d)\n", 
                i, result.isAllowed() ? "ALLOWED" : "DENIED", result.getRemainingTokens());
        }
        
        System.out.println("\nWaiting 2 seconds for token refill...");
        Thread.sleep(2000);
        
        RateLimitResult result = rateLimiter.isAllowed(userId);
        System.out.printf("After refill: %s (Remaining: %d)\n", 
            result.isAllowed() ? "ALLOWED" : "DENIED", result.getRemainingTokens());
    }
    
    private static void demonstrateSlidingWindowLog() throws InterruptedException {
        System.out.println("2. Sliding Window Log Algorithm Demo");
        System.out.println("Configuration: 3 requests per 2 seconds");
        
        RateLimiter rateLimiter = RateLimiterFactory.createSlidingWindowLog(3, Duration.ofSeconds(2));
        String userId = "user456";
        
        System.out.println("\nMaking 4 requests rapidly:");
        for (int i = 1; i <= 4; i++) {
            RateLimitResult result = rateLimiter.isAllowed(userId);
            System.out.printf("Request %d: %s (Remaining: %d)\n", 
                i, result.isAllowed() ? "ALLOWED" : "DENIED", result.getRemainingTokens());
        }
        
        System.out.println("\nWaiting 1 second...");
        Thread.sleep(1000);
        
        RateLimitResult result = rateLimiter.isAllowed(userId);
        System.out.printf("After 1 second: %s (Remaining: %d)\n", 
            result.isAllowed() ? "ALLOWED" : "DENIED", result.getRemainingTokens());
        
        System.out.println("\nWaiting another 1.5 seconds...");
        Thread.sleep(1500);
        
        result = rateLimiter.isAllowed(userId);
        System.out.printf("After window slides: %s (Remaining: %d)\n", 
            result.isAllowed() ? "ALLOWED" : "DENIED", result.getRemainingTokens());
    }
    
    private static void demonstrateFixedWindowCounter() throws InterruptedException {
        System.out.println("3. Fixed Window Counter Algorithm Demo");
        System.out.println("Configuration: 4 requests per 2 seconds");
        
        RateLimiter rateLimiter = RateLimiterFactory.createFixedWindowCounter(4, Duration.ofSeconds(2));
        String userId = "user789";
        
        System.out.println("\nMaking 5 requests rapidly:");
        for (int i = 1; i <= 5; i++) {
            RateLimitResult result = rateLimiter.isAllowed(userId);
            System.out.printf("Request %d: %s (Remaining: %d)\n", 
                i, result.isAllowed() ? "ALLOWED" : "DENIED", result.getRemainingTokens());
        }
        
        System.out.println("\nWaiting for window reset (2.5 seconds)...");
        Thread.sleep(2500);
        
        RateLimitResult result = rateLimiter.isAllowed(userId);
        System.out.printf("After window reset: %s (Remaining: %d)\n", 
            result.isAllowed() ? "ALLOWED" : "DENIED", result.getRemainingTokens());
    }
    
    private static void demonstrateSlidingWindowCounter() throws InterruptedException {
        System.out.println("4. Sliding Window Counter Algorithm Demo");
        System.out.println("Configuration: 6 requests per 3 seconds");
        
        RateLimiter rateLimiter = RateLimiterFactory.createSlidingWindowCounter(6, Duration.ofSeconds(3));
        String userId = "user101";
        
        System.out.println("\nMaking 7 requests rapidly:");
        for (int i = 1; i <= 7; i++) {
            RateLimitResult result = rateLimiter.isAllowed(userId);
            System.out.printf("Request %d: %s (Remaining: %d)\n", 
                i, result.isAllowed() ? "ALLOWED" : "DENIED", result.getRemainingTokens());
        }
        
        System.out.println("\nWaiting 1.5 seconds (halfway through window)...");
        Thread.sleep(1500);
        
        RateLimitResult result = rateLimiter.isAllowed(userId);
        System.out.printf("After 1.5 seconds: %s (Remaining: %d)\n", 
            result.isAllowed() ? "ALLOWED" : "DENIED", result.getRemainingTokens());
        
        System.out.println("\nWaiting another 2 seconds...");
        Thread.sleep(2000);
        
        result = rateLimiter.isAllowed(userId);
        System.out.printf("After sliding window: %s (Remaining: %d)\n", 
            result.isAllowed() ? "ALLOWED" : "DENIED", result.getRemainingTokens());
    }
}
