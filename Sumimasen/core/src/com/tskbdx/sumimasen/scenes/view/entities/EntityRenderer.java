package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    static int TILE_SIZE = 8;
    protected Texture image;
    private MessageRenderer messageRenderer;
    Entity entity;
    protected Animation animation;

    private Rectangle rectangle = new Rectangle();

    public EntityRenderer(Entity entity, String imagefile, AssetManager assetManager) {
        this.entity = entity;
        this.image = assetManager.get(IMAGES_RES_FOLDER + imagefile, Texture.class);
        rectangle.x = entity.getX() * TILE_SIZE;
        rectangle.y = entity.getY() * TILE_SIZE;
        rectangle.width = entity.getWidth() * TILE_SIZE;
        rectangle.height = image.getHeight() * (rectangle.width / image.getWidth());
        entity.addObserver(this);
        messageRenderer = new MessageRenderer(entity.getMessage());
    }

    /**
     * On update, calculate observable location and
     * prepare the animation to reach it
     *
     * @param observable
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        if (entityChangedPosition()) {
            Vector2 target = new Vector2(entity.getX() * TILE_SIZE, entity.getY() * TILE_SIZE);
            int speed = entity.getSpeed();
            animation = new PositionInterpolationAnimation(rectangle, target, 1.f / speed);
            animation.start();
        }
    }

    /**
     * @return true if this is the entity has succeeded to move
     */
    protected boolean entityChangedPosition() {
        return rectangle.x != entity.getX() * TILE_SIZE
                || rectangle.y != entity.getY() * TILE_SIZE;
    }

    boolean isAnimating() {
        return animation != null && !animation.isFinished();
    }

    void updateAnimation() {
        if (isAnimating()) {
            animation.update();
        }
    }

    public void render(Batch batch) {
        updateAnimation();
        batch.draw(image,
                rectangle.getX(), rectangle.getY(),
                rectangle.getWidth(), rectangle.getHeight());
        renderMessage(batch);
    }

    void renderMessage(Batch batch) {
        messageRenderer.render(batch);
    }

    private BitmapFont font = new BitmapFont();

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
