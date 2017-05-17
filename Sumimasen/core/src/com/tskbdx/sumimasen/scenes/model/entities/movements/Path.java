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

import static com.tskbdx.sumimasen.scenes.utility.Utility.setTimeout;

/**
 * Implements entity movement scripting
 * Automatically remove itself from the entity when done
 */
public class Path implements Movement {
    private final Queue<Direction> directionQueue = new LinkedList<>();
    private boolean ready = true;
    private final boolean loop;

    public Path(boolean loop, Direction... directions) {
        this.loop = loop;
        Collections.addAll(directionQueue, directions);
    }

    @Override
    public MovementResult move(Entity entity) {
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
            process(directionQueue.poll(), entity);
        } else {
            setTimeout(() -> {
                ready = true;
                move(entity);
            }, 1.f / entity.getSpeed());
        }

        return MovementResult.computeMovementResult(entity);
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
}
