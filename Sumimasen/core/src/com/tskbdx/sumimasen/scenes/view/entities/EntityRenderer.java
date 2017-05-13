package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.view.entities.animator.Animator;

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

    private Texture spritesheet;
    private MessageRenderer messageRenderer;
    private Entity entity;
    private PositionSyncer positionSyncer;

    private BitmapFont font = new BitmapFont();
    private Rectangle rectangle = new Rectangle();

    private Animator animator = null;

    public EntityRenderer(Entity entity, String imagefile, AssetManager assetManager) {
        this.entity = entity;
        this.spritesheet = assetManager.get(IMAGES_RES_FOLDER + imagefile, Texture.class);
        rectangle.x = entity.getX() * TILE_SIZE;
        rectangle.y = entity.getY() * TILE_SIZE;
        rectangle.width = entity.getWidth() * TILE_SIZE;
        rectangle.height = spritesheet.getHeight() * (rectangle.width / spritesheet.getWidth());
        entity.addObserver(this);
        messageRenderer = new MessageRenderer(entity.getMessage());
    }

    /**
     * On update, calculate observable location and
     * prepare the positionSyncer to reach it
     *
     * @param observable
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        if (positionSynced()) {
            Vector2 target = new Vector2(entity.getX() * TILE_SIZE, entity.getY() * TILE_SIZE);
            int speed = entity.getSpeed();
            positionSyncer = new InterpolationPositionSyncer(rectangle, target, 1.f / speed);
            positionSyncer.start();
        }
    }

    public void render(Batch batch) {
        updatePosition();

        if (animator == null) {
            batch.draw(spritesheet,
                    rectangle.getX(), rectangle.getY(),
                    rectangle.getWidth(), rectangle.getHeight());
        } else {
            TextureRegion textureRegion = animator.update();

            rectangle.setHeight(textureRegion.getRegionHeight() * (rectangle.width / textureRegion.getRegionWidth()));

            batch.draw(textureRegion,
                    rectangle.getX(), rectangle.getY(),
                    rectangle.getWidth(), rectangle.getHeight());
        }

        renderMessage(batch);
    }

    private void updatePosition() {
        if (isMoving()) {
            positionSyncer.update();
        }
    }

    private void renderMessage(Batch batch) {
        messageRenderer.render(batch);
    }

    /**
     * @return true if this is the entity has succeeded to move
     */
    public boolean positionSynced() {
        return rectangle.x != entity.getX() * TILE_SIZE
                || rectangle.y != entity.getY() * TILE_SIZE;
    }

    public boolean isMoving() {
        return positionSyncer != null && !positionSyncer.isFinished();
    }

    public Direction entityDirection() {
        return entity.getDirection();
    }

    public void setAnimator(Animator animator) {
        this.animator = animator;
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

    public Texture getSpritesheet() {
        return spritesheet;
    }
}
