package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.*;
import com.tskbdx.sumimasen.scenes.view.entities.SpritesheetUtils;
import com.tskbdx.sumimasen.scenes.view.entities.animator.Animator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sydpy on 5/24/17.
 */
public class TiledMapUtils {

    public static final int TILE_SIZE = 8;

    public static class MapObjectMapping {

        public final String name;
        public final int x;
        public final int y;
        public final int width;
        public final int height;

        public final Interaction defaultInteraction;
        public final Interaction onCollide;

        public final Animator standingAnimator;
        public final Animator walkingAnimator;

        MapObjectMapping(MapObject mapObject) {

            name    = mapObject.getName();

            x       = (int) Math.ceil(mapObject.getProperties().get("x", Float.class) / TILE_SIZE);
            y       = (int) Math.ceil(mapObject.getProperties().get("y", Float.class) / TILE_SIZE);
            width   = (int) Math.ceil(mapObject.getProperties().get("width", Float.class) / TILE_SIZE);
            height  = (int) Math.ceil(mapObject.getProperties().get("height", Float.class) / TILE_SIZE);


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
                onCollide = new ChangeMap(toMap);
            } else {
                onCollide = null;
            }

            String imageFile           = mapObject.getProperties().get("imageFile", String.class);
            String walkingSpritesheet  = mapObject.getProperties().get("walkingSpritesheet", String.class);
            String standingSpritesheet = mapObject.getProperties().get("standingSpritesheet", String.class);

            if (standingSpritesheet != null && !standingSpritesheet.equals("")
                    && walkingSpritesheet != null && !walkingSpritesheet.equals("")) {

                standingAnimator    = SpritesheetUtils.getAnimatorFromSpritesheet(standingSpritesheet);
                walkingAnimator     = SpritesheetUtils.getAnimatorFromSpritesheet(walkingSpritesheet);

            } else if (standingSpritesheet != null && !standingSpritesheet.equals("")) {

                standingAnimator    = SpritesheetUtils.getAnimatorFromSpritesheet(standingSpritesheet);
                walkingAnimator     = SpritesheetUtils.getAnimatorFromSpritesheet(standingSpritesheet);

            } else if (walkingSpritesheet != null && !walkingSpritesheet.equals("")) {

                standingAnimator    = SpritesheetUtils.getAnimatorFromSpritesheet(walkingSpritesheet);
                walkingAnimator     = SpritesheetUtils.getAnimatorFromSpritesheet(walkingSpritesheet);

            } else if (imageFile != null && !imageFile.equals("")) {

                standingAnimator    = SpritesheetUtils.getAnimatorFromSpritesheet(imageFile);
                walkingAnimator     = SpritesheetUtils.getAnimatorFromSpritesheet(imageFile);

            } else {

                standingAnimator    = SpritesheetUtils.getAnimatorFromSpritesheet("entity.png");
                walkingAnimator     = SpritesheetUtils.getAnimatorFromSpritesheet("entity.png");
            }

        }
    }

    public static List<MapObjectMapping> mapObjectMappings(TiledMap tiledMap) {

        MapLayer entities = tiledMap.getLayers().get("Entities");

        List<MapObjectMapping> mappings = new ArrayList<>();

        MapObjects objects = entities.getObjects();

        for (MapObject object : objects) {
            mappings.add(new MapObjectMapping(object));
        }

        return mappings;
    }
}
