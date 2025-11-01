# Rate Limiter - Low Level Design

A comprehensive implementation of various rate limiting algorithms in Java, designed for high-performance applications with in-memory storage.

## Features

- **Multiple Algorithms**: Token Bucket, Sliding Window Log, Fixed Window Counter
- **Thread-Safe**: All implementations are thread-safe using concurrent data structures
- **In-Memory Storage**: Fast, local rate limiting without external dependencies
- **Flexible Configuration**: Builder pattern for easy configuration
- **Factory Pattern**: Easy instantiation of different rate limiter types
- **Comprehensive Testing**: Full unit test coverage

## Algorithms Implemented

### 1. Token Bucket
- **Use Case**: Smooth rate limiting with burst capability
- **How it works**: Tokens are added to a bucket at a fixed rate. Each request consumes tokens.
- **Pros**: Allows bursts up to bucket capacity, smooth token refill
- **Cons**: More complex implementation

### 2. Sliding Window Log
- **Use Case**: Precise rate limiting with exact request tracking
- **How it works**: Maintains a log of request timestamps within a sliding time window
- **Pros**: Most accurate, no boundary effects
- **Cons**: Higher memory usage, more complex cleanup

### 3. Fixed Window Counter
- **Use Case**: Simple rate limiting with fixed time windows
- **How it works**: Counts requests in fixed time windows
- **Pros**: Simple implementation, low memory usage
- **Cons**: Boundary effects, potential burst at window boundaries

## Usage Examples

### Token Bucket
```java
RateLimiter rateLimiter = RateLimiterFactory.createTokenBucket(10, 5); // 10 capacity, 5 tokens/sec
RateLimitResult result = rateLimiter.isAllowed("user123");
if (result.isAllowed()) {
    // Process request
} else {
    // Rate limited - retry after result.getRetryAfterMs()
}
```

### Sliding Window Log
```java
RateLimiter rateLimiter = RateLimiterFactory.createSlidingWindowLog(100, Duration.ofMinutes(1));
RateLimitResult result = rateLimiter.isAllowed("api-key-456", 5); // consume 5 tokens
```

### Fixed Window Counter
```java
RateLimiter rateLimiter = RateLimiterFactory.createFixedWindowCounter(1000, Duration.ofHours(1));
RateLimitResult result = rateLimiter.isAllowed("endpoint:/api/data");
```

## Running the Demo

```bash
mvn compile exec:java -Dexec.mainClass="com.ratelimiter.demo.RateLimiterDemo"
```

## Running Tests

```bash
mvn test
```

## Architecture

```
com.ratelimiter/
├── core/                    # Core interfaces and data structures
│   ├── RateLimiter.java    # Main interface
│   ├── RateLimitResult.java # Result object
│   ├── RateLimitConfig.java # Configuration
│   └── RateLimitAlgorithm.java # Algorithm enum
├── algorithms/              # Algorithm implementations
│   ├── TokenBucketRateLimiter.java
│   ├── SlidingWindowLogRateLimiter.java
│   └── FixedWindowCounterRateLimiter.java
├── factory/                 # Factory for creating rate limiters
│   └── RateLimiterFactory.java
└── demo/                    # Demo application
    └── RateLimiterDemo.java
```

## Key Design Decisions

1. **In-Memory Storage**: All data is stored in memory using ConcurrentHashMap for fast access
2. **Thread Safety**: Uses ReentrantLock per key to ensure thread-safe operations
3. **Builder Pattern**: Flexible configuration through builder pattern
4. **Factory Pattern**: Easy instantiation and algorithm switching
5. **Immutable Results**: RateLimitResult is immutable for thread safety

## Performance Characteristics

- **Token Bucket**: O(1) time complexity, O(n) space where n is number of unique keys
- **Sliding Window Log**: O(m) time complexity where m is requests in window, O(n*m) space
- **Fixed Window Counter**: O(1) time complexity, O(n) space where n is number of unique keys

## Thread Safety

All implementations are thread-safe:
- Uses `ConcurrentHashMap` for storing rate limit data
- `ReentrantLock` per key ensures atomic operations
- Immutable result objects prevent race conditions
