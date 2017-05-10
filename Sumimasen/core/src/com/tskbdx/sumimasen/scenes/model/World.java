package com.tskbdx.sumimasen.scenes.model;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Sydpy on 4/27/17.
 */

/**
 * World represented in a matrix.
 * Collisionable :
 * - Walls stored as booleans
 * - Entities
 * Else :
 * - Empty location are filled by null value.
 */
public class World {

    private Object objects[][]; // For the moment

    private List<Entity> entities = new ArrayList<>();

    public World(int width, int height) {
        objects = new Object[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                setVoid(i, j);
            }
        }
    }

    public final Object get(int x, int y) {
        try {
            return objects[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public void update(float dt) {
        for (Entity entity : entities) {
            entity.update(dt);
        }
    }

    public void addEntity(Entity entity) {
        entity.setWorld(this);
        entities.add(entity);
        if (! (entity instanceof Player)) { // for the moment no collision management on player
            setEntityLocation(entity, entity);
        }
    }

    /**
     * Set a object at an entity location
     * (from x to x + w and y to y + h)
     * @param entity
     */
    public void setEntityLocation(Entity entity, Object object) {
        for (int i = entity.getX() ; i != entity.getX() + entity.getWidth() ; ++i) {
            for (int j = entity.getY(); j != entity.getY() + entity.getHeight(); ++j) {
                objects[i][j] = object;
            }
        }
    }

    public void removeEntity(Entity entity) {
        setEntityLocation(entity, null);
        entities.remove(entity);
        entity.setWorld(null);
    }

    public void setVoid(int i, int j) {
        objects[i][j] = null;
    }

    public void setWall(int x, int y) {
        objects[x][y] = Boolean.TRUE;
    }

    private boolean isWall(int x, int y) {
        try {
            return objects[x][y] instanceof Boolean;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private boolean isEntity(int x, int y) {
        try {
            return objects[x][y] instanceof Entity;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Since we want to move an entity in the world,
     * we are facing a problem :
     * if an 2x1 entity go left (or right), he won't be able
     * because his left location will be free, certainly,
     * but not for its right part.
     * So we'll ignore if the entity is colling with itself.
     */

    public boolean isCollisionOnBox(Entity entity, int x, int y, int width, int height) {
        for(int i = x; i < x + width; i++) {
            for(int j = y; j < y + height; j++) {
                if (isWall(i, j) ||
                        (isEntity(i, j) && ! entity.equals(objects[i][j])))
                    return true;
            }
        }
        return false;
    }
}
