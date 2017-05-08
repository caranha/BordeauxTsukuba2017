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

/**
 * Sprite class
 */
public class EntityRenderer implements Observer {

    private static String IMAGES_RES_FOLDER = "images/";

    private static int TILE_SIZE = 8;

    private Entity entity;
    private Texture image;

    private Rectangle rectangle;
    private final Tween animationX = new Tween(Interpolation.linear),
            animationY = new Tween(Interpolation.linear);

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

    /**
     * On update, calculate observable location and
     * prepare animation to reach it
     * @param observable
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        int targetX = entity.getX() * TILE_SIZE;
        int targetY = entity.getY() * TILE_SIZE;
        if (rectangle.x != targetX) {
            animationX.playWith(rectangle.x,
                    targetX, 0.25f);
        }
        if (rectangle.y != targetY) {
            animationY.playWith(rectangle.y,
                    targetY, 0.25f);
        }
    }

    public void render(Batch batch) {
        move();
        batch.draw(image,
                rectangle.getX(),       rectangle.getY(),
                rectangle.getWidth(),   rectangle.getHeight() );
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * Animate the player movements.
     */
    private void move() {
        if (animationX.isPlaying()) {
            rectangle.x = animationX.getInterpolation();
        }
        if (animationY.isPlaying()) {
            rectangle.y = animationY.getInterpolation();
        }
    }
}
