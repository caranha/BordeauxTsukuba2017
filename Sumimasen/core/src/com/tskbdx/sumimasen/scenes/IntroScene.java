package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.tskbdx.sumimasen.scenes.inputprocessors.BasicInputProcessor;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Player;
import com.tskbdx.sumimasen.scenes.model.entities.SceneObject;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;

/**
 * Created by Sydpy on 4/27/17.
 */
public class IntroScene implements Scene {

    private static float SCALE_FACTOR = 4.0f;

    private OrthographicCamera camera = new OrthographicCamera();

    private World world;
    private WorldRenderer worldRenderer;

    private Player player;

    @Override
    public void init() {

        //Load tiled map
        TiledMap tiledMap = new TmxMapLoader().load("maps/map.tmx");
        world = new World();
        worldRenderer = new WorldRenderer(tiledMap);

        loadEntities(tiledMap);

        loadWalls(tiledMap);

        Gdx.input.setInputProcessor(new BasicInputProcessor(this.player));
        camera.setToOrtho(false, 800, 480);
        camera.zoom = 1.f/SCALE_FACTOR;
        camera.translate(-400, -240);

    }

    private void loadWalls(TiledMap tiledMap) {
        for (MapObject wall : tiledMap.getLayers().get("Walls").getObjects()) {
            if (wall instanceof PolygonMapObject) {
               world.addWall((PolygonMapObject) wall);
            }
        }
    }

    private void loadEntities(TiledMap tiledMap) {
        for (MapObject object : tiledMap.getLayers().get("Entities").getObjects()) {

            Entity entity;

            if (object.getName().equals("player")) {
                this.player = new Player();
                entity = this.player;
            } else {
                entity = new SceneObject();
            }

            EntityRenderer entityRenderer = new EntityRenderer(entity, (String) object.getProperties().get("imagefile"));

            entity.setRectangle(new Rectangle(
                    (Float) object.getProperties().get("x"),
                    (Float) object.getProperties().get("y"),
                    (Float) object.getProperties().get("width"),
                    (Float) object.getProperties().get("height")));

            world.addEntity(entity);
            worldRenderer.addEntityRenderer(entityRenderer);
        }
    }

    @Override
    public void update(float dt) {

        world.update(dt);

        camera.translate(player.getRectangle().x - camera.position.x, player.getRectangle().y - camera.position.y);
    }

    @Override
    public void render(Batch batch) {
        camera.update();

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
