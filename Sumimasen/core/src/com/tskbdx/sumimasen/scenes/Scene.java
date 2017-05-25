package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.tskbdx.sumimasen.scenes.inputprocessors.GameCommands;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.view.SmoothCamera;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by Sydpy on 4/27/17.
 */
public abstract class Scene {

    public final static float SCALE_FACTOR = 4.f;

    protected String currentMap = "maps/map.tmx";

    private World world;
    private WorldRenderer worldRenderer;

    private SmoothCamera camera;
    private InputProcessor inputProcessor;

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

    protected final void loadMap(String map) {

        if (map != null && map.endsWith(".tmx")) {
            currentMap = map;

            TiledMap tiledMap = new TmxMapLoader().load(map);

            mapObjectMappings = TiledMapUtils.mapObjectMappings(tiledMap);

            world.init(tiledMap, mapObjectMappings);
            worldRenderer.init(tiledMap, mapObjectMappings);

        }
    }

    public void save(String dirName) throws IOException {

        System.out.println("Serializing world");

        FileOutputStream fileOutputStream1 = new FileOutputStream(dirName + "world.save", false);
        fileOutputStream1.flush();
        ObjectOutputStream out1 = new ObjectOutputStream(fileOutputStream1);
        out1.writeObject(world);
        out1.close();
        fileOutputStream1.close();

        System.out.println("Serializing current map");

        FileOutputStream fileOutputStream2 = new FileOutputStream(dirName + "map.save", false);
        fileOutputStream2.flush();
        ObjectOutputStream out2 = new ObjectOutputStream(fileOutputStream2);
        out2.writeObject(currentMap);
        out2.close();
        fileOutputStream2.close();

        System.out.println("Serializing current map objects mapping");

        FileOutputStream fileOutputStream3 = new FileOutputStream(dirName + "mapping.save", false);
        fileOutputStream3.flush();
        ObjectOutputStream out3 = new ObjectOutputStream(fileOutputStream3);
        out3.writeObject(mapObjectMappings);
        out3.close();
        fileOutputStream3.close();
    }
}
