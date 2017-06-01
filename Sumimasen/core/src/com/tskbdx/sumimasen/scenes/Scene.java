package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.view.SmoothCamera;
import com.tskbdx.sumimasen.scenes.view.Tween;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;
import com.tskbdx.sumimasen.scenes.view.ui.UserInterface;

import java.util.List;

/**
 * Created by Sydpy on 4/27/17.
 */
public abstract class Scene {

    private final static float SCALE_FACTOR = 4.f;
    private World world;
    private WorldRenderer worldRenderer;
    private SmoothCamera camera;

    private List<TiledMapUtils.MapObjectDescriptor> mapObjectDescriptors;

    Scene() {
        GameScreen.getPlayer().clearInteracted();

        camera = new SmoothCamera(0.5f);
        camera.setToOrtho(false, 800, 480);
        camera.zoom = 1.f / SCALE_FACTOR;

        world = new World();
        worldRenderer = new WorldRenderer(world, camera);

        UserInterface.setScene(this);
    }

    public abstract void init();


    public void update(float dt) {
        Tween.updateAll(dt);

        getCamera().translate(
                GameScreen.getPlayer().getX() * 8 - getCamera().position.x,
                GameScreen.getPlayer().getY() * 8 - getCamera().position.y);
        getCamera().update();
    }

    public void render(Batch batch) {
        batch.setProjectionMatrix(getCamera().combined);
        getWorldRenderer().render();
    }

    public abstract boolean isFinished();

    public abstract Scene getNextScene();

    public abstract void dispose();

    public final World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public final WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }

    public void setWorldRenderer(WorldRenderer worldRenderer) {
        this.worldRenderer = worldRenderer;
    }

    public final SmoothCamera getCamera() {
        return camera;
    }

    public final List<TiledMapUtils.MapObjectDescriptor> getMapObjectMappings() {
        return mapObjectDescriptors;
    }

    final public void loadMap() {
        TiledMap tiledMap = new TmxMapLoader().load("maps/" + defaultMap() + ".tmx");
        mapObjectDescriptors = TiledMapUtils.mapObjectMappings(tiledMap, true);
        world.load(tiledMap, mapObjectDescriptors, defaultSpawn());
        worldRenderer.load(tiledMap, mapObjectDescriptors);
    }

    final public void loadMap(String map, String spawn) {
        TiledMap tiledMap = new TmxMapLoader().load("maps/" + map + ".tmx");
        mapObjectDescriptors = TiledMapUtils.mapObjectMappings(tiledMap, true);
        world.load(tiledMap, mapObjectDescriptors, spawn);
        worldRenderer.load(tiledMap, mapObjectDescriptors);
    }

    final public String getName() {
        String name = getClass().getSimpleName();
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    final public void setCamera(SmoothCamera camera) {
        this.camera = camera;
    }

    abstract protected String defaultMap();
    abstract protected String defaultSpawn();
    abstract protected String description();
}

