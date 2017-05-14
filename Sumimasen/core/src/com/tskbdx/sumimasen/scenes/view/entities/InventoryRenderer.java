package com.tskbdx.sumimasen.scenes.view.entities;

/*
 * Created by viet khang on 13/05/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.badlogic.gdx.graphics.Color.WHITE;

/**
 * For the moment :
 * observable instance is not stored
 * this can only add new stuff according to update
 *
 * To do : make a Inventory Class in model
 * -> observer it
 */
public class InventoryRenderer implements Observer {

    private static final float SIZE = 50.f;
    private final List<Texture> textures = new LinkedList<>();

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Entity) {
            Entity stored = (Entity) arg;
            // if it's an entity with no world set
            // we suppose it just has been removed from it
            // thus, picked up
            if (stored.getWorld() == null) {
                // to do : get from asset manager
                textures.add(new Texture(Gdx.files.internal(
                        "images/" + stored.getName() + ".png")));
            }
        }
    }

    public void render(Batch screenBatch) {
        screenBatch.setColor(WHITE);
        int i = 0;
        for (Texture texture : textures) {
            screenBatch.draw(texture, 10 + i++ * (SIZE * 1.5f),
                    Gdx.graphics.getHeight() - SIZE - 10, SIZE, SIZE);
        }
    }
}
