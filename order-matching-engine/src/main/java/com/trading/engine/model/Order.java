package com.trading.engine.model;

public class Order {
    public enum Side {
        BUY, SELL
    }

    private final String orderId;
    private final String symbol;
    private final double price;
    private int quantity; // Mutable because quantity decreases when partially filled
    private final Side side;
    private final long timestamp;

    public Order(String orderId, String symbol, double price, int quantity, Side side) {
        this.orderId = orderId;
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.side = side;
        this.timestamp = System.nanoTime(); // Used for Time-Priority matching
    }

    public String getOrderId() { return orderId; }
    public String getSymbol() { return symbol; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public Side getSide() { return side; }
    public long getTimestamp() { return timestamp; }

    public void reduceQuantity(int amount) {
        if (amount > this.quantity) {
            throw new IllegalArgumentException("Cannot reduce more than available quantity");
        }
        this.quantity -= amount;
    }

    public boolean isFilled() {
        return this.quantity == 0;
    }

    @Override
    public String toString() {
        return String.format("Order[%s %s %d @ %.2f - %s]", side, symbol, quantity, price, orderId);
    }
}
