package com.tskbdx.sumimasen.scenes.model.entities;

import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;

import java.util.Observable;

/**
 * Created by Sydpy on 4/28/17.
 */
public abstract class Entity extends Observable {

    private World world;

    private Movement movement;

    private int x, y;
    private int width, height;

    private Direction.Horizontal hDirection;
    private Direction.Vertical vDirection;

    //Number of cell per sec
    private int speed = 4;

    public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.hDirection = Direction.Horizontal.NONE;
        this.vDirection = Direction.Vertical.NONE;
    }

    public void update(float dt) {
        if (movement != null) {
            movement.move(this, dt);
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        setChanged();
        notifyObservers();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        setChanged();
        notifyObservers();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        setChanged();
        notifyObservers();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        setChanged();
        notifyObservers();
    }

    public void setWorld(World world) {
        this.world = world;
        setChanged();
        notifyObservers();
    }

    public World getWorld() {
        return world;
    }

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
        setChanged();
        notifyObservers();
    }

    public Direction.Horizontal getHDirection() {
        return hDirection;
    }

    public void setHDirection(Direction.Horizontal hDirection) {
        this.hDirection = hDirection;
        setChanged();
        notifyObservers();
    }

    public Direction.Vertical getVDirection() {
        return vDirection;
    }

    public void setVDirection(Direction.Vertical vDirection) {
        this.vDirection = vDirection;
        setChanged();
        notifyObservers();
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
        setChanged();
        notifyObservers();
    }

}
