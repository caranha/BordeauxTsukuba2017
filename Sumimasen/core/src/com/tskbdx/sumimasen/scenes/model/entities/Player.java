package com.tskbdx.sumimasen.scenes.model.entities;

/**
 * Created by Sydpy on 4/27/17.
 */
public class Player extends Entity {
    private float speed = 96f;

    private boolean leftMove = false,
            rightMove = false,
            upMove = false,
            downMove = false;

    @Override
    public void update(float dt) {

        if(leftMove) {
            getRectangle().x -= speed*dt;
            setChanged();
            notifyObservers();
        }

        if(rightMove) {
            getRectangle().x += speed*dt;
            setChanged();
            notifyObservers();
        }

        if(upMove) {
            getRectangle().y += speed*dt;
            setChanged();
            notifyObservers();
        }

        if(downMove) {
            getRectangle().y -= speed*dt;
            setChanged();
            notifyObservers();
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isLeftMove() {
        return leftMove;
    }

    public void setLeftMove(boolean leftMove) {
        this.leftMove = leftMove;
    }

    public boolean isRightMove() {
        return rightMove;
    }

    public void setRightMove(boolean rightMove) {
        this.rightMove = rightMove;
    }

    public boolean isUpMove() {
        return upMove;
    }

    public void setUpMove(boolean upMove) {
        this.upMove = upMove;
    }

    public boolean isDownMove() {
        return downMove;
    }

    public void setDownMove(boolean downMove) {
        this.downMove = downMove;
    }
}
