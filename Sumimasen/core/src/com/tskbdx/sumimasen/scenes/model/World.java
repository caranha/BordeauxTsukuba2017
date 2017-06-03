package com.tskbdx.sumimasen.scenes.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.TiledMapUtils;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Sensor;
import com.tskbdx.sumimasen.scenes.model.entities.Spawn;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Sydpy on 4/27/17.
 */

/**
 * World represented in a matrix.
 * Object which collides :
 * - Walls stored as booleans
 * - Entities
 * - Sensors
 * Else :
 * - Empty locations are filled by null value.
 */
public class World extends Observable implements Serializable {

    private boolean wallsMap[][];
    private Entity entitiesMap[][];
    private Sensor sensorsMap[][];

    private Map<String, Entity> entitiesByName = new HashMap<>();
    private Map<String, Sensor> sensorsByName = new HashMap<>();

    private List<String> entitiesInCurrentMap = new ArrayList<>();

    private int width, height;

    public World() {
    }

    public void load(TiledMap tiledMap,
                     List<TiledMapUtils.EntityDescriptor> entityDescriptors,
                     List<TiledMapUtils.SensorDescriptor> sensorDescriptors,
                     String playerSpawn) {

        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Collision");

        width = collisionLayer.getWidth();
        height = collisionLayer.getHeight();

        wallsMap = new boolean[collisionLayer.getWidth()][collisionLayer.getHeight()];
        entitiesMap = new Entity[collisionLayer.getWidth()][collisionLayer.getHeight()];
        sensorsMap = new Sensor[collisionLayer.getWidth()][collisionLayer.getHeight()];
        entitiesInCurrentMap.clear();

        buildWalls(collisionLayer);
        spawnPlayer(tiledMap, playerSpawn);
        spawnEntities(entityDescriptors);

        buildSensors(sensorDescriptors);
    }

    public void load(TiledMap tiledMap,
                     List<TiledMapUtils.EntityDescriptor> entityDescriptors,
                     List<TiledMapUtils.SensorDescriptor> sensorDescriptors,
                     int playerX, int playerY) {

        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Collision");

        width   = collisionLayer.getWidth();
        height  = collisionLayer.getHeight();

        wallsMap    = new boolean[collisionLayer.getWidth()][collisionLayer.getHeight()];
        entitiesMap = new Entity[collisionLayer.getWidth()][collisionLayer.getHeight()];
        sensorsMap  = new Sensor[collisionLayer.getWidth()][collisionLayer.getHeight()];
        entitiesInCurrentMap.clear();

        buildWalls(collisionLayer);
        spawnPlayer(playerX, playerY);
        spawnEntities(entityDescriptors);

        buildSensors(sensorDescriptors);

    }

    private void buildSensors(List<TiledMapUtils.SensorDescriptor> sensorDescriptors) {
        for (TiledMapUtils.SensorDescriptor sensorDescriptor : sensorDescriptors) {

            Sensor sensor = new Sensor();

            sensor.setName(sensorDescriptor.name);
            sensor.setX(sensorDescriptor.x);
            sensor.setY(sensorDescriptor.y);
            sensor.setWidth(sensorDescriptor.width);
            sensor.setHeight(sensorDescriptor.height);

            sensor.setOnCollide(sensorDescriptor.onCollide);

            sensorsByName.put(sensor.getName(), sensor);

            for (int i = 0; i < sensor.getWidth(); i++) {
                for (int j = 0; j < sensor.getHeight(); j++) {
                    sensorsMap[sensor.getX() + i][sensor.getY() + j] = sensor;
                }
            }
        }
    }

    private void spawnEntities(List<TiledMapUtils.EntityDescriptor> entityDescriptors) {
        for (TiledMapUtils.EntityDescriptor entityDescriptor : entityDescriptors) {

            if (entityDescriptor.name != null && !entityDescriptor.name.equals(GameScreen.getPlayer().getName())) {

                Entity entity = createEntity(entityDescriptor);
                moveEntity(entity, entity.getX(), entity.getY());

                entitiesByName.put(entityDescriptor.name, entity);
                entitiesInCurrentMap.add(entityDescriptor.name);

                setChanged();
                notifyObservers();

            }
        }
    }

    private void spawnPlayer(TiledMap tiledMap, String playerSpawn) {
        MapLayer entities = tiledMap.getLayers().get("Entities");
        for (MapObject mapObject : entities.getObjects()) {
            if ("spawn".equals(mapObject.getProperties().get("type", String.class))) {

                /*
                 * This fix change map bug
                 * Before it took the first object of type "spawn"
                 * and broke the loop.
                 * The functional bug was going to the lab and going out.
                 * (spawn.getName() was "player_home" while spawnName was "lab_left"
                 */
                String spawnName = mapObject.getName();

                if (spawnName.equals(playerSpawn)) {

                    Spawn spawn = new Spawn(
                            mapObject.getName(),
                            mapObject.getProperties().get("x", Float.class).intValue(),
                            mapObject.getProperties().get("y", Float.class).intValue());

                    GameScreen.getPlayer().moveTo(spawn.getX() / TiledMapUtils.TILE_SIZE, spawn.getY() / TiledMapUtils.TILE_SIZE);
                    moveEntity(GameScreen.getPlayer(), GameScreen.getPlayer().getX(), GameScreen.getPlayer().getY());
                    GameScreen.getPlayer().setWorld(this);

                    entitiesByName.put(GameScreen.getPlayer().getName(), GameScreen.getPlayer());
                    entitiesInCurrentMap.add(GameScreen.getPlayer().getName());

                    break;
                }
            }
        }
    }

