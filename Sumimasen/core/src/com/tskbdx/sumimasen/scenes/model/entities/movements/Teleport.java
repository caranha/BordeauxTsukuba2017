package com.tskbdx.sumimasen.scenes.model.entities.movements;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

/**
 * Created by Sydpy on 5/17/17.
 */
public class Teleport implements Movement {

    private int x, y;

    public Teleport(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public MovementResult move(Entity entity) {

        entity.setX(x);
        entity.setY(y);
        entity.notifyObservers();

        return  MovementResult.computeMovementResult(entity);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
