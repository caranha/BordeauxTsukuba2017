package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sydpy on 5/24/17.
 */
public class TiledMapUtils {

    public static final int TILE_SIZE = 8;

    public static class MapObjectMapping implements Serializable {

        public String name;
        public int x;
        public int y;
        public int width;
        public int height;

        public Interaction defaultInteraction;
        public Interaction onCollide;

        public String standingSpritesheet;
        String walkingSpritesheet;

        MapObjectMapping() {}

        MapObjectMapping(MapObject mapObject) {

            name    = mapObject.getName();

            x       = (int) Math.floor(mapObject.getProperties().get("x", Float.class) / TILE_SIZE);
            y       = (int) Math.floor(mapObject.getProperties().get("y", Float.class) / TILE_SIZE);
            width   = (int) Math.floor(mapObject.getProperties().get("width", Float.class) / TILE_SIZE);
            height  = (int) Math.floor(mapObject.getProperties().get("height", Float.class) / TILE_SIZE);


            String defaultInteractionName  = mapObject.getProperties().get("defaultInteraction", String.class);
            if ("dialogue".equals(defaultInteractionName)) {
                String dialogueName        = mapObject.getProperties().get("dialogueName", String.class);
                defaultInteraction = new Dialogue(dialogueName);
            } else if ("getPickedUp".equals(defaultInteractionName)) {
                defaultInteraction = new GetPickedUp();
            } else {
                defaultInteraction = null;
            }

            String onCollideName = mapObject.getProperties().get("onCollide", String.class);
            if ("teleport".equals(onCollideName)) {

                int toX = mapObject.getProperties().get("toX", Integer.class);
                int toY = mapObject.getProperties().get("toY", Integer.class);

                onCollide = new Teleport(toX, toY);
            } else if ("changeMap".equals(onCollideName)) {

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

    public static List<MapObjectMapping> mapObjectMappings(TiledMap tiledMap) {

        MapLayer entities = tiledMap.getLayers().get("Entities");

        List<MapObjectMapping> mappings = new ArrayList<>();

        MapObjects objects = entities.getObjects();

        for (MapObject object : objects) {
            if("entity".equals(object.getProperties().get("type", String.class))) {
                mappings.add(new MapObjectMapping(object));
            }
        }

        MapObjectMapping playerMapping = new MapObjectMapping();
        playerMapping.name = GameScreen.getPlayer().getName();
        playerMapping.walkingSpritesheet = "player_walking.png";
        playerMapping.standingSpritesheet = "player_standing.png";

        mappings.add(playerMapping);

        return mappings;
    }
}
