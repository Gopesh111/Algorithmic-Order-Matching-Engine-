package com.trading.engine.core;

import com.trading.engine.model.Order;
import java.util.Comparator;
import java.util.PriorityQueue;

public class OrderBook {
    private final String symbol;

    // Buy Orders (Bids): Highest price first. If same price, oldest timestamp first.
    private final PriorityQueue<Order> bids;
    
    // Sell Orders (Asks): Lowest price first. If same price, oldest timestamp first.
    private final PriorityQueue<Order> asks;

    public OrderBook(String symbol) {
        this.symbol = symbol;
        
        this.bids = new PriorityQueue<>(
            Comparator.comparingDouble(Order::getPrice).reversed()
                      .thenComparingLong(Order::getTimestamp)
        );
        
        this.asks = new PriorityQueue<>(
            Comparator.comparingDouble(Order::getPrice)
                      .thenComparingLong(Order::getTimestamp)
        );
    }

    // Synchronized keyword prevents Race Conditions when multiple threads submit orders
    public synchronized void processOrder(Order order) {
        System.out.println("Received: " + order);
        
        if (order.getSide() == Order.Side.BUY) {
            matchOrder(order, asks, bids);
        } else {
            matchOrder(order, bids, asks);
        }
    }

    private void matchOrder(Order incomingOrder, PriorityQueue<Order> opposingBook, PriorityQueue<Order> sameBook) {
        while (!opposingBook.isEmpty() && !incomingOrder.isFilled()) {
            Order bestOpposingOrder = opposingBook.peek();

            // Check if price matches
            boolean isMatch = incomingOrder.getSide() == Order.Side.BUY ? 
                              incomingOrder.getPrice() >= bestOpposingOrder.getPrice() : 
                              incomingOrder.getPrice() <= bestOpposingOrder.getPrice();

            if (!isMatch) {
                break; // No more matching prices available
            }

            int tradeQuantity = Math.min(incomingOrder.getQuantity(), bestOpposingOrder.getQuantity());
            double executionPrice = bestOpposingOrder.getPrice(); // Trade executes at maker's price

            System.out.printf(">>> TRADE EXECUTED: %d shares of %s @ %.2f (Taker: %s, Maker: %s)\n",
                    tradeQuantity, symbol, executionPrice, incomingOrder.getOrderId(), bestOpposingOrder.getOrderId());

            incomingOrder.reduceQuantity(tradeQuantity);
            bestOpposingOrder.reduceQuantity(tradeQuantity);

            // Remove filled orders from the book
            if (bestOpposingOrder.isFilled()) {
                opposingBook.poll();
            }
        }

        // If incoming order is not fully filled, add it to the book to wait for future matches
        if (!incomingOrder.isFilled()) {
            sameBook.add(incomingOrder);
            System.out.println("Added to book: " + incomingOrder);
        }
    }
}
