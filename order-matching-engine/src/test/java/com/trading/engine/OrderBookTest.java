package com.trading.engine;

import com.trading.engine.core.OrderBook;
import com.trading.engine.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderBookTest {

    private OrderBook orderBook;

    @BeforeEach
    public void setUp() {
        // Initializes a fresh order book before every test case
        orderBook = new OrderBook("RELIANCE");
    }

    @Test
    public void testExactMatch() {
        Order buyOrder = new Order("B1", "RELIANCE", 2500.0, 100, Order.Side.BUY);
        Order sellOrder = new Order("S1", "RELIANCE", 2500.0, 100, Order.Side.SELL);

        orderBook.processOrder(buyOrder);
        orderBook.processOrder(sellOrder);

        assertTrue(buyOrder.isFilled(), "Buy order should be completely filled");
        assertTrue(sellOrder.isFilled(), "Sell order should be completely filled");
    }

    @Test
    public void testPartialFill() {
        Order buyOrder = new Order("B2", "RELIANCE", 2510.0, 100, Order.Side.BUY);
        Order sellOrder = new Order("S2", "RELIANCE", 2500.0, 50, Order.Side.SELL);

        // Buy order price is higher than sell price, so they should match
        orderBook.processOrder(buyOrder);
        orderBook.processOrder(sellOrder);

        assertFalse(buyOrder.isFilled(), "Buy order should only be partially filled");
        assertEquals(50, buyOrder.getQuantity(), "Buy order should have 50 shares remaining");
        assertTrue(sellOrder.isFilled(), "Sell order should be completely filled");
    }

    @Test
    public void testNoMatch() {
        Order buyOrder = new Order("B3", "RELIANCE", 2490.0, 100, Order.Side.BUY);
        Order sellOrder = new Order("S3", "RELIANCE", 2500.0, 100, Order.Side.SELL);

        // Buyer wants to buy at 2490, Seller wants 2500. No match possible.
        orderBook.processOrder(buyOrder);
        orderBook.processOrder(sellOrder);

        assertFalse(buyOrder.isFilled());
        assertFalse(sellOrder.isFilled());
        assertEquals(100, buyOrder.getQuantity());
        assertEquals(100, sellOrder.getQuantity());
    }
}
