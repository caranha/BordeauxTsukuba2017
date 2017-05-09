package com.tskbdx.sumimasen.scenes.model.entities.movements;

/**
 * Created by viet khang on 09/05/2017.
 */

import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Implements entity movement scripting
 * Automatically remove itself from the entity when done
 */
public class Path extends Movement {
    private final Queue<Direction> directionQueue = new LinkedList<>();
    private float clock = 0.f;
    private boolean ready = true;

    public Path(Entity entity, Direction... directions) {
        super(entity);
        Collections.addAll(directionQueue, directions);
    }

    @Override
    public void move(float dt) {
        if (directionQueue.isEmpty()) {
            entity.setMovement(null);
        } else if (ready) {
            ready = false;
            process(directionQueue.poll());
        } else {
            updateClock(dt);
        }
    }

    private void process(Direction direction) {
        entity.setDirection(direction);
        int newX = entity.getX(), newY = entity.getY();
        switch (direction) {
            case EAST:
                ++newX;
                break;
            case WEST:
                --newX;
                break;
            case NORTH:
                ++newY; //--newY;
                break;
            case SOUTH:
                --newY; //++newY;
                break;
        }

        if (!entity.getWorld().isCollisionOnBox(newX, newY, entity.getWidth(),
                entity.getHeight())) {
            entity.setXY(newX, newY);
            entity.notifyObservers();
        }
    }

    private void updateClock(float dt) {
        clock += dt;
        if(clock >= 1.f / entity.getSpeed()) {
            ready = true;
            clock %= 1.f / entity.getSpeed();
        }
    }
}
