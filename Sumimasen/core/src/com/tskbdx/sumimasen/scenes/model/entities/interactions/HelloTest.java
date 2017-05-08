package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

/**
 * Created by viet khang on 08/05/2017.
 */
public class HelloTest extends Interaction {
    public HelloTest(Entity producer, Entity consumer) {
        super(producer, consumer);
    }

    @Override
    public void run() {
        System.out.println("- Hello I'm " + active.getName());
        System.out.println("- I'm " + passive.getName());
    }
}
