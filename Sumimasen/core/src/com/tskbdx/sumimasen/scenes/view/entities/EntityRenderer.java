package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.view.Tween;

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
    private Tween mouvementX, mouvementY;

    public EntityRenderer(Entity entity, String imagefile) {
        this.entity = entity;
        this.image = new Texture(IMAGES_RES_FOLDER + imagefile);
        float width = entity.getWidth() * TILE_SIZE;
        float height = image.getHeight() * (width / image.getWidth());
        this.rectangle = new Rectangle(
                entity.getX() * TILE_SIZE,
                entity.getY() * TILE_SIZE,
                width, height);
        entity.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object o) {
        int targetX = entity.getX() * TILE_SIZE;
        int targetY = entity.getY() * TILE_SIZE;
        if (rectangle.x != targetX) {
            mouvementX = new Tween(Interpolation.smooth, rectangle.x, targetX, 0.25f);
            mouvementX.play();
        }
        else if (rectangle.y != targetY) {
            mouvementY = new Tween(Interpolation.smooth, rectangle.y, targetY, 0.25f);
            mouvementY.play();
        }
    }

    public void render(Batch batch) {
        if (mouvementX != null && mouvementX.isPlaying()) {
            rectangle.x = mouvementX.getInterpolation();
        }
        if (mouvementY != null && mouvementY.isPlaying()) {
            rectangle.y = mouvementY.getInterpolation();
        }
        batch.draw(image,
                rectangle.getX(),       rectangle.getY(),
                rectangle.getWidth(),   rectangle.getHeight() );
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
