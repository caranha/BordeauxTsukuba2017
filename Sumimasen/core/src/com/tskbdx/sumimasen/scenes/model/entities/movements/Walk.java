package com.tskbdx.sumimasen.scenes.model.entities.movements;

import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.utility.Utility;

import java.util.List;

/*
 * Created by Sydpy on 5/2/17.
 */

/**
 * Walk
 */
public class Walk implements Movement {

    private boolean canMove = true;

    @Override
    public void move(Entity entity) {
        if (canMove && entity.getDirection() != Direction.NONE) {
            int newX = entity.getX(), newY = entity.getY();
            switch (entity.getDirection()) {
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

            canMove = false;

            List<Entity> entityColliding = entity.getWorld().getEntities(newX, newY, entity.getWidth(), entity.getHeight());
            entityColliding.remove(entity);

            if (!entity.getWorld().isWall(newX, newY, entity.getWidth(), entity.getHeight())
                    && entityColliding.isEmpty()) {
                entity.moveTo(newX, newY);
                entity.notifyObservers();
            }

            Utility.setTimeout(() -> {
                canMove = true;
                move(entity);
            }, 1.f / entity.getSpeed());
        }
    }
}