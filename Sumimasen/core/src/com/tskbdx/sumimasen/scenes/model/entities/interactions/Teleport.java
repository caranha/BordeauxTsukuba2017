package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;
import com.tskbdx.sumimasen.scenes.utility.Utility;

/**
 * Created by Sydpy on 5/16/17.
 */
public class Teleport extends Interaction {


    public static final float DELAY = 2.f;

    private final int x, y;

    public Teleport(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    @Override
    public void start(Entity active, Entity passive) {
        super.start(active, passive);

        Movement backup = passive.getMovement();
        passive.setMovement(null);
        passive.setDirection(Direction.NONE);
        passive.notifyObservers(Teleport.class);

        Utility.setTimeout(() -> {
            passive.moveTo(x, y);
            passive.setMovement(backup);
            passive.notifyObservers();
            end();
        } , DELAY);
    }

    @Override
    public void end() {
        super.end();
    }
}