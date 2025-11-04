package org.labs;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Programmer extends Thread {

    private final int id;
    private final SpoonManager spoonManager;
    private final WaiterService waiterService;

    private boolean hasPortion;
    private int eaten = 0;

    public Programmer(int id, SpoonManager manager, WaiterService waiterService) {
        this.id = id;
        this.spoonManager = manager;
        this.waiterService = waiterService;
        this.hasPortion = true;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            code();

            if (!hasPortion) {
                break;
            }

            spoonManager.aquireSpoons(id);
            try {
                eat();
            } finally {
                spoonManager.releaseSpoons(id);
            }
            try {
                CompletableFuture<Boolean> getFoodFuture = waiterService.requestFood(eaten);
                hasPortion = getFoodFuture.get(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (ExecutionException e) {
                System.err.println("Waiter failed to bring new portion: " + e.getCause());
                break;
            } catch (TimeoutException e) {
                System.err.println("No response from waiter");
                break;
            }
        }
        System.out.println("Programmer-" + id + " has eaten " + eaten + " portions");
    }

    private void eat() {
        try {
            Thread.sleep(TimeUtil.randomTime(TimeUtil.MIN_WAIT_TIME, TimeUtil.MAX_WAIT_TIME));
            eaten++;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void code() {
        try {
            Thread.sleep(TimeUtil.randomTime(TimeUtil.MIN_WAIT_TIME, TimeUtil.MAX_WAIT_TIME));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
