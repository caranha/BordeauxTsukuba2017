package com.tskbdx.sumimasen.scenes.view.entities.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sydpy on 5/12/17.
 */
public class DirectionSpriteSheetAnimator implements Animator {

    private Map<Direction, Animation<TextureRegion>> animations = new HashMap<>();

    private Animation currentAnimation;

    private Entity entity = null;

    private float stateTime = 0.f;

    public DirectionSpriteSheetAnimator(Map<Direction, TextureRegion[]> regions, float frameDuration) {

        for (Map.Entry<Direction, TextureRegion[]> directionEntry : regions.entrySet()) {
            animations.put(directionEntry.getKey(), new Animation<>(frameDuration, directionEntry.getValue()));
        }

        currentAnimation = animations.get(Direction.NONE);
    }

    @Override
    public TextureRegion update() {

        if (entity != null) {
            currentAnimation = animations.get(entity.isWalking() ? entity.getDirection() : entity.getLastDirection());
        }

        stateTime += Gdx.graphics.getDeltaTime();

        return (TextureRegion) currentAnimation.getKeyFrame(stateTime, true);
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
