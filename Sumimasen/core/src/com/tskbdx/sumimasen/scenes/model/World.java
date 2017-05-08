package com.tskbdx.sumimasen.scenes.model;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.ArrayList;

/**
 * Created by Sydpy on 4/27/17.
 */
public class World {

    private Object objects[][]; // For the moment

    private ArrayList<Entity> entities = new ArrayList<>();

    public World(int width, int height) {

        objects = new Object[width][height];

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
        entities.add(entity);
        for (int i = entity.getX() ; i != entity.getX() + entity.getWidth() ; ++i) {
            for (int j = entity.getY() ; j != entity.getY() + entity.getHeight() ; ++j) {
                objects[i][j] = entity;
            }
        }
    }

    public void removeEntity(Entity entity) {
        entity.removeFromWorld();
        entities.remove(entity);
    }

    public void setVoid(int i, int j) {
        objects[i][j] = null;
    }

    public void setWall(int x, int y) {
        objects[x][y] = Boolean.TRUE;
    }


    private boolean isWall(int x, int y) {
        return objects[x][y] instanceof Boolean;
    }

    private boolean isEntity(int x, int y) {
        return objects[x][y] instanceof Entity;
    }

    public boolean isCollisionOnBox(int x, int y, int width, int height) {

        for(int i = x; i < x + width; i++) {
            for(int j = y; j < y + height; j++) {
                if (isWall(i, j) || isEntity(i, j))
                    return true;
            }
        }

        return false;
    }
}
