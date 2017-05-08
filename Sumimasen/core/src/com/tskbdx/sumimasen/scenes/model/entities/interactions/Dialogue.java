package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

/**
 * Created by viet khang on 08/05/2017.
 */
public class Dialogue extends Interaction {

    Dialogue(Entity active, Entity passive) {
        super(active, passive);
    }

    @Override
    public void run() {
        System.out.println("Interaction !");
    }
}
