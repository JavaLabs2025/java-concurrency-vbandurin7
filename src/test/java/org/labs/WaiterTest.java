package org.labs;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WaiterTest {

    @Test
    public void testWaiterPriorityQueue() throws InterruptedException {
        PriorityBlockingQueue<WaiterTask> queue = new PriorityBlockingQueue<>();
        AtomicInteger portions = new AtomicInteger(100);

        queue.offer(createTask(10, portions));
        queue.offer(createTask(5, portions));
        queue.offer(createTask(15, portions));

        assertEquals(5, queue.take().getEatenBefore());
        assertEquals(10, queue.take().getEatenBefore());
        assertEquals(15, queue.take().getEatenBefore());
    }

    private WaiterTask createTask(int eatenBefore, AtomicInteger portions) {
        return new WaiterTask(eatenBefore, portions, new CompletableFuture<>());
    }
}