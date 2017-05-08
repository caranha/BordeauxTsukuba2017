package com.tskbdx.sumimasen.scenes.model.entities.movements;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

/**
 * Created by Sydpy on 5/2/17.
 */
public class BasicMovement implements Movement {

    private float horizontalClock = 0.f, verticalClock = 0.f;

    @Override
    public void move(Entity entity, float dt) {
        moveHorizontal(entity, dt);
        moveVertical(entity, dt);
    }

    private void moveHorizontal(Entity entity, float dt) {

        int speed = entity.getSpeed();
        Direction.Horizontal direction = entity.getHDirection();

        if (direction != Direction.Horizontal.NONE) {

            if (horizontalClock == 0.f) {

                int newX;

                if (direction == Direction.Horizontal.RIGHT) {
                    newX = entity.getX() + 1;
                } else {
                    newX = entity.getX() - 1;
                }

                if(!entity.getWorld().isWallOnBox(
                        newX,
                        entity.getY(),
                        entity.getWidth(),
                        entity.getHeight())) {
                    entity.setX(newX);
                    entity.notifyObservers();
                    entity.setVDirection(Direction.Vertical.NONE);
                }
            }

            horizontalClock += dt;
            if (horizontalClock > 1.f /speed) {
                horizontalClock = 0.f;
            }

        } else {
            horizontalClock = 0.f;
        }
    }

    private void moveVertical(Entity entity, float dt) {

        int speed = entity.getSpeed();
        Direction.Vertical direction = entity.getVDirection();

        if (direction != Direction.Vertical.NONE) {

            if (verticalClock == 0.f) {

                int newY;

                if (direction == Direction.Vertical.UP) {
                    newY = entity.getY() + 1;
                } else {
                    newY = entity.getY() - 1;
                }

                if(!entity.getWorld().isWallOnBox(
                        entity.getX(),
                        newY,
                        entity.getWidth(),
                        entity.getHeight())) {
                    entity.setY(newY);
                    entity.notifyObservers();
                    entity.setHDirection(Direction.Horizontal.NONE);
                }
            }

            verticalClock += dt;
            if (verticalClock > 1.f /speed) {
                verticalClock = 0.f;
            }

        } else {
            verticalClock = 0.f;
        }
    }
}
