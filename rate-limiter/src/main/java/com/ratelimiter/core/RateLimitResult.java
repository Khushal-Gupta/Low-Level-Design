package com.ratelimiter.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RateLimitResult {
    private final boolean allowed;
    private final long remainingTokens;
    private final long retryAfterMs;
    private final long resetTimeMs;
    
    public static RateLimitResult allowed(long remainingTokens, long resetTimeMs) {
        return new RateLimitResult(true, remainingTokens, 0, resetTimeMs);
    }
    
    public static RateLimitResult denied(long retryAfterMs, long resetTimeMs) {
        return new RateLimitResult(false, 0, retryAfterMs, resetTimeMs);
    }
}
