package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.tskbdx.sumimasen.scenes.inputprocessors.BasicInputProcessor;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Player;
import com.tskbdx.sumimasen.scenes.view.SmoothCamera;
import com.tskbdx.sumimasen.scenes.view.Tween;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;
import com.tskbdx.sumimasen.scenes.view.ui.UserInterface;

import java.util.ArrayList;
import java.util.List;

import static com.tskbdx.sumimasen.GameScreen.getPlayer;

/*
 * Created by Sydpy on 4/27/17.
 */
public class IntroScene implements Scene {

    private final static float SCALE_FACTOR = 4.0f;
    private final Player player;

    public static SmoothCamera camera;

    private World world;
    private WorldRenderer worldRenderer;

    private UserInterface userInterface;

    private List<String> entityNames = new ArrayList<>();

    private InputProcessor basicProcessor = new BasicInputProcessor();

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
        entityNames.add("entity");
        entityNames.add("item");

        MapLoader.loadEntities(tiledMap, world, worldRenderer, entityNames);
        MapLoader.loadWalls(tiledMap, world, worldRenderer);

        userInterface = new UserInterface(world, getPlayer());
        setInputProcessor(basicProcessor);

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

        userInterface.act(dt);

        setInputProcessor(player.isInteracting() ? userInterface : basicProcessor);
    }

    private void setInputProcessor(InputProcessor inputProcessor) {
        if (Gdx.input.getInputProcessor() != inputProcessor) {
            // we want to compare references
            Gdx.input.setInputProcessor(inputProcessor);
        }
    }

    @Override
    public void render(Batch batch) {

        batch.setProjectionMatrix(camera.combined);

        worldRenderer.setView(camera);
        worldRenderer.render();

        userInterface.draw();
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
        userInterface.dispose();
    }

    @Override
    public void resize(int width, int height) {
        //userInterface.getViewport().setScreenSize(width, height);
        //userInterface.getViewport().update(width, height, true);
        // to do
    }
}
