package com.tskbdx.sumimasen.scenes.model.entities.movements;

import com.badlogic.gdx.Gdx;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sydpy on 5/2/17.
 */

/**
 * More precise with a scheduled executor than
 * incrementing a clock with elapsed time
 * (no more lag on old computers)
 * (IMO it's ok for other platforms)
 */
public class Walk extends Movement {

    public Walk(Entity entity) {
        super(entity);
    }

    private boolean canMove = true;
    private float clock = 0.f;

    @Override
    public void move(float dt) {

        int speed = entity.getSpeed();

        if (canMove) {
            int newX = entity.getX(), newY = entity.getY();

            switch (entity.getDirection()) {
                case EAST:
                    ++newX;
                    break;
                case WEST:
                    --newX;
                    break;
                case NORTH: /// Gdx landmark seems to be from bottom to top
                    ++newY; //--newY;
                    break;
                case SOUTH:
                    --newY; //++newY;
                    break;
                case NONE:
                    return;
            }

            canMove = false;

            if (!entity.getWorld().isCollisionOnBox(newX, newY, entity.getWidth(),
                    entity.getHeight())) {
                entity.setXY(newX, newY);
                entity.notifyObservers();
            }
        } else {
            clock += dt;
            if(clock >= 1.f/speed) {
                canMove = true;
                clock %= 1.f/speed;
            }
        }
    }
}
