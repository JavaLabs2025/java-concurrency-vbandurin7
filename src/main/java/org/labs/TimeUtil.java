package org.labs;

public class TimeUtil {

    public static Integer MIN_WAIT_TIME = 1;
    public static Integer MAX_WAIT_TIME = 3;

    public static long randomTime(int min, int max) {
        return min + (long)(Math.random() * (max - min));
    }

    private TimeUtil() {
        throw new UnsupportedOperationException();
    }
}
