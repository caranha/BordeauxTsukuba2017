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
    public static Map<String, MapObjectDescriptor> allDescriptorsByName = new HashMap<>();

    public static class MapObjectDescriptor implements Serializable {

        public String name;
        public int x;
        public int y;
        public int width;
        public int height;

        public Interaction onCollide;

        public String standingSpritesheet;
        public String walkingSpritesheet;

        MapObjectDescriptor() {}

        MapObjectDescriptor(MapObject mapObject) {

            name    = mapObject.getName();

            x       = mapObject.getProperties().get("x", Float.class).intValue() / TILE_SIZE;
            y       = mapObject.getProperties().get("y", Float.class).intValue() / TILE_SIZE;
            width   = mapObject.getProperties().get("width", Float.class).intValue() / TILE_SIZE;
            height  = mapObject.getProperties().get("height", Float.class).intValue() / TILE_SIZE;

            String onCollideName = mapObject.getProperties().get("onCollide", String.class);
            if ("changeMap".equals(onCollideName)) {

                String toMap = mapObject.getProperties().get("toMap", String.class);
                String toSpawn = mapObject.getProperties().get("toSpawn", String.class);
                onCollide = new ChangeMap(toMap, toSpawn);
            } else {
                onCollide = null;
            }

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

    public static List<MapObjectDescriptor> mapObjectMappings(TiledMap tiledMap, boolean withPlayer) {

        MapLayer entities = tiledMap.getLayers().get("Entities");

        List<MapObjectDescriptor> mappings = new ArrayList<>();

        mappings.addAll(buildDescriptors(entities));

        if (withPlayer) {

            mappings.add(buildPlayerDescriptor());
        }

        return mappings;
    }

    private static MapObjectDescriptor buildPlayerDescriptor() {

        String playerName = GameScreen.getPlayer().getName();

        if (allDescriptorsByName.containsKey(playerName)) {
            return allDescriptorsByName.get(playerName);
        }

        MapObjectDescriptor playerMapping = new MapObjectDescriptor();
        playerMapping.name = playerName;
        playerMapping.walkingSpritesheet = "player_walking.png";
        playerMapping.standingSpritesheet = "player_standing.png";

        allDescriptorsByName.put(playerMapping.name, playerMapping);

        return playerMapping;
    }

    private static List<MapObjectDescriptor> buildDescriptors(MapLayer entities) {
        List<MapObjectDescriptor> mappings = new ArrayList<>();
        MapObjects objects = entities.getObjects();

        for (MapObject object : objects) {
            if ("entity".equals(object.getProperties().get("type", String.class))) {

                if (allDescriptorsByName.containsKey(object.getName())) {

                    MapObjectDescriptor mapObjectDescriptor = allDescriptorsByName.get(object.getName());
                    mapObjectDescriptor.x = object.getProperties().get("x", Float.class).intValue() / TILE_SIZE;
                    mapObjectDescriptor.y = object.getProperties().get("y", Float.class).intValue() / TILE_SIZE;

                    mappings.add(mapObjectDescriptor);

                } else {

                    MapObjectDescriptor mapObjectDescriptor = new MapObjectDescriptor(object);
                    mappings.add(mapObjectDescriptor);

                    if (!allDescriptorsByName.containsKey(mapObjectDescriptor.name)) {
                        allDescriptorsByName.put(mapObjectDescriptor.name, mapObjectDescriptor);
                    }
                }
            }
        }

        return mappings;
    }
}
