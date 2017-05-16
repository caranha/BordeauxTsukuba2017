package com.tskbdx.sumimasen.scenes.utility;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

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
     * Using ExecutorService to submit Runnable
     * (Thread can't set a new one after creation)
     *
     * ScheduledExecutorService used to submit with a delay
     */

    /*
     * List of existing created services
     */
    private static List<ScheduledExecutorService> executorServices = new LinkedList<>();

    public static void setTimeout(Runnable callback, int delay) {
        /*
         * For each service, we check if it's available
         * if so : schedules another task
         */
        for (ScheduledExecutorService service : executorServices) {
            if (service.isTerminated()) {
                service.schedule(callback, delay, TimeUnit.MILLISECONDS);
                return;
            }
        }

        /*
         * Else create a new one
         */
        final boolean multiThreaded = false; // no big changes since it's fast calculations
        ScheduledExecutorService service = multiThreaded ?
                Executors.newSingleThreadScheduledExecutor() :
                Executors.newScheduledThreadPool(4);
        service.schedule(callback, delay, TimeUnit.MILLISECONDS);
        executorServices.add(service);
    }

    public static void setTimeout(Runnable callback, float delay) {
        setTimeout(callback, (int) (delay * 1000));
    }
}
