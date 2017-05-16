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
import com.tskbdx.sumimasen.scenes.model.entities.Sensor;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.GetPickedUp;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Teleport;
import com.tskbdx.sumimasen.scenes.story.StoryTeller;
import com.tskbdx.sumimasen.scenes.story.introduction.StartState;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.CollisionSound;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;
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

            String type = object.getProperties().get("type", String.class);

            System.out.println(name);

            //TODO : Refacto this
            if (type != null && type.equals("sensor")) {

                Sensor entity = new Sensor(x,y,width,height);
                EntityRenderer entityRenderer;

                entity.setName(object.getName());

                String onCollision = object.getProperties().get("onCollision", String.class);

                if (onCollision.equals("teleport")) {

                    Integer toX = object.getProperties().get("toX", Integer.class);
                    Integer toY = object.getProperties().get("toY", Integer.class);

                    entity.setOnCollision(new Teleport(toX, toY));
                }

                entityRenderer = new EntityRenderer(entity, imagefile, Sumimasen.getAssetManager());
                world.addEntity(entity);
                worldRenderer.addEntityRenderer(entityRenderer);

            } else if (entityNames.contains(name)) {

                Entity entity;
                EntityRenderer entityRenderer;

                if (name.equals("player")) {
                    entity = getPlayer();
                } else {
                    entity = new SceneObject(x, y, width, height);
                }

                entity.setName(object.getName());

                // set interaction from property "defaultInteraction"
                Interaction interaction = null;
                String defaultInteraction = object.getProperties().get("defaultInteraction", String.class);

                if (defaultInteraction != null
                        && defaultInteraction.equals("dialogue")) {

                    interaction = new Dialogue(object.getProperties().get("dialogueName", String.class)); // check constructor for filename

                } else if (defaultInteraction != null
                        && defaultInteraction.equals("getPickedUp")) {
                    interaction = new GetPickedUp();
                }
                entity.setInteraction(interaction);

                entity.addObserver(new CollisionSound("collision.mp3"));
                entityRenderer = new EntityRenderer(entity, imagefile, Sumimasen.getAssetManager());

                if (name.equals("player")) {
                    entityRenderer.setAnimator(
                            new DirectionSpriteSheetAnimator(entityRenderer, 1, 2,12,16, 0.5f ));
                }

                world.addEntity(entity);
                worldRenderer.addEntityRenderer(entityRenderer);
            }

            object.setVisible(false);
        }

        getPlayer().addObserver(new StoryTeller(world, new StartState()));
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
