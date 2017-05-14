package com.tskbdx.sumimasen.scenes.model.entities.movements;

/**
 * Created by viet khang on 09/05/2017.
 */

import com.badlogic.gdx.math.Rectangle;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Implements entity movement scripting
 * Automatically remove itself from the entity when done
 */
public class Path extends Movement {
    private final Queue<Direction> directionQueue = new LinkedList<>();
    private float clock = 0.f;
    private boolean ready = true;
    private final boolean loop;

    public Path(Entity entity, boolean loop, Direction... directions) {
        super(entity);
        this.loop = loop;
        Collections.addAll(directionQueue, directions);
    }

    @Override
    public void move(float dt) {
        if (directionQueue.isEmpty()) {
            entity.setMovement(null);
        } else if (ready) {
            ready = false;
            /*
             * Menage the case where the entity tried to move
             * but hasn't succeeded
             * In that cas, the direction has actually changed
             */
            entity.notifyObservers();
            process(directionQueue.poll());
        } else {
            updateClock(dt);
        }
    }

    private void process(Direction direction) {
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
            case NONE:
                ready = false;
                break;
        }

        Rectangle rect = entity.getRectangle(new Rectangle());
        rect.setPosition(newX, newY);

        List<Entity> entityColliding = entity.getWorld().getEntities(rect);
        entityColliding.remove(entity);

        if (!entity.getWorld().isWall(rect)
                &&  entityColliding.isEmpty()) {
            entity.moveTo(newX, newY);
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
