package com.tskbdx.sumimasen.scenes.view.ui;

/*
 * Created by viet khang on 13/05/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.TiledMapUtils;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Inventory;
import com.tskbdx.sumimasen.scenes.view.Tween;
import com.tskbdx.sumimasen.scenes.view.entities.SpritesheetUtils;
import com.tskbdx.sumimasen.scenes.view.entities.animator.Animator;

import java.util.*;

/**
 * Observe a Inventory instance in model
 * and load from it.
 */
final class InventoryRenderer implements Observer, Disposable {

    private static final String FOLDER = "images/";
    private final Map<Entity, Slot> textures = new HashMap<>();
    private final List<Slot> slots = new ArrayList<>();
    private final List<Entity> viewInventory;
    private final Collection<Entity> modelInventory;
    private final Scene scene;

    InventoryRenderer(Inventory inventory, Scene scene) {
        inventory.addObserver(this);

        this.scene = scene;

        modelInventory = inventory.getObjects();
        viewInventory = new ArrayList<>(modelInventory);
    }

    @Override
    public void update(Observable o, Object arg) {

        Collection<Entity> difference;
        if (modelInventory.size() > viewInventory.size()) {
            /*
             * There are more in the model than in the view.
              * Let's add some items here.
             */
            difference = getDifference(modelInventory, viewInventory);
            for (Entity object : difference) {

                TiledMapUtils.MapObjectDescriptor mapObjectDescriptor = null;
                for (TiledMapUtils.MapObjectDescriptor mapping: scene.getMapObjectDescriptors()) {
                   if (mapping.name.equals(object.getName())) {
                       mapObjectDescriptor = mapping;
                       break;
                   }
                }

                if (mapObjectDescriptor != null) {

                    Animator animator = SpritesheetUtils.getAnimatorFromSpritesheet(mapObjectDescriptor.standingSpritesheet);
                    if ( animator == null) SpritesheetUtils.getAnimatorFromSpritesheet(mapObjectDescriptor.standingSpritesheet);

                    Slot slot = new Slot(animator, slots.size());
                    textures.put(object, slot);
                    slots.add(slot);
                    viewInventory.add(object);
                }

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
        screenBatch.setColor(Color.WHITE);

        for (Map.Entry<Entity, Slot> entry : textures.entrySet()) {
            Slot slot = entry.getValue();
            screenBatch.draw(slot.animator.update(), slot.x(), slot.y(), slot.size(), slot.size());
        }
    }

    @Override
    public void dispose() {
    }

    private class Slot {
        static final float PADDING = 10.f;
        static final float SIZE = 50.f;

        final Animator animator;
        final Tween scaleTween = new Tween(Interpolation.bounceOut);
        final Tween positionTween = new Tween(Interpolation.smooth);
        float position;

        Slot(Animator animator, float startPosition) {
            this.animator = animator;
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
    }
}
