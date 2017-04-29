package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.tskbdx.sumimasen.scenes.inputprocessors.BasicInputProcessor;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;

import java.util.ArrayList;

/**
 * Created by Sydpy on 4/27/17.
 */
public class IntroScene implements Scene {

    private OrthographicCamera camera = new OrthographicCamera();

    private WorldRenderer worldRenderer;

    @Override
    public void init() {
        Gdx.input.setInputProcessor(new BasicInputProcessor(this));
        camera.setToOrtho(false, 800, 480);

        TiledMap tiledMap = new TmxMapLoader().load("maps/map.tmx");
        worldRenderer = new WorldRenderer(tiledMap, new ArrayList<EntityRenderer>());

    }

    @Override
    public void update(float dt) {

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
