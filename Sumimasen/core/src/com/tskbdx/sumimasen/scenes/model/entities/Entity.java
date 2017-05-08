package com.tskbdx.sumimasen.scenes.model.entities;

import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;

import java.util.Observable;

import static com.tskbdx.sumimasen.scenes.model.entities.movements.Direction.*;

/**
 * Created by Sydpy on 4/28/17.
 */
public abstract class Entity extends Observable {

    private World world;

    private Movement movement;

    private int x, y;
    private int width, height;

    private Direction direction;

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
            movement.move(this, dt);
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
        this.x = x;
        this.y = y;
        setChanged();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
      //  if (world != null) world.setEntityLocation(this, null);
        this.y = y;
     //   if (world != null) world.setEntityLocation(this, this);
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

    public void setMovement(Movement movement) {
        this.movement = movement;
        setChanged();
    }

    public void setDirection(Direction direction) {
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

    @Override
    public int hashCode() {
        int result = world != null ? world.hashCode() : 0;
        result = 31 * result + (movement != null ? movement.hashCode() : 0);
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + width;
        result = 31 * result + height;
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + speed;
        return result;
    }
}
