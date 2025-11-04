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
        this.hasPortion = false;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            code();

            hasPortion = requestFoodWithTimeout(eaten);
            if (!hasPortion) {
                break;
            }

            spoonManager.aquireSpoons(id);
            try {
                eat();
            } finally {
                spoonManager.releaseSpoons(id);
            }
        }
        System.out.println("Programmer-" + id + " has eaten " + eaten + " portions");
    }

    public int getEaten() {
        return eaten;
    }

    private boolean requestFoodWithTimeout(int eaten) {
        try {
            CompletableFuture<Boolean> getFoodFuture = waiterService.requestFood(eaten);
            return getFoodFuture.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (ExecutionException e) {
            System.err.println("Waiter failed to bring new portion: " + e.getCause());
            return false;
        } catch (TimeoutException e) {
            System.err.println("No response from waiter");
            return false;
        }
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
