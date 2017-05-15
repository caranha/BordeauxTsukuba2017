package com.tskbdx.sumimasen.scenes.view.entities.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sydpy on 5/12/17.
 */
public class DirectionSpriteSheetAnimator implements Animator {

    private Map<Direction, Animation<TextureRegion>> notMovingAnimations = new HashMap<>();
    private Map<Direction, Animation<TextureRegion>> movingAnimations    = new HashMap<>();

    private EntityRenderer entityRenderer;

    private float stateTime = 0.f;

    private float lastX, lastY;

    /**
     * @param entityRenderer Renderer to animate
     * @param notMovingFrames Number of frames for the not moving animation
     * @param movingFrames Number of frames for the moving animation
     * @param frameWidth Width of each frame
     * @param frameHeight Height of each frame
     */
    public DirectionSpriteSheetAnimator(EntityRenderer entityRenderer,
                                        int notMovingFrames, int movingFrames,
                                        int frameWidth, int frameHeight, float frameDuration) {

        this.entityRenderer = entityRenderer;

        Texture spriteSheet = entityRenderer.getSpritesheet();

        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, frameWidth, frameHeight);

        TextureRegion[] standingBack    = new TextureRegion[notMovingFrames];
        System.arraycopy(tmp[0], 0, standingBack, 0, notMovingFrames);

        TextureRegion[] standingFront   = new TextureRegion[notMovingFrames];
        System.arraycopy(tmp[1], 0, standingFront, 0, notMovingFrames);

        TextureRegion[] standingLeft    = new TextureRegion[notMovingFrames];
        System.arraycopy(tmp[2], 0, standingLeft, 0, notMovingFrames);

        TextureRegion[] standingRight   = new TextureRegion[notMovingFrames];
        System.arraycopy(tmp[3], 0, standingRight, 0, notMovingFrames);

        notMovingAnimations.put(Direction.NORTH, new Animation<>(frameDuration, standingBack));
        notMovingAnimations.put(Direction.SOUTH, new Animation<>(frameDuration, standingFront));
        notMovingAnimations.put(Direction.WEST , new Animation<>(frameDuration, standingLeft));
        notMovingAnimations.put(Direction.EAST , new Animation<>(frameDuration, standingRight));
        notMovingAnimations.put(Direction.NONE , notMovingAnimations.get(Direction.SOUTH));

        TextureRegion[] movingBack      = new TextureRegion[movingFrames];
        System.arraycopy(tmp[4], 0, movingBack, 0, movingFrames);

        TextureRegion[] movingFront     = new TextureRegion[movingFrames];
        System.arraycopy(tmp[5], 0, movingFront, 0, movingFrames);

        TextureRegion[] movingLeft      = new TextureRegion[movingFrames];
        System.arraycopy(tmp[6], 0, movingLeft, 0, movingFrames);

        TextureRegion[] movingRight     = new TextureRegion[movingFrames];
        System.arraycopy(tmp[7], 0, movingRight, 0, movingFrames);

        movingAnimations.put(Direction.NORTH, new Animation<>(frameDuration, movingBack));
        movingAnimations.put(Direction.SOUTH, new Animation<>(frameDuration, movingFront));
        movingAnimations.put(Direction.WEST , new Animation<>(frameDuration, movingLeft));
        movingAnimations.put(Direction.EAST , new Animation<>(frameDuration, movingRight));
        movingAnimations.put(Direction.NONE , movingAnimations.get(Direction.SOUTH));

        lastX = entityRenderer.getX();
        lastY = entityRenderer.getY();
    }

    @Override
    public TextureRegion update() {
        stateTime += Gdx.graphics.getDeltaTime();

        Direction direction = entityRenderer.entityDirection();
        Direction lastDirection = entityRenderer.entityLastDirection();

        Animation<TextureRegion> animation =
                entityRenderer.entityDirection() != Direction.NONE ?
                movingAnimations.get(lastDirection) : notMovingAnimations.get(lastDirection);

        lastX = entityRenderer.getX();
        lastY = entityRenderer.getY();

        return animation.getKeyFrame(stateTime, true);
    }
}
