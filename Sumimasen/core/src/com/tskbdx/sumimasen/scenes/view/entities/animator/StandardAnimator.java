package com.tskbdx.sumimasen.scenes.view.entities.animator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Sydpy on 5/29/17.
 */
public class StandardAnimator implements Animator {

    private float stateTime = 0.f;

    private Animation<TextureRegion> animation;

    public StandardAnimator(TextureRegion [] regions, float frameDuration) {
        animation = new Animation<TextureRegion>(frameDuration, regions);
    }

    @Override
    public TextureRegion update() {

        stateTime += Gdx.graphics.getDeltaTime();

        return animation.getKeyFrame(stateTime, true);
    }
}
