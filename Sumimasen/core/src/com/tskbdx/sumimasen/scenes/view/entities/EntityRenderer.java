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
public class EntityRenderer implements Observer {

    private static String IMAGES_RES_FOLDER = "images/";

    private static int TILE_SIZE = 8;

    private Entity entity;
    private Texture image;

    private Rectangle rectangle;

    public EntityRenderer(Entity entity, String imagefile) {
        this.entity = entity;
        this.image = new Texture(IMAGES_RES_FOLDER + imagefile);
        this.rectangle = new Rectangle();

        entity.addObserver(this);
        update(null, null);
    }

    @Override
    public void update(Observable observable, Object o) {
        rectangle.x         = entity.getX() * TILE_SIZE;
        rectangle.y         = entity.getY() * TILE_SIZE;
        rectangle.width     = entity.getWidth() * TILE_SIZE;

        float scale_factor = rectangle.width / image.getWidth();

        rectangle.height    = image.getHeight() * scale_factor;
    }

    public void render(Batch batch) {

        batch.draw(image,
                rectangle.getX(),       rectangle.getY(),
                rectangle.getWidth(),   rectangle.getHeight() );
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
