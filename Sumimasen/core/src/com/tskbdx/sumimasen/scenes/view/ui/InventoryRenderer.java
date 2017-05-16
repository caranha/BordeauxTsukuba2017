package com.tskbdx.sumimasen.scenes.view.ui;

/*
 * Created by viet khang on 13/05/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Inventory;
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
    private final Map<Entity, Slot> textures = new HashMap<>();
    private final List<Slot> slots = new ArrayList<>();
    private final List<Entity> viewInventory;
    private final Collection<Entity> modelInventory;

    InventoryRenderer(Inventory inventory) {
        inventory.addObserver(this);
        modelInventory = inventory.getObjects();
        viewInventory = new ArrayList<>(modelInventory);
    }

    @Override
    public void update(Observable o, Object arg) {
        assert modelInventory.size() != viewInventory.size();
        Collection<Entity> difference;
        if (modelInventory.size() > viewInventory.size()) {
            /*
             * There are more in the model than in the view.
              * Let's add some items here.
             */
            difference = getDifference(modelInventory, viewInventory);
            for (Entity object : difference) {
                Texture texture = getAssetManager().get(FOLDER + object.getName() + ".png", Texture.class);
                Slot slot = new Slot(texture, slots.size());
                textures.put(object, slot);
                slots.add(slot);
                viewInventory.add(object);
            }
        } else {
            /*
             * There are less in the model than in the view.
              * Let's remove some items here.
             */
            difference = getDifference(viewInventory, modelInventory);
            for (Entity object : difference) {
                slots.remove(textures.get(object));
                textures.remove(object);
                viewInventory.remove(object);
                for (int index = 0 ; index != slots.size() ; ++index) {
                    slots.get(index).moveTo(index);
                }
            }
        }
    }

    private Collection<Entity> getDifference(Collection<Entity> a,
                                           Collection<Entity> b) {
        Collection<Entity> difference = new HashSet<>(a);
        difference.removeAll(b);
        return difference;
    }

    public void render(Batch screenBatch) {
        screenBatch.setColor(WHITE);

        for (Map.Entry<Entity, Slot> entry : textures.entrySet()) {
            Slot slot = entry.getValue();
            screenBatch.draw(slot.texture, slot.x(), slot.y(), slot.size(), slot.size());
        }
    }

    @Override
    public void dispose() {
        for (Map.Entry<Entity, Slot> entry : textures.entrySet()) {
            entry.getValue().dispose();
        }
    }

    private class Slot implements Disposable {
        static final float PADDING = 10.f;
        static final float SIZE = 50.f;
        final Texture texture;
        final Tween scaleTween = new Tween(Interpolation.bounceOut);
        final Tween positionTween = new Tween(Interpolation.smooth);
        float position;

        Slot(Texture texture, float startPosition) {
            this.texture = texture;
            position = startPosition;
            scaleTween.playWith(0, 1, 1);
        }

        float x() {
            return (PADDING + SIZE) * (position + positionInterpolation())
                    + PADDING + scaleInterpolation();
        }

        float y() {
            return Gdx.graphics.getHeight() - SIZE - PADDING + scaleInterpolation();
        }

        float size() {
            return SIZE * scaleTween.getInterpolation();
        }

        float scaleInterpolation() {
            return (1 - scaleTween.getInterpolation()) * SIZE / 2;
        }

        float positionInterpolation() { return positionTween.getInterpolation(); }

        void moveTo(int newPosition) {
            //positionTween.playWith(position + positionInterpolation(), newPosition, 1);
            // to do : an animation
            position = newPosition;
        }

        @Override
        public void dispose() {
            texture.dispose();
        }
    }
}
