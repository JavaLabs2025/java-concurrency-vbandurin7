package org.labs;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Waiter extends Thread {

    private final int id;
    private final BlockingQueue<WaiterTask> taskQueue;

    public Waiter(int id, BlockingQueue<WaiterTask> taskQueue) {
        this.id = id;
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                WaiterTask task = taskQueue.poll(1, TimeUnit.SECONDS);
                if (task != null) {
                    task.call();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.printf("Waiter-%d is shutting down%n", id);
    }

    public void stopWorking() {
        this.interrupt();
    }
}
