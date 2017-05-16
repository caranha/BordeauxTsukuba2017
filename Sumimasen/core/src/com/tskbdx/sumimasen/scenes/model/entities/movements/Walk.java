package com.tskbdx.sumimasen.scenes.model.entities.movements;

import com.badlogic.gdx.math.Rectangle;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.List;

import static com.tskbdx.sumimasen.scenes.utility.Utility.setTimeout;

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
        if (canMove) {
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
                case NONE:
                    return;
            }

            canMove = false;

            Rectangle rect = entity.getRectangle(new Rectangle());
            rect.setPosition(newX, newY);

            List<Entity> entityColliding = entity.getWorld().getEntities(rect);
            entityColliding.remove(entity);

            if (!entity.getWorld().isWall(rect)
                    && entityColliding.isEmpty()) {
                entity.moveTo(newX, newY);
            }

            setTimeout(() -> {
                canMove = true;
                move(entity);
            }, 1.f / entity.getSpeed());
        }
    }
}