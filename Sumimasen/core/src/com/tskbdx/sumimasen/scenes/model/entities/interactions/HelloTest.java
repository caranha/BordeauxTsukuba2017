package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

/**
 * Created by viet khang on 08/05/2017.
 */
public class HelloTest extends Interaction {
    public HelloTest(Entity active, Entity passive) {
        super(active, passive);
    }

    @Override
    public void run() {
        System.out.println("message");
        active.setMessage("Hello " + passive.getName(), 3.f, passive);
        active.notifyObservers();
    }
}
