package com.tskbdx.sumimasen.scenes.model.entities.movements;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

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

            /*
             * Menage the case where the entity tried to move
             * but hasn't succeeded
             * In that cas, the direction has actually changed
             */
            entity.notifyObservers();

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

            if (!entity.getWorld().isCollisionOnBox(entity, newX, newY, entity.getWidth(),
                    entity.getHeight())) {
                entity.moveTo(newX, newY);
                entity.notifyObservers();
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
