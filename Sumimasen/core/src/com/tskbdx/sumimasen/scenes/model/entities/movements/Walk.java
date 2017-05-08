package com.tskbdx.sumimasen.scenes.model.entities.movements;

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

    private boolean ready = true;
    private ScheduledExecutorService executorService
            = Executors.newSingleThreadScheduledExecutor();
    private Runnable resetClock = () -> ready = true;

    @Override
    public void run() {
        if (ready) {
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

            if (!entity.getWorld().isCollisionOnBox(newX, newY, entity.getWidth(),
                    entity.getHeight())) {
                entity.setXY(newX, newY);
                entity.notifyObservers();
                ready = false;
                executorService.schedule(resetClock,
                        (long) ((1.f / entity.getSpeed()) * 1000), TimeUnit.MILLISECONDS);
            }
        }
    }
}
