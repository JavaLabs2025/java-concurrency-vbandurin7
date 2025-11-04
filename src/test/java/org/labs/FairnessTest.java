package org.labs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FairnessTest {

    @Test
    void testFairPortionDistribution() throws InterruptedException {
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

        for (int i = 0; i < progsCount; i++) {
            programmers[i].join(20000);
        }

        double possibleDeviation = 0.1;
        double average = portions / (double) progsCount;
        int totalEaten = 0;
        for (Programmer programmer : programmers) {
            assertTrue(Math.abs(average - programmer.getEaten()) / average <= possibleDeviation);
            totalEaten += programmer.getEaten();
        }
        waiterService.shutdown();

        assertEquals(portions, totalEaten);
    }
}
