package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sydpy on 4/28/17.
 */
public class EntityObserver implements Observer {

    private Entity entity;
    private Texture image;

    public EntityObserver(Entity entity, String imagefile) {
        this.entity = entity;
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    public void draw(Batch batch) {

        Rectangle rectangle = entity.getRectangle();

        batch.draw(image,
                rectangle.getX(), rectangle.getY(),
                rectangle.getWidth(), rectangle.getHeight() );
    }
}
