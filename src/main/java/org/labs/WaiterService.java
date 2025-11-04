package org.labs;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Сервис для обслуживания программистов
 */
public class WaiterService {
    private final AtomicInteger portions;
    private final PriorityBlockingQueue<WaiterTask> blockingQueue;
    private final Waiter[] waiters;

    public WaiterService(int waitersCount, int portionCount) {
        blockingQueue = new PriorityBlockingQueue<>();
        portions = new AtomicInteger(portionCount);
        waiters = new Waiter[waitersCount];
        for (int i = 0; i < waitersCount; i++) {
            waiters[i] = new Waiter(i, blockingQueue);
        }

        startWaiters();
    }

    public CompletableFuture<Boolean> requestFood(int eatenBefore) throws InterruptedException {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        blockingQueue.offer(new WaiterTask(eatenBefore, portions, future));
        return future;
    }

    private void startWaiters() {
        for (Waiter waiter : waiters) {
            waiter.start();
        }
    }

    public void shutdown() {
        for (Waiter waiter : waiters) {
            waiter.stopWorking();
        }

        for (Waiter waiter : waiters) {
            try {
                waiter.join(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
