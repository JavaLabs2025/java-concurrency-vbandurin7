package org.labs;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProgrammerTest {

    @Test
    void testEarlyInterruption() throws InterruptedException {
        int progsCount = 7;
        int waitersCount = 2;
        int portions = 10000;

        SpoonManager spoonManager = new SpoonManager(progsCount);
        Programmer[] programmers = new Programmer[progsCount];
        WaiterService waiterService = new WaiterService(waitersCount, portions);
        for (int i = 0; i < progsCount; i++) {
            programmers[i] = new Programmer(i, spoonManager, waiterService);
            programmers[i].start();
        }

        Thread.sleep(1000);

        int totalEaten = 0;
        for (Programmer programmer : programmers) {
            programmer.interrupt();
            programmer.join();
            totalEaten += programmer.getEaten();
        }

        waiterService.shutdown();

        assertTrue(totalEaten < portions);
    }
}
