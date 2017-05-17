package com.tskbdx.sumimasen.scenes.model.entities.movements;

import com.badlogic.gdx.math.Rectangle;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
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
    public MovementResult move(Entity entity) {
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

        return MovementResult.computeMovementResult(entity);
    }
}