package com.tskbdx.sumimasen.scenes.view.ui;

/*
 * Created by viet khang on 13/05/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import com.tskbdx.sumimasen.scenes.model.entities.Inventory;
import com.tskbdx.sumimasen.scenes.model.entities.SceneObject;
import com.tskbdx.sumimasen.scenes.view.Tween;

import java.util.*;

import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.tskbdx.sumimasen.Sumimasen.getAssetManager;

/**
 * Observe a Inventory instance in model
 * and update from it.
 */
final class InventoryRenderer implements Observer, Disposable {

    private static final String FOLDER = "images/";
    private final Map<SceneObject, Slot> textures = new HashMap<>();
    private final List<Slot> slots = new ArrayList<>();
    private final List<SceneObject> viewInventory;
    private final Collection<SceneObject> modelInventory;

    InventoryRenderer(Inventory inventory) {
        inventory.addObserver(this);
        modelInventory = inventory.getObjects();
        viewInventory = new ArrayList<>(modelInventory);
    }

    @Override
    public void update(Observable o, Object arg) {
        assert modelInventory.size() != viewInventory.size();
        Collection<SceneObject> difference;
        if (modelInventory.size() > viewInventory.size()) {
            difference = getDifference(modelInventory, viewInventory);
            for (SceneObject object : difference) {
                Texture texture = getAssetManager().get(FOLDER + object.getName() + ".png", Texture.class);
                Slot slot = new Slot(texture, slots);
                textures.put(object, slot);
                slots.add(slot);
                viewInventory.add(object);
            }
        } else {
            difference = getDifference(viewInventory, modelInventory);
            for (SceneObject object : difference) {
                slots.remove(textures.get(object));
                textures.remove(object);
                viewInventory.remove(object);
            }

        }
    }

    private Collection<SceneObject> getDifference(Collection<SceneObject> a,
                                           Collection<SceneObject> b) {
        Collection<SceneObject> difference = new HashSet<>(a);
        difference.removeAll(b);
        return difference;
    }

    public void render(Batch screenBatch) {
        screenBatch.setColor(WHITE);

        for (Map.Entry<SceneObject, Slot> entry : textures.entrySet()) {
            Slot slot = entry.getValue();
            screenBatch.draw(slot.texture, slot.x(), slot.y(), slot.size(), slot.size());
        }
    }

    @Override
    public void dispose() {
        for (Map.Entry<SceneObject, Slot> entry : textures.entrySet()) {
            entry.getValue().dispose();
        }
    }

    private class Slot implements Disposable {
        static final float PADDING = 10.f;
        static final float SIZE = 50.f;
        final Texture texture;
        private final List slots;
        private Tween tween = new Tween(Interpolation.bounceOut);

        Slot(Texture texture, List slots) {
            this.texture = texture;
            this.slots = slots;
            tween.playWith(0, 1, 1);
        }

        float x() {
            return (PADDING + SIZE) * slots.indexOf(this) + PADDING + interpolationOffset();
        }

        float y() {
            return Gdx.graphics.getHeight() - SIZE - PADDING + interpolationOffset();
        }

        float size() {
            return SIZE * tween.getInterpolation();
        }

        private float interpolationOffset() {
            return (1 - tween.getInterpolation()) * SIZE / 2;
        }

        @Override
        public void dispose() {
            texture.dispose();
        }
    }
}
