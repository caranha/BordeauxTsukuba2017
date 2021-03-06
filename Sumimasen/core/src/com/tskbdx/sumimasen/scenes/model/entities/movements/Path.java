package com.tskbdx.sumimasen.scenes.model.entities.movements;

/**
 * Created by viet khang on 09/05/2017.
 */

import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.utility.Utility;

import java.util.*;

/**
 * Implements entity movement scripting
 * Automatically remove itself from the entity when done
 */
public class Path implements Movement {
    private final Queue<Direction> directionQueue = new LinkedList<>();
    private boolean ready = true;
    private final boolean loop;
    private Runnable onFinished;

    public Path(boolean loop, Direction... directions) {
        this.loop = loop;
        Collections.addAll(directionQueue, directions);
    }

    public Path(Runnable onFinished, Direction... directions) {
        loop = false;
        this.onFinished = onFinished;
        Collections.addAll(directionQueue, directions);
    }

    @Override
    public void move(Entity entity) {
        if (directionQueue.isEmpty()) {
            entity.setMovement(null);
            if (onFinished != null) {
                onFinished.run();
            }
        } else if (ready) {
            ready = false;
            /*
             * Menage the case where the entity tried to move
             * but hasn't succeeded
             * In that cas, the direction has actually changed
             */
            process(directionQueue.poll(), entity);
            Utility.setTimeout(() -> {
                ready = true;
                move(entity);
            }, 1.f / entity.getSpeed());
        }
    }

    private void process(Direction direction, Entity entity) {
        entity.setDirection(direction);
        if (loop) {
            directionQueue.add(direction);
        }
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

        List<Entity> entityColliding = entity.getWorld().getEntities(newX, newY, entity.getWidth(), entity.getHeight());
        entityColliding.remove(entity);

        if (!entity.getWorld().isWall(newX, newY, entity.getWidth(), entity.getHeight())
                &&  entityColliding.isEmpty()) {
            entity.moveTo(newX, newY);
            entity.notifyObservers();
        }
    }
}
