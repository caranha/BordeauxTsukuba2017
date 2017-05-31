package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.tskbdx.sumimasen.scenes.inputprocessors.GameCommands;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.story.Story;
import com.tskbdx.sumimasen.scenes.view.SmoothCamera;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sydpy on 4/27/17.
 */
public abstract class Scene {

    public final static float SCALE_FACTOR = 4.f;
    protected final Story story = Story.getInstance();
    protected String currentMap = "map";
    protected String spawn = "player_home";
    private World world;
    private WorldRenderer worldRenderer;
    private SmoothCamera camera;
    private InputProcessor inputProcessor;
    private Map<String, TiledMap> loadedMaps = new HashMap<>();

    private List<TiledMapUtils.MapObjectMapping> mapObjectMappings;

    public Scene() {

        camera = new SmoothCamera(0.5f);
        camera.setToOrtho(false, 800, 480);
        camera.zoom = 1.f / SCALE_FACTOR;

        inputProcessor = new GameCommands();
        Gdx.input.setInputProcessor(inputProcessor);

        world = new World();
        worldRenderer = new WorldRenderer(world, camera);
    }

    public abstract void init();


    public abstract void update(float dt);

    public abstract void render(Batch batch);

    public abstract boolean isFinished();

    public abstract Scene getNextScene();

    public abstract void dispose();

    public final World getWorld() {
        return world;
    }

    public final WorldRenderer getWorldRenderer() {
        return worldRenderer;
    }

    public final SmoothCamera getCamera() {
        return camera;
    }

    public final InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public final List<TiledMapUtils.MapObjectMapping> getMapObjectMappings() {
        return mapObjectMappings;
    }

    public void loadMap(String map, String spawn) {

        TiledMap tiledMap;

        if (!loadedMaps.containsKey(map)) {
            tiledMap = new TmxMapLoader().load("maps/" + map + ".tmx");
            loadedMaps.put(map, tiledMap);
            mapObjectMappings = TiledMapUtils.mapObjectMappings(tiledMap);
        } else {
            tiledMap = loadedMaps.get(map);
        }
        this.currentMap = map;
        this.spawn = spawn;


        world.init(tiledMap, mapObjectMappings, spawn);
        worldRenderer.init(tiledMap, mapObjectMappings);
    }
}

