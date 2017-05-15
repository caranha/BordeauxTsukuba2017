package com.tskbdx.sumimasen.scenes.view.ui;

/*
 * Created by viet khang on 13/05/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;
import com.tskbdx.sumimasen.scenes.model.entities.Inventory;
import com.tskbdx.sumimasen.scenes.model.entities.SceneObject;

import java.util.*;

import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.tskbdx.sumimasen.Sumimasen.getAssetManager;

/**
 * Observe a Inventory instance in model
 * and update from it.
 */
final class InventoryRenderer implements Observer, Disposable {

    private static final String FOLDER = "images/";
    private static final float SIZE = 50.f;
    private static final float PADDING = 10.f;
    private final Map<SceneObject, Texture> textures = new HashMap<>();
    private final Set<SceneObject> viewInventory;
    private final Set<SceneObject> modelInventory;

    InventoryRenderer(Inventory inventory) {
        inventory.addObserver(this);
        modelInventory = inventory.getObjects();
        viewInventory = new HashSet<>(modelInventory);
    }

    @Override
    public void update(Observable o, Object arg) {
        assert modelInventory.size() != viewInventory.size();
        Set<SceneObject> difference;
        if (modelInventory.size() > viewInventory.size()) {
            difference = getDifference(modelInventory, viewInventory);
            for (SceneObject object : difference) {
                textures.put(object,
                        getAssetManager().get(FOLDER + object.getName() + ".png"));
            }
        } else {
            difference = getDifference(viewInventory, modelInventory);
            for (SceneObject object : difference) {
                textures.remove(object);
            }
        }
    }

    private Set<SceneObject> getDifference(Set<SceneObject> a, Set<SceneObject> b) {
        Set<SceneObject> difference = new HashSet<>(a);
        difference.removeAll(b);
        return difference;
    }

    public void render(Batch screenBatch) {
        screenBatch.setColor(WHITE);
        int i = 0;

        for (Map.Entry<SceneObject, Texture> entry : textures.entrySet()) {
            screenBatch.draw(entry.getValue(), PADDING + i++ * SIZE,
                    Gdx.graphics.getHeight() - SIZE - PADDING, SIZE, SIZE);
        }
    }

    @Override
    public void dispose() {
        for (Map.Entry<SceneObject, Texture> entry : textures.entrySet()) {
            entry.getValue().dispose();
        }
    }
}
