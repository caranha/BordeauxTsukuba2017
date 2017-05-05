package com.tskbdx.sumimasen.scenes.model;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.ArrayList;

/**
 * Created by Sydpy on 4/27/17.
 */
public class World {

    private boolean walls[][];

    private ArrayList<Entity> entities = new ArrayList<Entity>();

    public World(int width, int height) {

        walls = new boolean[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                walls[i][j] = false;
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
    }

    public void removeEntity(Entity entity) {
        entity.setWorld(null);
        entities.remove(entity);
    }

    public void setWall(int x, int y) {
        walls[x][y] = true;
    }

    public void setVoid(int x, int y) {
        walls[x][y] = false;
    }

    public boolean isWall(int x, int y) {
        return walls[x][y];
    }

    public boolean isWallOnBox(int x, int y, int width, int height) {

        for(int i = x; i < x + width; i++) {
            for(int j = y; j < y + height; j++) {
                if (isWall(i,j))
                    return true;
            }
        }

        return false;
    }

}
