package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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

    public static int TILE_SIZE = 8;

    private Texture spritesheet;
    private Entity entity;
    private PositionSyncer positionSyncer;

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
        // to dynamically remove from world and update in view
        // we have several choices :
        // - Make WorldRenderer to observe World
        // - Put a tag in Entity to know if it's removed or not
        // - Get current entity world (if null, then we can suppose
        // it has been removed from)
        // This last solution is the simpler as it doesn't affect
        // current architecture but it can lead to dysfunctions somehow
        if (entity.getWorld() != null) {
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
        }
    }

    private void updatePosition() {
        if (isMoving()) {
            positionSyncer.update();
        }
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

    public Direction entityLastDirection() {
        return entity.getLastDirection();
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
