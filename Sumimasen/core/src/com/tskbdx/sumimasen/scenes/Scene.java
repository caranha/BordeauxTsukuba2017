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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sydpy on 4/27/17.
 */
public abstract class Scene {

    private final static float SCALE_FACTOR = 4.f;
    private static Map<String, TiledMap> loadedMaps = new HashMap<>();
    private World world;
    private WorldRenderer worldRenderer;
    private SmoothCamera camera;
    private List<TiledMapUtils.EntityDescriptor> entityDescriptors;
    private List<TiledMapUtils.SensorDescriptor> sensorDescriptors;

    protected Scene() {
        System.out.println("-> " + getName());
        System.out.println(description() + "\n");

        GameScreen.getPlayer().clearInteracted();

        camera = new SmoothCamera(0.5f);
        camera.setToOrtho(false, 800, 480);
        camera.zoom = 1.f / SCALE_FACTOR;

        world = new World();
        worldRenderer = new WorldRenderer(world, camera);

        UserInterface.setScene(this);

        GameScreen.getPlayer().think(description());
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

    final public void setCamera(SmoothCamera camera) {
        this.camera = camera;
    }

    final public String getName() {
        String name = getClass().getSimpleName();
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public final List<TiledMapUtils.EntityDescriptor> getEntityDescriptors() {
        return entityDescriptors;
    }

    /**
     * This method is to call first before calling init()
     * It loads the scene with the player, according to
     * the default map and the default spawn
     */
    public void loadMap() {
        loadMap(defaultMap(), defaultSpawn(), true);
    }

    /**
     * This method is to call to change map
     */
    public void loadMap(String map, String spawn, boolean withPlayer) {

        TiledMap tiledMap;
        if (loadedMaps.containsKey(map)) {
            tiledMap = loadedMaps.get(map);
        } else {
            tiledMap = new TmxMapLoader().load("maps/" + map + ".tmx");
            loadedMaps.put(map, tiledMap);
        }

        entityDescriptors = TiledMapUtils.entityDescriptors(tiledMap, withPlayer);
        sensorDescriptors = TiledMapUtils.sensorDescriptors(tiledMap);

        world.load(tiledMap, entityDescriptors, sensorDescriptors, spawn);
        worldRenderer.load(tiledMap, entityDescriptors, sensorDescriptors);
    }

    abstract protected String defaultMap();

    abstract protected String defaultSpawn();

    public abstract String description();
}

