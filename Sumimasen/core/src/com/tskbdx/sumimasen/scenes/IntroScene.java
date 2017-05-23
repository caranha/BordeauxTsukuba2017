package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.tskbdx.sumimasen.scenes.inputprocessors.GameCommands;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Player;
import com.tskbdx.sumimasen.scenes.story.Story;
import com.tskbdx.sumimasen.scenes.view.SmoothCamera;
import com.tskbdx.sumimasen.scenes.view.Tween;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;
import com.tskbdx.sumimasen.scenes.view.ui.UserInterface;

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

    private InputProcessor basicProcessor = new GameCommands();

    public IntroScene(Player player) {
        this.player = player;
    }

    @Override
    public void init() {

        camera = new SmoothCamera(1.f);
        camera.setToOrtho(false, 800, 480);
        camera.zoom = 1.f / SCALE_FACTOR;

        //Load tiled map
        TiledMap tiledMap = new TmxMapLoader().load("maps/map.tmx");
        world = new World();
        worldRenderer = new WorldRenderer(world, camera);

        world.init(tiledMap);

        userInterface = new UserInterface(world, getPlayer());
        setInputProcessor(basicProcessor);

        camera.setTo(getPlayer().getX() * 8.f, getPlayer().getY() * 8.f);

        Story.setScene(this);
    }

    @Override
    public void update(float dt) {
        Tween.updateAll(dt);

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
        userInterface.getViewport().setScreenSize(width, height);
        userInterface.getViewport().update(width, height, true);
        // to do
    }

    @Override
    public World getWorld() {
        return world;
    }
}
