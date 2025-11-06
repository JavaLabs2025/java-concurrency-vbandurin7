package org.labs;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class WaiterTask implements Runnable, Comparable<WaiterTask> {
    private final int eatenBefore;
    private final AtomicInteger portions;
    private final CompletableFuture<Boolean> resultFuture;

    public WaiterTask(int eatenBefore, AtomicInteger portions, CompletableFuture<Boolean> resultFuture) {
        this.eatenBefore = eatenBefore;
        this.portions = portions;
        this.resultFuture = resultFuture;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(TimeUtil.randomTime(TimeUtil.MIN_WAIT_TIME, TimeUtil.MAX_WAIT_TIME));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int remainingPortions = portions.get();
        if (remainingPortions % 5000 == 0) {
            System.out.println("Portions left - " + remainingPortions);
        }
        boolean result = portions.getAndDecrement() > 0;
        resultFuture.complete(result);
    }

    @Override
    public int compareTo(WaiterTask o) {
        return Integer.compare(this.eatenBefore, o.eatenBefore);
    }

    public int getEatenBefore() {
        return eatenBefore;
    }
}
