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
            entity.setX(entity.getX() + 1);
        }

    }

    private void moveVertical(Entity entity, float dt) {

    }
}
