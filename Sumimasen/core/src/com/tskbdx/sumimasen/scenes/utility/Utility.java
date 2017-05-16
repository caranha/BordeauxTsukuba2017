package com.tskbdx.sumimasen.scenes.utility;

import java.util.concurrent.ThreadLocalRandom;

/*
 * Created by viet khang on 16/05/2017.
 */
abstract public class Utility {

    /*
     * Random method are upper bound exclusive
     */
    public static int getRandomInteger(int lower, int upper) {
        assert upper > lower;
        return ThreadLocalRandom.current().nextInt(lower, upper);
    }

    public static float getRandomFloat(float lower, float upper) {
        assert upper > lower;
        return ThreadLocalRandom.current().nextFloat() * (upper - lower) + lower;
    }

    public static boolean getRandomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    /*
     * Purpose : callback after a delay without blocking
     */
    public static void setTimeout(Runnable callback, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                callback.run();
            } catch (Exception ignored) {
            }
        }).start();
    }

    public static void setTimeout(Runnable callback, float delay) {
        setTimeout(callback, (int) (delay * 1000));
    }
}
