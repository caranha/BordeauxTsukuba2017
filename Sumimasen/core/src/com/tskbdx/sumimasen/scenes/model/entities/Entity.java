package com.tskbdx.sumimasen.scenes.model.entities;

import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;

import java.util.Observable;

import static com.tskbdx.sumimasen.scenes.model.entities.Direction.*;

/**
 * Created by Sydpy on 4/28/17.
 */
public abstract class Entity extends Observable {

    private World world;

    private Movement movement;

    private int x, y;
    private int width, height;

    /**
     * direction is the current direction movement state
     * lastDirection is like the static direction state
     */
    private Direction direction;
    private Direction lastDirection = SOUTH; // by default

    //Number of cell per sec
    private int speed = 4;

    public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.direction = NONE;
    }

    public void update(float dt) {
        if (movement != null) {
            movement.run();
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        setChanged();
    }

    public void setXY(int x, int y) {
        setX(x);
        setY(y);
        setChanged();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        setChanged();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        setChanged();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        setChanged();
    }

    public void setWorld(World world) {
        this.world = world;
        setChanged();
    }

    public World getWorld() {
        return world;
    }

    public Movement getMovement() {
        return movement;
    }

    void setMovement(Movement movement) {
        this.movement = movement;
        setChanged();
    }

    public void setDirection(Direction direction) {
        if (direction != NONE) {
            lastDirection = direction;
        }
        this.direction = direction;
        setChanged();
    }

    public final Direction getDirection() {
        return direction;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        setChanged();
    }

    /**
     * Can only interact if there is a SceneObject
     * in front of the entity
     */
    public void tryInteract() {
        int targetX = getX(), targetY = getY(); // target position = in front of the entity
        switch (getLastDirection()) {
            case NORTH:
                targetY += getHeight() + 1; // Gdx seems to be from bot to top
                break;
            case SOUTH:
                --targetY; // Gdx seems to be from bot to top
                break;
            case WEST:
                --targetX;
                break;
            case EAST:
                targetX += getWidth() + 1;
                break;
        }
        SceneObject interactive = (SceneObject) world.get(targetX, targetY);
        if (interactive != null) { // && interactive.isInteractable()) {
            System.out.println("Interaction !");// when it will be implemented -> interactive.interactWith(this);
        } else {
            System.out.println(targetX + " : " + targetY + " Nobody to interact with !");
        }
    }

    private Direction getLastDirection() {
        return lastDirection;
    }
}
