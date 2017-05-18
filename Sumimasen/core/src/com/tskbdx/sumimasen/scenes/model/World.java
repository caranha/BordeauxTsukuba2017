package com.tskbdx.sumimasen.scenes.model;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.Pair;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.*;
import com.tskbdx.sumimasen.scenes.story.StoryTeller;
import com.tskbdx.sumimasen.scenes.story.introduction.StartState;

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

    private List<Entity> entities = new ArrayList<>();

    public World() { }

    public void init(TiledMap tiledMap) {

        int mapWidth = tiledMap.getProperties().get("width", Integer.class);
        int mapHeight = tiledMap.getProperties().get("height", Integer.class);

        wallsMap = new boolean[mapWidth][mapHeight];

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                setVoid(i, j);
            }
        }

        entities.clear();

        setChanged();
        notifyObservers(tiledMap);

        MapLayer entitiesLayer = tiledMap.getLayers().get("Entities");
        MapObjects objects = entitiesLayer.getObjects();

        for (MapObject object : objects) {

            String name = object.getName();

            int x = (int) Math.ceil(object.getProperties().get("x", Float.class) / 8);
            int y = (int) Math.ceil(object.getProperties().get("y", Float.class) / 8);
            int width = (int) Math.ceil(object.getProperties().get("width", Float.class) / 8);
            int height = (int) Math.ceil(object.getProperties().get("height", Float.class) / 8);
            String imagefile = object.getProperties().get("imagefile", String.class);

            Entity entity;

            if (name.equals("player")) {
                entity = GameScreen.getPlayer();
            } else {
                entity = new Entity();
            }

            entity.setX(x);
            entity.setY(y);
            entity.setWidth(width);
            entity.setHeight(height);

            entity.setName(object.getName());

            // set interaction from property "defaultInteraction"
            String defaultInteraction = object.getProperties().get("defaultInteraction", String.class);

            if (defaultInteraction != null) {

                Interaction interaction = null;
                if (defaultInteraction.equals("dialogue")) {

                    interaction = new Dialogue(object.getProperties().get("dialogueName", String.class)); // check constructor for filename

                } else if (defaultInteraction.equals("getPickedUp")) {

                    interaction = new GetPickedUp();

                }

                entity.setInteraction(interaction);
            }

            String onCollideInteraction = object.getProperties().get("onCollide", String.class);

            if (onCollideInteraction != null) {

                if (onCollideInteraction.equals("teleport")) {
                    Integer toX = object.getProperties().get("toX", Integer.class);
                    Integer toY = object.getProperties().get("toY", Integer.class);

                    entity.setOnCollide(new Teleport(toX, toY));
                } else if (onCollideInteraction.equals("changeMap")) {
                    String toMap = object.getProperties().get("toMap", String.class);

                    entity.setOnCollide(new ChangeMap(toMap));
                }

            }

            addEntity(entity);
            notifyObservers(new Pair<>(entity, imagefile));

            object.setVisible(false);
        }

        GameScreen.getPlayer().addObserver(new StoryTeller(this, new StartState()));

        TiledMapTileLayer collisionLayer =
                (TiledMapTileLayer) tiledMap.getLayers().get("Collision");

        for(int i = 0; i < collisionLayer.getWidth(); i++) {
            for (int j = 0; j < collisionLayer.getHeight(); j++) {
                if (collisionLayer.getCell(i,j) != null) {
                    setWall(i,j);
                }
            }
        }

        collisionLayer.setVisible(false);
    }

    public void addEntity(Entity entity) {
        entity.setWorld(this);
        entities.add(entity);
        setChanged();
    }

    public void removeEntity(Entity entity) {
        entity.setWorld(null);
        entities.remove(entity);
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

    //TODO : rethink this method
    public Entity getEntity(int x, int y) {
        for (Entity entity : entities) {
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

        for (Entity entity : entities) {
            if (entity != null &&
                    rectangle.overlaps(entity.getRectangle(new Rectangle()))) {
                colliding.add(entity);
            }
        }

        return colliding;
    }

    public final List<Entity> getEntitiesByName(String name) {

        List<Entity> entitiesByName = new ArrayList<>(entities);
        entitiesByName.removeIf( e -> !e.getName().equals(name));

        return entitiesByName;
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
