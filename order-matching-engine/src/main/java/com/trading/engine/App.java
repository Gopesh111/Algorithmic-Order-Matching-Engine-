package com.trading.engine;

import com.trading.engine.core.OrderBook;
import com.trading.engine.model.Order;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class App {
    public static void main(String[] args) {
        System.out.println("--- Starting Algorithmic Order Matching Engine ---");
        
        OrderBook appleOrderBook = new OrderBook("AAPL");

        // Simulating a high-concurrency environment using a Thread Pool
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // Simulating multiple traders sending orders concurrently
        Runnable trader1 = () -> {
            appleOrderBook.processOrder(new Order("O1", "AAPL", 150.00, 100, Order.Side.BUY));
            appleOrderBook.processOrder(new Order("O2", "AAPL", 151.00, 50, Order.Side.BUY));
        };

        Runnable trader2 = () -> {
            appleOrderBook.processOrder(new Order("O3", "AAPL", 149.00, 120, Order.Side.SELL));
        };

        Runnable trader3 = () -> {
            appleOrderBook.processOrder(new Order("O4", "AAPL", 150.50, 40, Order.Side.SELL));
            appleOrderBook.processOrder(new Order("O5", "AAPL", 152.00, 200, Order.Side.SELL));
        };

        // Submit tasks to the thread pool
        executorService.submit(trader1);
        executorService.submit(trader2);
        executorService.submit(trader3);

        // Shut down the executor gracefully
        executorService.shutdown();
        try {
            executorService.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("--- Trading Session Ended ---");
    }
}
