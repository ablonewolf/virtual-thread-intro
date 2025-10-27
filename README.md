# Virtual Threads Introduction

A comprehensive Java project demonstrating the concepts, usage patterns, and performance characteristics of Virtual Threads introduced in Java 19 (Preview) and finalized in Java 21.

## Overview

This repository contains practical examples showcasing how Virtual Threads work, their benefits over traditional platform threads, and various implementation patterns. Virtual Threads are lightweight threads managed by the JVM that enable high-throughput concurrent applications with simpler programming models.

## Prerequisites

- Java 25 or later
- Maven (for dependency management)

## Project Structure

```
src/main/java/org/ablonewolf/
├── basic/                          # Core virtual thread concepts
├── completableFuture/              # Integration with CompletableFuture
├── executorService/                # ExecutorService implementations
├── externalService/                # External API integration examples
└── util/                           # Utility classes
```

## External Service Setup

**Important**: Several demos in this project require an external backend service to demonstrate real-world HTTP client scenarios with virtual threads.

### Running the Backend Service

1. **Locate the backend JAR**: The backend service is provided as `product-backend.jar` in the `resources` directory.

2. **Start the service**:
   ```bash
   java -jar resources/product-backend.jar --server.port=7070
   ```
   
   The service will start on **port 7070** and provides the following endpoints:
   - `GET /productApp/product/{id}` - Get product information
   - `GET /productApp/rating/{id}` - Get product rating
   - `GET /productApp/stream/products/{id}` - Get product information (streaming)
   - `GET /productApp/stream/ratings/{id}` - Get product rating (streaming)

3. **Verify the service**: Once started, you can test the service:
   ```bash
   curl http://localhost:7070/productApp/product/1
   curl http://localhost:7070/productApp/rating/1
   ```

### Demos That Require the Backend Service

The following examples require the backend service to be running:

- **AccessResponseUsingFutureDemo**: Compares platform threads vs virtual threads for HTTP calls
- **DemonstrateConcurrencyLimit**: Shows concurrency limiting with external API calls
- **ConcurrencyLimitWithSemaphore**: Advanced concurrency control patterns
- **ReactiveAggregatorDemo**: Reactive programming with virtual threads
- **AggregatorDemo**: Service aggregation patterns

## Key Features Demonstrated

### 1. Basic Virtual Thread Operations
- **VirtualThreadsDemo**: Creating and managing 10,000 virtual threads efficiently
- **InboundOutboundTaskDemo**: Comparing platform threads vs virtual threads for I/O operations
- **ThreadFactoryDemo**: Using virtual thread factories for thread creation
- **CooperativeSchedulingDemo**: Understanding cooperative scheduling with `Thread.yield()`

### 2. Synchronization and Thread Safety
- **SynchronizationWithIODemo**: Thread pinning issues with synchronized blocks
- **ReentrantLockDemo**: Using ReentrantLock as an alternative to synchronized blocks

### 3. Performance Comparisons
- **CPUIntensiveTaskDemo**: Comparing virtual threads vs platform threads for CPU-bound tasks
- Performance analysis for I/O-bound vs CPU-bound workloads

### 4. Integration Patterns
- **ExecutorService Integration**: Using `Executors.newVirtualThreadPerTaskExecutor()`
- **CompletableFuture Integration**: Async programming with virtual threads
- **External Service Calls**: HTTP client integration with virtual threads

### 5. Real-World HTTP Client Examples
- **HTTP Performance Testing**: Compare virtual threads vs platform threads for concurrent HTTP calls
- **Service Aggregation**: Combine multiple API calls efficiently using virtual threads
- **Reactive Programming**: Integration with reactive streams and virtual threads
- **Concurrency Control**: Limit concurrent HTTP requests using semaphores and thread pools

## Getting Started

### Running the Examples

