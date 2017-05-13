package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.tskbdx.sumimasen.Sumimasen;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.SceneObject;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.GetPickedUp;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.CollisionSound;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.InventoryRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.MessageRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.animator.DirectionSpriteSheetAnimator;

import java.util.List;

import static com.tskbdx.sumimasen.GameScreen.getPlayer;

/**
 * Created by Sydpy on 5/12/17.
 */
public class MapLoader {

    public static void loadEntities(TiledMap tiledMap,
                                    World world,
                                    WorldRenderer worldRenderer,
                                    List<String> entityNames) {

        MapLayer entitiesLayer = tiledMap.getLayers().get("Entities");
        MapObjects objects = entitiesLayer.getObjects();

        for (MapObject object : objects) {

            int x = (int) Math.ceil(object.getProperties().get("x", Float.class) / 8);
            int y = (int) Math.ceil(object.getProperties().get("y", Float.class) / 8);
            int width = (int) Math.ceil(object.getProperties().get("width", Float.class) / 8);
            int height = (int) Math.ceil(object.getProperties().get("height", Float.class) / 8);
            String imagefile = object.getProperties().get("imagefile", String.class);
            String name = object.getName();

            Entity entity;
            EntityRenderer entityRenderer;
            MessageRenderer messageRenderer;

            if (entityNames.contains(name)) {

                if (name.equals("player")) {
                    entity = getPlayer();
                } else {
                    entity = new SceneObject(x, y, width, height);
                }

                entity.setX(x);
                entity.setY(y);
                entity.setWidth(width);
                entity.setHeight(height);
                entity.setName(object.getName());

                // set interaction from property "firstInteraction"
                Interaction interaction = null;
                String firstInteraction = object.getProperties().get("firstInteraction", String.class);
                if (firstInteraction != null && firstInteraction.equals("dialogue")) {
                    interaction = new Dialogue(entity, getPlayer(),
                            object.getProperties().get("dialogueName", String.class)); // check constructor for filename
                } else if (firstInteraction != null && firstInteraction.equals("getPickedUp")) {
                    interaction = new GetPickedUp(entity, getPlayer());
                }
                entity.setInteraction(interaction);

                entity.addObserver(new CollisionSound("collision.mp3"));
                entityRenderer = new EntityRenderer(entity, imagefile, Sumimasen.getAssetManager());
                messageRenderer = new MessageRenderer(entity.getMessage());

                if (name.equals("player")) {
                    entityRenderer.setAnimator(
                            new DirectionSpriteSheetAnimator(entityRenderer, 1, 2,12,16, 0.5f ));
                }

                world.addEntity(entity);
                worldRenderer.addEntityRenderer(entityRenderer);
                worldRenderer.addMessageRenderer(messageRenderer);
            }

            object.setVisible(false);
        }

        InventoryRenderer inventoryRenderer = new InventoryRenderer();
        getPlayer().addObserver(inventoryRenderer);
        worldRenderer.setInventoryRenderer(inventoryRenderer);
    }

    public static void loadWalls(TiledMap tiledMap, World world, WorldRenderer worldRenderer) {

        TiledMapTileLayer collisionLayer =
                (TiledMapTileLayer) tiledMap.getLayers().get("Collision");

        for(int i = 0; i < collisionLayer.getWidth(); i++) {
            for (int j = 0; j < collisionLayer.getHeight(); j++) {
                if (collisionLayer.getCell(i,j) != null) {
                    world.setWall(i,j);
                }
            }
        }

        collisionLayer.setVisible(false);
    }

}
