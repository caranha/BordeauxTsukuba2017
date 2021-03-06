package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tskbdx.sumimasen.scenes.TiledMapUtils;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.ChangeMap;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;
import com.tskbdx.sumimasen.scenes.view.effects.Effect;
import com.tskbdx.sumimasen.scenes.view.effects.Fade;
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

    private Entity entity;
    private TiledMapUtils.EntityDescriptor descriptor;
    private PositionSyncer positionSyncer;

    private Rectangle rectangle = new Rectangle();

    private Animator currentAnimator;

    private boolean walkingAnimation = false;

    public EntityRenderer(Entity entity, TiledMapUtils.EntityDescriptor descriptor) {
        this.entity = entity;
        this.descriptor = descriptor;

        rectangle.x = entity.getX() * TiledMapUtils.TILE_SIZE;
        rectangle.y = entity.getY() * TiledMapUtils.TILE_SIZE;
        rectangle.width = entity.getWidth() * TiledMapUtils.TILE_SIZE;

        entity.addObserver(this);
    }
    private WorldRenderer worldRenderer;

    /**
     * On load, calculate observable location and
     * prepare the positionSyncer to reach it
     *
     * @param observable
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        if (o == ChangeMap.class) {
            if (worldRenderer != null) {
                Effect fadeIn = new Fade(ChangeMap.DELAY, Fade.IN);
                fadeIn.setCallback(() -> {
                    worldRenderer.setEffect(new Fade(ChangeMap.DELAY, Fade.OUT));
                });

                worldRenderer.setEffect(fadeIn);
            }
        }

        if (!positionSynced()) {
            Vector2 target = new Vector2(entity.getX() * TiledMapUtils.TILE_SIZE, entity.getY() * TiledMapUtils.TILE_SIZE);
            int speed = entity.getSpeed();
            positionSyncer = new InterpolationPositionSyncer(rectangle, target, 1.f / speed);
            positionSyncer.start();

        }

    }

    public void render(Batch batch) {

        if (entity.isWalking()) {
            currentAnimator = SpritesheetUtils.getAnimatorFromSpritesheet(descriptor.walkingSpritesheet);
            currentAnimator.setDirection(entity.getDirection());

        } else {
            currentAnimator = SpritesheetUtils.getAnimatorFromSpritesheet(descriptor.standingSpritesheet);
            currentAnimator.setDirection(entity.getLastDirection());
        }

        if (entity.getWorld() != null) {
            updatePosition();

            if (currentAnimator != null) {

                TextureRegion textureRegion = currentAnimator.update();

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

    private boolean positionSynced() {
        return rectangle.x == entity.getX() * TiledMapUtils.TILE_SIZE
                && rectangle.y == entity.getY() * TiledMapUtils.TILE_SIZE;
    }

    private boolean isMoving() {
        return positionSyncer != null && !positionSyncer.isFinished();
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

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setWorldRenderer(WorldRenderer worldRenderer) {
        this.worldRenderer = worldRenderer;
    }

    public Entity getEntity() {
        return entity;
    }
}
