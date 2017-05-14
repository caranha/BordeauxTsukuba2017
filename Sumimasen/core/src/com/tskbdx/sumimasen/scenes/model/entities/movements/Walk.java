package com.tskbdx.sumimasen.scenes.model.entities.movements;

import com.badlogic.gdx.math.Rectangle;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.List;

/**
 * Created by Sydpy on 5/2/17.
 */

/**
 * Walk
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
                    &&  entityColliding.isEmpty()) {
                this.entity.moveTo(newX, newY);
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
