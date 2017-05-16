package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

/**
 * Created by Sydpy on 5/16/17.
 */
public class Teleport extends Interaction {

    private int x, y;

    public Teleport(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    @Override
    public void start(Entity active, Entity passive) {
        super.start(active, passive);

        System.out.println("STEAAAAAAAAAAAAAK");

        passive.setX(x);
        passive.setY(y);
    }

    @Override
    public void end() {
        super.end();
    }
}