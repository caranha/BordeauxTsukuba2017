package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.ChangeMap;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Teleport;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;
import com.tskbdx.sumimasen.scenes.view.effects.Effect;
import com.tskbdx.sumimasen.scenes.view.effects.Fade;
import com.tskbdx.sumimasen.scenes.view.entities.animator.Animator;
import com.tskbdx.sumimasen.scenes.view.entities.animator.DirectionSpriteSheetAnimator;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sydpy on 4/28/17.
 */

/**
 * Sprite class
 */
public class EntityRenderer implements Observer {

    public static int TILE_SIZE = 8;

    private Entity entity;
    private PositionSyncer positionSyncer;

    private Rectangle rectangle = new Rectangle();

    private Animator walkingAnimator;
    private Animator standingAnimator;

    private Animator currentAnimator;

    private WorldRenderer worldRenderer;

    public EntityRenderer(Entity entity) {
        this.entity = entity;
        rectangle.x = entity.getX() * TILE_SIZE;
        rectangle.y = entity.getY() * TILE_SIZE;
        rectangle.width = entity.getWidth() * TILE_SIZE;
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

        if (o == Teleport.class || o == ChangeMap.class) {
            if (worldRenderer != null) {
                Effect fadeIn = new Fade(Teleport.DELAY, Fade.IN);
                fadeIn.setCallback(() -> {
                    worldRenderer.setEffect(new Fade(Teleport.DELAY, Fade.OUT));
                });

                worldRenderer.setEffect(fadeIn);
            }
        }

        if (!positionSynced()) {
            Vector2 target = new Vector2(entity.getX() * TILE_SIZE, entity.getY() * TILE_SIZE);
            int speed = entity.getSpeed();
            positionSyncer = new InterpolationPositionSyncer(rectangle, target, 1.f / speed);
            positionSyncer.start();

        }

    }

    public void render(Batch batch) {

        if (!positionSynced()) {
            currentAnimator = walkingAnimator;
            if (currentAnimator instanceof DirectionSpriteSheetAnimator) {
                if (entity.getDirection() != Direction.NONE) {
                    ((DirectionSpriteSheetAnimator) currentAnimator).setCurrentDirection(entity.getDirection());
                }
            }
        } else {
            currentAnimator = standingAnimator;
            if (currentAnimator instanceof DirectionSpriteSheetAnimator) {
                ((DirectionSpriteSheetAnimator) currentAnimator).setCurrentDirection(entity.getLastDirection());
            }
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
        return rectangle.x == entity.getX() * TILE_SIZE
                && rectangle.y == entity.getY() * TILE_SIZE;
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

    public void setWalkingAnimator(Animator walkingAnimator) {
        this.walkingAnimator = walkingAnimator;

        if (currentAnimator == null) currentAnimator = walkingAnimator;
    }

    public void setStandingAnimator(Animator standingAnimator) {
        this.standingAnimator = standingAnimator;

        if (currentAnimator == null) currentAnimator = standingAnimator;
    }

}
