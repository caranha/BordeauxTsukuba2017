package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

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

    private Animation animation;

    private Rectangle rectangle;
    public EntityRenderer(Entity entity, String imagefile) {
        this.entity = entity;
        this.image = new Texture(IMAGES_RES_FOLDER + imagefile);
        this.rectangle = new Rectangle(
                entity.getX() * TILE_SIZE,
                entity.getY() * TILE_SIZE,
                entity.getWidth() * TILE_SIZE,
                entity.getHeight() * TILE_SIZE);
        entity.addObserver(this);
    }

    /**
     * On update, calculate observable location and
     * prepare the animation to reach it
     * @param observable
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        if ( rectangle.x != entity.getX() * TILE_SIZE
                || rectangle.y != entity.getY() * TILE_SIZE) {

            Vector2 target = new Vector2(entity.getX() * TILE_SIZE, entity.getY() * TILE_SIZE);
            int speed = entity.getSpeed();
            animation = new PositionInterpolationAnimation(rectangle, target, 1.f/speed);
            animation.start();
        }

        rectangle.width     = entity.getWidth() * TILE_SIZE;
        float scale_factor  = rectangle.width / image.getWidth();
        rectangle.height    = image.getHeight() * scale_factor;
    }

    public void render(Batch batch) {

        if (animation != null && !animation.isFinished()) {
            animation.update();
        }

        batch.draw(image,
                rectangle.getX(), rectangle.getY(),
                rectangle.getWidth(), rectangle.getHeight());
    }

    public float getX() {
        return rectangle.getX();
    }

    public float getY() {
        return rectangle.getY();
    }

    public float getWidth() {
        return rectangle.getWidth();
    }

    public float getHeight() {
        return rectangle.getHeight();
    }
}
