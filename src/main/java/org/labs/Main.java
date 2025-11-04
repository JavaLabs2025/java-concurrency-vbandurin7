package org.labs;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        startDinner(30, 20, 10000);
        System.out.println(Runtime.getRuntime().availableProcessors());
    }

    public static void startDinner(int progsCount, int waitersCount, int portions) throws InterruptedException {
        SpoonManager spoonManager = new SpoonManager(progsCount);
        Programmer[] programmers = new Programmer[progsCount];
        WaiterService waiterService = new WaiterService(waitersCount, portions);
        for (int i = 0; i < progsCount; i++) {
            programmers[i] = new Programmer(i, spoonManager, waiterService);
        }
        for (int i = 0; i < progsCount; i++) {
            programmers[i].start();
        }

        for (int i = 0; i < progsCount; i++) {
            programmers[i].join();
        }

        waiterService.shutdown();
    }
}

