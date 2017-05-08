package com.tskbdx.sumimasen.scenes.model.entities.movements;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sydpy on 5/2/17.
 */

public class BasicMovement implements Movement, Runnable {

    private boolean ready = true;
    private ScheduledExecutorService executorService
            = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void move(Entity entity, float dt) {
        int speed = entity.getSpeed();

        if (ready) {
            int newX = entity.getX(), newY = entity.getY();

            switch (entity.getDirection()) {
                case RIGHT:
                    ++newX;
                    break;
                case LEFT:
                    --newX;
                    break;
                case UP:
                    --newY;
                    break;
                case DOWN:
                    ++newY;
                    break;
                case NONE:
                    return;
            }

            if (!entity.getWorld().isWallOnBox(newX, newY, entity.getWidth(),
                    entity.getHeight())) {
                entity.setX(newX);
                entity.setY(newY);
                entity.notifyObservers();
                ready = false;
                executorService.schedule(this,
                        (long) ((1.f / speed) * 1000), TimeUnit.MILLISECONDS);
            }
        }
    }

    @Override
    public void run() {
        ready = true;
    }
}
