package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.ChangeMap;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sydpy on 5/24/17.
 */
public class TiledMapUtils {

    public static final int TILE_SIZE = 8;
    public static Map<String, EntityDescriptor> allDescriptorsByName = new HashMap<>();

    public static class EntityDescriptor implements Serializable {

        public String name;
        public int x;
        public int y;
        public int width;
        public int height;

        public String standingSpritesheet;
        public String walkingSpritesheet;

        EntityDescriptor() {}

        EntityDescriptor(MapObject mapObject) {

            name    = mapObject.getName();

            x       = mapObject.getProperties().get("x", Float.class).intValue() / TILE_SIZE;
            y       = mapObject.getProperties().get("y", Float.class).intValue() / TILE_SIZE;
            width   = mapObject.getProperties().get("width", Float.class).intValue() / TILE_SIZE;
            height  = mapObject.getProperties().get("height", Float.class).intValue() / TILE_SIZE;

            String imageFile           = mapObject.getProperties().get("imageFile", String.class);
            String walkingSpritesheet  = mapObject.getProperties().get("walkingSpritesheet", String.class);
            String standingSpritesheet = mapObject.getProperties().get("standingSpritesheet", String.class);

            if (standingSpritesheet != null && !standingSpritesheet.equals("")
                    && walkingSpritesheet != null && !walkingSpritesheet.equals("")) {

                this.standingSpritesheet = standingSpritesheet;
                this.walkingSpritesheet = walkingSpritesheet;

            } else if (standingSpritesheet != null && !standingSpritesheet.equals("")) {

                this.standingSpritesheet = standingSpritesheet;
                this.walkingSpritesheet = standingSpritesheet;

            } else if (walkingSpritesheet != null && !walkingSpritesheet.equals("")) {

                this.standingSpritesheet = walkingSpritesheet;
                this.walkingSpritesheet = walkingSpritesheet;

            } else if (imageFile != null && !imageFile.equals("")) {

                this.standingSpritesheet = imageFile;
                this.walkingSpritesheet = imageFile;

            } else {

                this.standingSpritesheet = "entity.png";
                this.walkingSpritesheet = "entity.png";
            }
        }
    }

    public static class SensorDescriptor {

        public String name;
        public int x;
        public int y;
        public int width;
        public int height;

        public Interaction onCollide;

        public String imageFile;

        public SensorDescriptor(MapObject mapObject) {
            name = mapObject.getName();

            x       = mapObject.getProperties().get("x", Float.class).intValue() / TILE_SIZE;
            y       = mapObject.getProperties().get("y", Float.class).intValue() / TILE_SIZE;
            width   = mapObject.getProperties().get("width", Float.class).intValue() / TILE_SIZE;
            height  = mapObject.getProperties().get("height", Float.class).intValue() / TILE_SIZE;

            imageFile           = mapObject.getProperties().get("imageFile", String.class);

            String onCollideName = mapObject.getProperties().get("onCollide", String.class);

            if ("changeMap".equals(onCollideName)) {

                String toMap    = mapObject.getProperties().get("toMap", String.class);
                String toSpawn  = mapObject.getProperties().get("toSpawn", String.class);

                onCollide = new ChangeMap(toMap, toSpawn);
            }
        }
    }

    public static List<SensorDescriptor> sensorDescriptors(TiledMap tiledMap) {

        List<SensorDescriptor> sensorDescriptors = new ArrayList<>();

        MapLayer sensors = tiledMap.getLayers().get("Sensors");

        for (MapObject mapObject : sensors.getObjects()) {
            sensorDescriptors.add(new SensorDescriptor(mapObject));
        }

        return sensorDescriptors;
    }

    public static List<EntityDescriptor> entityDescriptors(TiledMap tiledMap, boolean withPlayer) {

        MapLayer entities = tiledMap.getLayers().get("Entities");

        List<EntityDescriptor> mappings = new ArrayList<>();

        mappings.addAll(buildDescriptors(entities));

        if (withPlayer) {

            mappings.add(buildPlayerDescriptor());
        }

        return mappings;
    }

    private static EntityDescriptor buildPlayerDescriptor() {

        String playerName = GameScreen.getPlayer().getName();

        if (allDescriptorsByName.containsKey(playerName)) {
            return allDescriptorsByName.get(playerName);
        }

        EntityDescriptor playerMapping = new EntityDescriptor();
        playerMapping.name = playerName;
        playerMapping.walkingSpritesheet = "player_walking.png";
        playerMapping.standingSpritesheet = "player_standing.png";

        allDescriptorsByName.put(playerMapping.name, playerMapping);

        return playerMapping;
    }

    private static List<EntityDescriptor> buildDescriptors(MapLayer entities) {
        List<EntityDescriptor> mappings = new ArrayList<>();
        MapObjects objects = entities.getObjects();

        for (MapObject object : objects) {
            if ("entity".equals(object.getProperties().get("type", String.class))) {

                if (allDescriptorsByName.containsKey(object.getName())) {

                    EntityDescriptor entityDescriptor = allDescriptorsByName.get(object.getName());
                    entityDescriptor.x = object.getProperties().get("x", Float.class).intValue() / TILE_SIZE;
                    entityDescriptor.y = object.getProperties().get("y", Float.class).intValue() / TILE_SIZE;

                    mappings.add(entityDescriptor);

                } else {

                    EntityDescriptor entityDescriptor = new EntityDescriptor(object);
                    mappings.add(entityDescriptor);

                    if (!allDescriptorsByName.containsKey(entityDescriptor.name)) {
                        allDescriptorsByName.put(entityDescriptor.name, entityDescriptor);
                    }
                }
            }
        }

        return mappings;
    }
}
