package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.tskbdx.sumimasen.scenes.inputprocessors.BasicInputProcessor;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Player;
import com.tskbdx.sumimasen.scenes.view.SmoothCamera;
import com.tskbdx.sumimasen.scenes.view.Tween;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sydpy on 4/27/17.
 */
public class IntroScene implements Scene {

    private static float SCALE_FACTOR = 4.0f;
    private final Player player;

    public static SmoothCamera camera;

    private World world;
    private WorldRenderer worldRenderer;

    private List<String> entityNames = new ArrayList<>();

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

        entityNames.add("player");
        entityNames.add("Entity1");
        entityNames.add("item");

        MapLoader.loadEntities(tiledMap, world, worldRenderer, entityNames);
        MapLoader.loadWalls(tiledMap, world, worldRenderer);

        Gdx.input.setInputProcessor(new BasicInputProcessor());

        camera = new SmoothCamera(1.f);
        camera.setToOrtho(false, 800, 480);
        camera.zoom = 1.f / SCALE_FACTOR;
        camera.update();
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
