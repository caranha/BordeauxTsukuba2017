package com.tskbdx.sumimasen.scenes.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.TiledMapUtils;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Spawn;

import java.io.Serializable;
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
public class World extends Observable implements Serializable {

    private boolean wallsMap[][];

    private Map<String, Entity> entitiesByName = new HashMap<>();
    private Map<String, Spawn> spawnByName = new HashMap<>();

    private Scene scene;

    public World(Scene scene) {
        this.scene = scene;
    }

    public void init(TiledMap tiledMap, List<TiledMapUtils.MapObjectMapping> mappings, String playerSpawn ) {

        entitiesByName.clear();
        spawnByName.clear();

        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Collision");

        wallsMap = new boolean[collisionLayer.getWidth()][collisionLayer.getHeight()];

        for (int i = 0; i < collisionLayer.getWidth(); i++) {
            for (int j = 0; j < collisionLayer.getHeight(); j++) {
                if (collisionLayer.getCell(i, j) != null ) {
                    setWall(i, j);
                }
            }
        }

        MapLayer entities = tiledMap.getLayers().get("Entities");
        for (MapObject mapObject : entities.getObjects()) {
            if ("spawn".equals(mapObject.getProperties().get("type", String.class))) {

                Spawn spawn = new Spawn(
                        mapObject.getName(),
                        mapObject.getProperties().get("x", Float.class).intValue(),
                        mapObject.getProperties().get("y", Float.class).intValue());

                spawnByName.put(spawn.getName(), spawn);

                if (playerSpawn.equals(spawn.getName())) {

                    System.out.println("Found player spawn : " + playerSpawn);

                    GameScreen.getPlayer().setX(spawn.getX() / 8);
                    GameScreen.getPlayer().setY(spawn.getY() / 8);

                    GameScreen.getPlayer().setWorld(this);
                    entitiesByName.put(GameScreen.getPlayer().getName(), GameScreen.getPlayer());
                }
            }
        }

        for (TiledMapUtils.MapObjectMapping mapping : mappings) {
            if(mapping.name != null && !mapping.name.equals(GameScreen.getPlayer().getName()))
                createEntity(mapping);
        }

    }

    private void createEntity(TiledMapUtils.MapObjectMapping mapping) {

        Entity entity = new Entity();

        entity.setName(mapping.name);

        entity.setX(mapping.x);
        entity.setY(mapping.y);
        entity.setWidth(mapping.width);
        entity.setHeight(mapping.height);

        entity.setOnCollide(mapping.onCollide);
        entity.setInteraction(mapping.defaultInteraction);

        entity.setWorld(this);
        entitiesByName.put(mapping.name, entity);
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

    public Scene getScene() {
        return scene;
    }
}
