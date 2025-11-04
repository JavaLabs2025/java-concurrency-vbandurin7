package org.labs;

import java.util.concurrent.locks.ReentrantLock;

public class SpoonManager {
    private final int resourceCount;
    private final ReentrantLock[] issued;

    public SpoonManager(int resourceCount) {
        this.resourceCount = resourceCount;
        this.issued = new ReentrantLock[resourceCount];
        for (int i = 0; i < resourceCount; i++) {
            issued[i] = new ReentrantLock();
        }
    }

    public void aquireSpoons(int programmerId) {
        int leftSpoonIdx = leftSpoonIdx(programmerId);
        int rightSpoonIdx = rightSpoonIdx(programmerId);

        issued[Math.min(leftSpoonIdx, rightSpoonIdx)].lock();
        issued[Math.max(leftSpoonIdx, rightSpoonIdx)].lock();
    }

    public void releaseSpoons(int programmerId) {
        issued[leftSpoonIdx(programmerId)].unlock();
        issued[rightSpoonIdx(programmerId)].unlock();
    }

    private int rightSpoonIdx(int programmerId) {
        return programmerId;
    }

    private int leftSpoonIdx(int programmerId) {
        return (programmerId + 1) % resourceCount;
    }
}
