package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.tskbdx.sumimasen.scenes.inputprocessors.BasicInputProcessor;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Player;
import com.tskbdx.sumimasen.scenes.model.entities.SceneObject;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.view.SmoothCamera;
import com.tskbdx.sumimasen.scenes.view.Tween;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.AnimatedEntityRendered;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;

/**
 * Created by Sydpy on 4/27/17.
 */
public class IntroScene implements Scene {

    private static float SCALE_FACTOR = 4.0f;
    private final Player player;

    private SmoothCamera camera;

    private World world;
    private WorldRenderer worldRenderer;

    public IntroScene(Player player) {
        this.player = player;
    }

    @Override
    public void init() {

        //Load tiled map
        TiledMap tiledMap = new TmxMapLoader().load("maps/map.tmx");
        int width = tiledMap.getProperties().get("width", Integer.class);
        int height = tiledMap.getProperties().get("height", Integer.class);
        world = new World(width, height);
        worldRenderer = new WorldRenderer(tiledMap);

        loadEntities(tiledMap);

        loadWalls(tiledMap);

        Gdx.input.setInputProcessor(new BasicInputProcessor());
        camera = new SmoothCamera(1.f);
        camera.setToOrtho(false, 800, 480);
        camera.zoom = 1.f/SCALE_FACTOR;
        camera.position.x -= 400;
        camera.position.y -= 240;
        camera.update();
    }

    private void loadWalls(TiledMap tiledMap) {

        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Collision");

        for(int i = 0; i < collisionLayer.getWidth(); i++) {
            for (int j = 0; j < collisionLayer.getHeight(); j++) {
                if (collisionLayer.getCell(i,j) != null) {
                    world.setWall(i,j);
                }
            }
        }

        tiledMap.getLayers().remove(collisionLayer);
    }

    private void loadEntities(TiledMap tiledMap) {

        MapObjects objects = tiledMap.getLayers().get("Entities").getObjects();

        for (MapObject object : objects) {

            int x       = (int) Math.ceil(object.getProperties().get("x" , Float.class) / 8);
            int y       = (int) Math.ceil(object.getProperties().get("y" , Float.class) / 8);
            int width   = (int) Math.ceil(object.getProperties().get("width" , Float.class) / 8);
            int height  = (int) Math.ceil(object.getProperties().get("height" , Float.class) / 8);
            String imagefile = object.getProperties().get("imagefile", String.class);

            Entity entity;
            EntityRenderer entityRenderer;

            if (object.getName().equals("player")) {
                player.setX(x);
                player.setY(y);
                player.setWidth(width);
                player.setHeight(height);
                entity = player;
                entityRenderer = new AnimatedEntityRendered(entity, imagefile, 3, 4);
            } else {
                SceneObject sceneObject = new SceneObject(x, y, width, height);
                sceneObject.setInteraction(new Dialogue(sceneObject, player, "dialogues/test.xml"));
                entity = sceneObject;
                entityRenderer = new EntityRenderer(entity, imagefile);
            }
            entity.setName(object.getName());
            world.addEntity(entity);
            worldRenderer.addEntityRenderer(entityRenderer);
        }


    }

    @Override
    public void update(float dt) {
        Tween.updateAll(dt);

        world.update(dt);
        camera.translate(player.getX()*8 - camera.position.x, player.getY()*8 - camera.position.y);
        camera.update();
    }

    @Override
    public void render(Batch batch) {

        batch.setProjectionMatrix(camera.combined);

        worldRenderer.setView(camera);
        worldRenderer.render();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Scene getNextScene() {
        return null;
    }

    @Override
    public void dispose() {

    }
}
