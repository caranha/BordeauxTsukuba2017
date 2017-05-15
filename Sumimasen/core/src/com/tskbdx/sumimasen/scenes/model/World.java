package com.tskbdx.sumimasen.scenes.model;

import com.badlogic.gdx.math.Rectangle;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.*;

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

    private boolean wallsMap[][];

    private List<Entity> entities = new ArrayList<>();

    private Map<String, Entity> entityByName = new HashMap<>();

    public World(int width, int height) {
        wallsMap = new boolean[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                setVoid(i, j);
            }
        }
    }

    public void update(float dt) {
        for (Entity entity : entities) {
            entity.update(dt);
        }
    }

    public void addEntity(Entity entity) {
        entity.setWorld(this);

        entityByName.put(entity.getName(), entity);
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entity.setWorld(null);

        entityByName.remove(entity.getName());
       // entities.remove(entity);
    }

    public void setVoid(int i, int j) {
        try {
            wallsMap[i][j] = false;
        } catch (ArrayIndexOutOfBoundsException ignored){}
    }

    public void setWall(int x, int y) {
        try {
            wallsMap[x][y] = true;
        } catch (ArrayIndexOutOfBoundsException ignored){}
    }

    public boolean isWall(int x, int y) {
        try {
            return wallsMap[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    public boolean isWall(Rectangle rectangle) {
        for(int i = (int) rectangle.x; i < rectangle.x + rectangle.width; i++) {
            for(int j = (int) rectangle.y; j < rectangle.y + rectangle.height; j++) {
                if (wallsMap[i][j]) return true;
            }
        }

        return false;
    }

    public Entity getEntity(int x, int y) {
        for (Entity entity : entities) {
            for (int i = entity.getX(); i < entity.getX() + entity.getWidth(); i++) {
                for (int j = entity.getY(); j < entity.getY() + entity.getHeight(); j++) {
                    if (i == x && j == y) {
                        return entity;
                    }
                }
            }
        }

        return null;
    }

    public List<Entity> getEntity(Rectangle rectangle) {

        List<Entity> colliding = new ArrayList<>();

        for (Entity entity : entities) {
            if (rectangle.overlaps(entity.getRectangle(new Rectangle()))) {
                colliding.add(entity);
            }
        }

        return colliding;
    }

    public final Entity getEntityByName(String name) {
        if (entityByName.containsKey(name)) {
            return entityByName.get(name);
        }
        return null;
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
