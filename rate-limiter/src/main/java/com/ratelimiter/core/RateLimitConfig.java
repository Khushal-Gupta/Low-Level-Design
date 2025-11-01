package com.ratelimiter.core;

import lombok.Builder;
import lombok.Getter;

import java.time.Duration;

@Getter
@Builder
public class RateLimitConfig {
    private final long capacity;
    private final long refillRate;
    private final Duration timeWindow;
    private final RateLimitAlgorithm algorithm;
}
