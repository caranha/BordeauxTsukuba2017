package com.tskbdx.sumimasen.scenes.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.TiledMapUtils;
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
public class World extends Observable {

    private boolean wallsMap[][];

    private Map<String, Entity> entitiesByName = new HashMap<>();

    public World() { }

    public void init(TiledMap tiledMap, List<TiledMapUtils.MapObjectMapping> mappings ) {

        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Collision");

        wallsMap = new boolean[collisionLayer.getWidth()][collisionLayer.getHeight()];

        for (int i = 0; i < collisionLayer.getWidth(); i++) {
            for (int j = 0; j < collisionLayer.getHeight(); j++) {
                if (collisionLayer.getCell(i, j) != null ) {
                    setWall(i, j);
                }
            }
        }

        for (TiledMapUtils.MapObjectMapping mapping : mappings) {
            createEntity(mapping);
        }

    }

    public Entity createEntity(TiledMapUtils.MapObjectMapping mapping) {

        Entity entity;

        if ("player".equals(mapping.name)) {
            entity = GameScreen.getPlayer();
        } else {
            entity = new Entity();
        }
        entity.setName(mapping.name);

        entity.setX(mapping.x);
        entity.setY(mapping.y);
        entity.setWidth(mapping.width);
        entity.setHeight(mapping.height);

        entity.setOnCollide(mapping.onCollide);
        entity.setInteraction(mapping.defaultInteraction);

        entity.setWorld(this);
        entitiesByName.put(mapping.name, entity);

        return entity;
    }

    public void removeEntity(Entity entity) {
        entity.setWorld(null);
        entitiesByName.remove(entity.getName());
        setChanged();
        notifyObservers(entity);
    }

    private void setVoid(int i, int j) {
        try {
            wallsMap[i][j] = false;
        } catch (ArrayIndexOutOfBoundsException ignored){}
    }

    public void setWall(int x, int y) {
        try {
            wallsMap[x][y] = true;
        } catch (ArrayIndexOutOfBoundsException ignored){}
    }

    public boolean isWall(Rectangle rectangle) {
        for(int i = (int) rectangle.x; i < rectangle.x + rectangle.width; i++) {
            for(int j = (int) rectangle.y; j < rectangle.y + rectangle.height; j++) {
                if (wallsMap[i][j]) return true;
            }
        }

        return false;
    }

    //TODO : rethink this method
    public Entity getEntity(int x, int y) {
        for (Entity entity : entitiesByName.values()) {
            if (entity != null) {
                for (int i = entity.getX(); i < entity.getX() + entity.getWidth(); i++) {
                    for (int j = entity.getY(); j < entity.getY() + entity.getHeight(); j++) {
                        if (i == x && j == y) {
                            return entity;
                        }
                    }
                }
            }
        }

        return null;
    }

    public List<Entity> getEntities(Rectangle rectangle) {

        List<Entity> colliding = new ArrayList<>();

        for (Entity entity : entitiesByName.values()) {
            if (entity != null &&
                    rectangle.overlaps(entity.getRectangle(new Rectangle()))) {
                colliding.add(entity);
            }
        }

        return colliding;
    }

    public final Entity getEntitiesByName(String name) {

        return entitiesByName.get(name);
    }

    public List<Entity> getEntities() {
        return new ArrayList<>(entitiesByName.values());
    }
}