    private void spawnPlayer(int playerX, int playerY) {
        GameScreen.getPlayer().moveTo(playerX, playerY);
        moveEntity(GameScreen.getPlayer(), GameScreen.getPlayer().getX(), GameScreen.getPlayer().getY());
        GameScreen.getPlayer().setWorld(this);

        entitiesByName.put(GameScreen.getPlayer().getName(), GameScreen.getPlayer());
        entitiesInCurrentMap.add(GameScreen.getPlayer().getName());

    }



    private void buildWalls(TiledMapTileLayer collisionLayer) {
        for (int i = 0; i < collisionLayer.getWidth(); i++) {
            for (int j = 0; j < collisionLayer.getHeight(); j++) {
                if (collisionLayer.getCell(i, j) != null) {
                    setWall(i, j);
                } else {
                    setVoid(i, j);
                }

                entitiesMap[i][j] = null;
            }
        }
    }

    private Entity createEntity(TiledMapUtils.EntityDescriptor mapping) {

        Entity entity = entitiesByName.get(mapping.name);

        if (entity == null) {
            entity = new Entity();

            entity.setName(mapping.name);

            entity.setWidth(mapping.width);
            entity.setHeight(mapping.height);
        }

        entity.setWorld(this);
        entity.moveTo(mapping.x, mapping.y);

        return entity;

    }

    public void removeEntity(Entity entity) {
        setNone(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight());
        entity.setWorld(null);

        entitiesByName.remove(entity.getName());
        entitiesInCurrentMap.remove(entity.getName());

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

        for (int i = entity.getX(); i < Math.min(width - 1, entity.getX() + entity.getWidth()); i++) {
            for (int j = entity.getY(); j < Math.min(height - 1, entity.getY() + entity.getHeight()); j++) {
                entitiesMap[i][j] = entity;
            }
        }

        List<Sensor> sensors = getSensors(entity.getX(), entity.getY(), entity.getWidth(), entity.getHeight());
        if (!sensors.isEmpty()) {
            Sensor sensor = sensors.get(0);

            sensor.getOnCollide().start(sensor, entity); ////////
        }

        setChanged();
        notifyObservers();
    }

    private void setVoid(int i, int j) {
        try {
            wallsMap[i][j] = false;
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    private void setWall(int x, int y) {
        try {
            wallsMap[x][y] = true;
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    public boolean isWall(int x, int y, int width, int height) {
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                try {
                    if (wallsMap[i][j]) return true;
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    return true;
                }
            }
        }

        return false;
    }

    public Entity getEntity(int x, int y) {
        try {
            return entitiesMap[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public Sensor getSensor(int x, int y) {
        try {
            return sensorsMap[x][y];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public List<Entity> getEntities(int x, int y, int width, int height) {

        List<Entity> colliding = new ArrayList<>();

        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {

                try {

                    Entity entity = entitiesMap[i][j];

                    if (entity != null && !colliding.contains(entity)) {
                        colliding.add(entity);
                    }

                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }

        return colliding;
    }

    public List<Sensor> getSensors(int x, int y, int width, int height) {

        List<Sensor> colliding = new ArrayList<>();

        for (int i = x; i < Math.min(this.width - 1, x + width); i++) {
            for (int j = y; j < y + Math.min(this.height - 1, height); j++) {

                Sensor sensor = sensorsMap[i][j];

                if (sensor != null && !colliding.contains(sensor)) {
                    colliding.add(sensor);
                }
            }
        }

        return colliding;
    }

    public final Entity getEntityByName(String name) {

        return entitiesByName.get(name);
    }

    public final boolean isEntityInCurrentMap(String entityName) {
        return entitiesInCurrentMap.contains(entityName);
    }

    public final boolean isEntityInCurrentMap(Entity entity) {
        return entitiesInCurrentMap.contains(entity.getName());
    }

    public void addEntity(String name, Entity entity) {
        entitiesByName.put(name, entity);
        entity.setName(name);
        setChanged();
    }

    public List<Entity> getEntities() {
        return new ArrayList<>(entitiesByName.values());
    }

    public List<Entity> getEntitiesInCurrentMap() {

        List<Entity> entities = new ArrayList<>();
        for (Map.Entry<String, Entity> entry : entitiesByName.entrySet()) {
            if (isEntityInCurrentMap(entry.getKey())) {
                entities.add(entry.getValue());
            }
        }

        return entities;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Sensor getSensorByName(String name) {
        return sensorsByName.get(name);
    }

    public void addSensor(Sensor sensor) {

        sensorsByName.put(sensor.getName(), sensor);

        for (int i = 0; i < sensor.getWidth(); i++) {
            for (int j = 0; j < sensor.getHeight(); j++) {
                sensorsMap[sensor.getX() + i][sensor.getY() + j] = sensor;
            }
        }
    }

    public void removeSensor(Sensor sensor) {
        sensorsByName.remove(sensor.getName());

        for (int i = 0; i < sensor.getWidth(); i++) {
            for (int j = 0; j < sensor.getHeight(); j++) {
                sensorsMap[sensor.getX() + i][sensor.getY() + j] = null;
            }
        }
    }
}