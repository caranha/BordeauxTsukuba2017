package com.tskbdx.sumimasen.scenes.utility;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private static Map<ScheduledExecutorService, ScheduledFuture<?>>
            executorServices = new HashMap<>();

    public static void setTimeout(Runnable callback, int delay) {
        /*
         * For each service, we check if it's available
         * if so : schedules another task
         */
        for (Map.Entry<ScheduledExecutorService, ScheduledFuture<?>>
                entry : executorServices.entrySet()) {
            if (entry.getValue().isDone()) {
                entry.setValue(entry.getKey().schedule(
                        callback, delay, TimeUnit.MILLISECONDS));
                return;
            }
        }

        /*
         * Else create a new one
         */
        // can create a service in another thread but there is no big change
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        executorServices.put(service,
                service.schedule(callback, delay, TimeUnit.MILLISECONDS));
        // Max services used : ~2
        // System.out.println(executorServices.size());
    }

    public static void setTimeout(Runnable callback, float delay) {
        setTimeout(callback, (int) (delay * 1000));
    }
}