Each demo class contains a `main()` method (using Java 21's instance main methods). You can run any example directly:

```bash
# Compile the project
mvn compile

# Run a specific demo (example)
java -cp target/classes org.ablonewolf.basic.VirtualThreadsDemo
```

### Key Examples to Try

1. **Start with the basics**:
   ```bash
   java -cp target/classes org.ablonewolf.basic.VirtualThreadsDemo
   ```

2. **Compare performance**:
   ```bash
   java -cp target/classes org.ablonewolf.basic.InboundOutboundTaskDemo
   ```

3. **See thread pinning in action**:
   ```bash
   java -Djdk.tracePinnedThreads=full -cp target/classes org.ablonewolf.basic.SynchronizationWithIODemo
   ```

4. **Test external service integration** (requires backend service):
   ```bash
   # First, start the backend service
   java -jar resources/product-backend.jar --server.port=7070
   
   # Then run the HTTP client demos
   java -cp target/classes org.ablonewolf.externalService.AccessResponseUsingFutureDemo
   java -cp target/classes org.ablonewolf.executorService.DemonstrateConcurrencyLimit
   ```

## Virtual Threads Concepts Covered

### Creating Virtual Threads
```java
// Simple virtual thread creation
Thread.ofVirtual().start(() -> {
    // Your task here
});

// Named virtual threads
Thread.ofVirtual().name("MyVirtualThread").start(() -> {
    // Your task here
});

// Using thread factories
ThreadFactory factory = Thread.ofVirtual().name("Worker-", 1).factory();
```

### ExecutorService Integration
```java
// Virtual thread per task executor
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    // Submit tasks
    executor.submit(() -> {
        // Your task here
    });
}
```

### HTTP Client Integration
```java
// Using virtual threads for concurrent HTTP calls
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    List<Future<String>> futures = new ArrayList<>();
    for (int i = 1; i <= 50; i++) {
        final var productId = i;
        futures.add(executor.submit(() -> Client.getProduct(productId)));
    }
    // Process results...
}
```

### Best Practices Demonstrated

1. **Use for I/O-bound tasks**: Virtual threads excel at handling many concurrent I/O operations
2. **Avoid synchronized blocks**: Use `ReentrantLock` or other alternatives to prevent thread pinning
3. **Leverage existing APIs**: Virtual threads work seamlessly with existing concurrent APIs
4. **Monitor thread pinning**: Use JVM flags to detect and resolve pinning issues
5. **HTTP Client Optimization**: Perfect for concurrent web service calls and API aggregation

## Performance Insights

The project demonstrates several key performance characteristics:

- **Scalability**: Creating thousands of virtual threads with minimal overhead
- **Resource Efficiency**: Lower memory footprint compared to platform threads
- **I/O Optimization**: Excellent performance for blocking I/O operations
- **HTTP Concurrency**: Dramatic performance improvements for concurrent HTTP calls
- **CPU-bound Limitations**: Not necessarily better for CPU-intensive tasks

## Common Pitfalls and Solutions

### Thread Pinning
Virtual threads can be "pinned" to platform threads in certain scenarios:
- **Synchronized blocks/methods**: Use `ReentrantLock` instead
- **Native method calls**: Minimize usage in virtual thread contexts
- **Foreign function calls**: Similar to native methods

### Monitoring and Debugging
Enable virtual thread monitoring:
```bash
-Djdk.tracePinnedThreads=full
```

## Troubleshooting

### Backend Service Issues
- **Port conflicts**: Ensure port 7070 is available
- **Service not responding**: Check if the JAR file exists in the `resources` directory
- **Connection refused**: Verify the service is running before executing HTTP client demos

### Common Errors
- **ClassNotFoundException**: Ensure proper classpath when running examples
- **ConnectException**: Make sure the backend service is running on port 7070

## Contributing

Feel free to contribute additional examples or improvements to existing demonstrations. Focus areas:
- Advanced synchronization patterns
- Integration with popular frameworks
- Performance benchmarking scenarios
- Error handling patterns
- Additional HTTP client patterns

## References

- [JEP 444: Virtual Threads](https://openjdk.org/jeps/444)
- [Java Virtual Threads Documentation](https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html)
- [Virtual Threads Best Practices](https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html#GUID-704A716D-0662-4BC7-8C7F-66EE74B1EDAD)

## License

This project is open source and available under standard licensing terms.
