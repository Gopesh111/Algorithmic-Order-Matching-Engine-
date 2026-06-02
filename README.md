# Algorithmic Order Matching Engine

## Overview
A high-performance, multithreaded order matching engine built in Java. This backend system simulates the core operations of a financial exchange, matching buy and sell orders strictly based on Price-Time Priority rules.

## Technical Stack
* Language: Java 11+
* Testing: JUnit 5
* Build Tool: Apache Maven

## Core Architecture
The system relies on Object-Oriented principles and concurrent data structures to ensure speed, stability, and thread safety in a high-throughput environment:

* Data Structures: The order book is implemented using two Priority Queues. A Max-Heap manages Buy orders (Bids) to prioritize the highest prices, while a Min-Heap manages Sell orders (Asks) to prioritize the lowest prices. Insertion and extraction operate at O(log N) time complexity.
* Thread Safety: To handle a multithreaded environment where multiple clients route orders concurrently, synchronized blocks are utilized at the core matching engine level. This ensures strict thread safety and prevents race conditions during order mutation.
* Matching Logic: Follows Price-Time Priority. If competing orders have the same price, the engine uses nano-second precision timestamps to prioritize older orders.

## Getting Started

### Prerequisites
* Java Development Kit (JDK) 11 or higher
* Apache Maven installed and added to system path

### Build and Run Instructions

1. Clone this repository to your local machine.
2. Navigate to the root directory of the project.
3. Clean and compile the project using Maven:
```bash
   mvn clean install
   ```
4. Run the JUnit test suite to verify matching logic and stability:
```bash
   mvn test
   ```
5. Execute the main application to see the multithreaded simulation in action:
```bash
   mvn exec:java -Dexec.mainClass="com.trading.engine.App"
   ```
