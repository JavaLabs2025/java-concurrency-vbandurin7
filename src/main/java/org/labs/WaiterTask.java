package org.labs;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class WaiterTask implements Callable<Boolean>, Comparable<WaiterTask> {
    private final int eatenBefore;
    private final AtomicInteger portions;
    private final CompletableFuture<Boolean> resultFuture;

    public WaiterTask(int eatenBefore, AtomicInteger portions, CompletableFuture<Boolean> resultFuture) {
        this.eatenBefore = eatenBefore;
        this.portions = portions;
        this.resultFuture = resultFuture;
    }

    @Override
    public Boolean call() {
        try {
            Thread.sleep(TimeUtil.randomTime(TimeUtil.MIN_WAIT_TIME, TimeUtil.MAX_WAIT_TIME));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        boolean result = portions.getAndDecrement() > 0;
        resultFuture.complete(result);
        return result;
    }

    @Override
    public int compareTo(WaiterTask o) {
        return Integer.compare(this.eatenBefore, o.eatenBefore);
    }
}
