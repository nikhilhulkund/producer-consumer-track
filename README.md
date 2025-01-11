# Message-Driven Producer-Consumer Application

## Overview
This project simulates a **producer-consumer** scenario using core Java and a message queue. It demonstrates a basic messaging system where:
- A **producer** generates messages.
- A **consumer** processes the messages.
- The system tracks the success and failure of message processing.
- Logs all activities using **Log4j**.

## Key Features
- **Message Queue:** A simple message queue with producer-consumer behavior using `BlockingQueue`.
- **Logging:** Tracks message processing success, failures, and logs system events with Log4j.
- **Concurrency:** Uses `ExecutorService` to manage the producer and consumer threads concurrently.
- **Error Handling:** Tracks errors when processing messages with proper exception handling and logging.
- **Termination:** The consumer stops when it encounters a `TERMINATE` signal.

## Technologies Used
- **Java 17:** Core Java for the implementation.
- **Maven:** Build automation tool for dependency management and packaging.
- **Log4j 2:** For logging message processing, errors, and application state.
- **JUnit:** For unit testing the application, including success and failure scenarios.
- **Mockito:** For mocking dependencies during testing.

## Installation and Setup

### Prerequisites
Before you begin, make sure you have the following installed on your system:
- **Java 17:** Ensure you have JDK 11 or higher installed.
- **Maven:** You need Maven to build and run the project. Install it from [Maven's official site](https://maven.apache.org/).

### Steps to Run the Application
 1. **Clone the repository to your local machine:**
  ``` 
git clone https://github.com/nikhilhulkund/producer-consumer-track.git
cd producer-consumer-track
  ```
 2. **Build the project using Maven:**
``` 
mvn clean install
```
 3. **Run the application**
```declarative
mvn exec:java
```

## How It Works

- **Producer:** Generates a list of messages and pushes them onto the message queue. It simulates various scenarios (success, client error, server error) and signals termination with a `"TERMINATE"` message.
- **Consumer:** Continuously consumes messages from the queue, processes them, and tracks successful and failed message processing. The consumer stops when it encounters a `"TERMINATE"` message.
- **MessageQueue:** A simple thread-safe queue using `BlockingQueue` to store and retrieve messages. The queue has a fixed capacity to simulate real-world limitations.

## Key Classes:

- **Producer:** Simulates the producer of messages and pushes them onto the queue.
- **Consumer:** Simulates the consumer that processes the messages from the queue.
- **MessageQueue:** A custom class implementing the queue with a `BlockingQueue` to manage messages.
- **Message:** A simple POJO representing a message with a status code and content.

## Logging

We use **Log4j 2** to log the following:

- Success and failure messages.
- Errors during message processing.
- Thread execution details for better debugging.
- Logs are saved in the console and are essential for understanding the application's flow and state.

##### Example Logs
Here are some examples of log outputs you can expect from the application:
```declarative
INFO  [main] - Producer Thread: Finished producing messages.
INFO  [main] - Consumer Thread: Finished processing messages. Success: 3, Errors: 2
ERROR [main] - Error processing message: Message{statusCode='500', content='Internal Server Error'}
INFO  [main] - Consumed message: Message{statusCode='200', content='OK'}
```
____________________________________________________________________________________________
## Testing
### Unit Tests
The application includes unit tests for the core components. These tests ensure that the producer, consumer, and message queue behave as expected.
- **MessageQueue Tests:**
  - Verifies that messages are produced and consumed correctly.
  - Ensures that consuming from an empty queue blocks the thread until a message is available.
- **Producer Tests:**
  - Verifies that the producer correctly pushes messages onto the queue.
- **Consumer Tests:**
  - Tests the consumer's behavior, including handling errors and terminating on the `TERMINATE` message.
- **Main Execution Flow Tests:**
  - Verifies that the producer and consumer run as expected.
  - Ensures proper logging and shutdown of threads.

### Running Tests
To run the tests using Maven, use the following command:
``` 
mvn Test
```

### Test Coverage
- **Success Scenarios:** Ensures that messages with valid status codes (e.g., 200, 201) are processed successfully.
- **Failure Scenarios:** Ensures that messages with error status codes (e.g., 400, 500) are handled as failures.
- **Queue Behavior:** Tests the queue's capacity and consumption behavior under various scenarios.

_________________________________________________________________________________________

## Conclusion
This project provides a simple yet powerful example of how to implement a message-driven system in Java using concurrency and message queues. It covers important aspects of logging, error handling, and testing, making it a great learning tool for developers interested in message-driven applications and multi-threading.