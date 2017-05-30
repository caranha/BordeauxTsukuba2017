package com.tskbdx.sumimasen.scenes.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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
    private Entity entitiesMap[][];

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
        entitiesMap = new Entity[collisionLayer.getWidth()][collisionLayer.getHeight()];

        for (int i = 0; i < collisionLayer.getWidth(); i++) {
            for (int j = 0; j < collisionLayer.getHeight(); j++) {
                if (collisionLayer.getCell(i, j) != null ) {
                    setWall(i, j);
                } else {
                    setVoid(i, j);
                }

                entitiesMap[i][j] = null;
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

                    GameScreen.getPlayer().moveTo(spawn.getX() / 8, spawn.getY() / 8);

                    GameScreen.getPlayer().setWorld(this);
                    moveEntity(GameScreen.getPlayer(), GameScreen.getPlayer().getX(), GameScreen.getPlayer().getY());
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

        entity.moveTo(mapping.x, mapping.y);
        entity.setWidth(mapping.width);
        entity.setHeight(mapping.height);

        entity.setOnCollide(mapping.onCollide);
        entity.setInteraction(mapping.defaultInteraction);

        entity.setWorld(this);
        moveEntity(entity, entity.getX(), entity.getY());
        entitiesByName.put(mapping.name, entity);
    }

    public void removeEntity(Entity entity) {
        setNone(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight());
        entity.setWorld(null);
        entitiesByName.remove(entity.getName());
        setChanged();
        notifyObservers(entity);
    }

    private void setNone(int x, int y, int width, int height) {

        for (int i = x; i < Math.min(x + width, entitiesMap.length); i++) {
            for (int j = y; j < Math.min(y + height, entitiesMap[0].length); j++) {
                entitiesMap[i][j] = null;
            }
        }
    }

    public void moveEntity(Entity entity, int prevX, int prevY) {

        setNone(prevX, prevY, entity.getWidth(), entity.getHeight());

        for (int i = entity.getX(); i < entity.getX() + entity.getWidth(); i++) {
            for (int j = entity.getY(); j < entity.getY() + entity.getHeight(); j++) {
                entitiesMap[i][j] = entity;
            }
        }
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

    public boolean isWall(int x, int y, int width, int height) {
        for(int i = x; i < x + width; i++) {
            for(int j = y; j < y + height; j++) {
                if (wallsMap[i][j]) return true;
            }
        }

        return false;
    }

    public Entity getEntity(int x, int y) {

        return entitiesMap[x][y];
    }

    public List<Entity> getEntities(int x, int y, int width, int height) {

        List<Entity> colliding = new ArrayList<>();

        for(int i = x; i < x + width; i++) {
            for(int j = y; j < y + height; j++) {

                Entity entity = entitiesMap[i][j];

                if(entity != null && !colliding.contains(entity)) {
                    colliding.add(entity);
                }
            }
        }

        return colliding;
    }

    public List<Entity> getEntitiesAround(Entity entity) {

        List<Entity> entities = new ArrayList<>();

        for (int i = entity.getX(); i < entity.getX() + entity.getWidth(); i++) {
            Entity e;
            e = getEntity(i, entity.getY() - 1);
            if (e != null && !entities.contains(e)) entities.add(e);
            e = getEntity(i, entity.getY() + entity.getHeight());
            if (e != null && !entities.contains(e)) entities.add(e);
        }

        for (int i = entity.getY(); i < entity.getY() + entity.getHeight(); i++) {
            Entity e;
            e = getEntity(entity.getX() - 1, i);
            if (e != null && !entities.contains(e)) entities.add(e);
            e = getEntity(entity.getX() + entity.getWidth(), i);
            if (e != null && !entities.contains(e)) entities.add(e);
        }
        return entities;
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
