package org.labs;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        startDinner(7, 2, 100000);
    }

    public static void startDinner(int progsCount, int waitersCount, int portions) throws InterruptedException {
        SpoonManager spoonManager = new SpoonManager(progsCount);
        Programmer[] programmers = new Programmer[progsCount];
        WaiterService waiterService = new WaiterService(waitersCount, portions);
        for (int i = 0; i < progsCount; i++) {
            programmers[i] = new Programmer(i, spoonManager, waiterService);
            programmers[i].start();
        }

        int total = 0;
        for (int i = 0; i < progsCount; i++) {
            programmers[i].join();
            total += programmers[i].getEaten();
        }

        waiterService.shutdown();
        System.out.println("TOTAL EATEN " + total);
    }
}

