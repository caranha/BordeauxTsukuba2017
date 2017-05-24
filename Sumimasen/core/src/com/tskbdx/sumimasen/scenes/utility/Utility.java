package com.tskbdx.sumimasen.scenes.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;

/*
 * Created by viet khang on 16/05/2017.
 */
abstract public class Utility {

    /*
     * Key = existing created services
     * Value = current working task associated to key
     */
    private static Map<ScheduledExecutorService, ScheduledFuture>
            executorServices = new HashMap<>();
    private static Map<String, Class> nativeTypes = new HashMap<>();

    static {
        nativeTypes.put("int", Integer.TYPE);
        nativeTypes.put("long", Long.TYPE);
        nativeTypes.put("double", Double.TYPE);
        nativeTypes.put("float", Float.TYPE);
        nativeTypes.put("bool", Boolean.TYPE);
        nativeTypes.put("char", Character.TYPE);
        nativeTypes.put("byte", Byte.TYPE);
        nativeTypes.put("void", Void.TYPE);
        nativeTypes.put("short", Short.TYPE);
    }

    public static Class getPrimitiveType(String name) {
        if (!nativeTypes.containsKey(name)) {
            throw new IllegalStateException("invalid native name");
        }
        return nativeTypes.get(name);
    }

    public static Object interpret(String string) {
        Scanner scanner = new Scanner(string);
        Object next;

        if (scanner.hasNext())
            next = scanner.nextInt();
        else if (scanner.hasNextDouble())
            next = scanner.nextDouble();
        else if (scanner.hasNext())
            next = scanner.next();
        else
            next = string;

        scanner.close();
        return next;
    }

    /*
     * Random method are upper bound exclusive
     * In Java 1.7 or later, the standard way to do this is as follows :
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
     * Purpose : callback after a DELAY without blocking
     * Using ExecutorService to submit Runnable
     * (Thread can't set a new one after creation)
     *
     * ScheduledExecutorService used to submit with a DELAY
     */

    public static void setTimeout(Runnable callback, int delay) {
        /*
         * For each service, we check if it's available
         * if so : schedules another task
         */
        for (Map.Entry<ScheduledExecutorService, ScheduledFuture>
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
    }

    public static void setTimeout(Runnable callback, float delay) {
        setTimeout(callback, Math.round(delay * 1000));
    }
}
