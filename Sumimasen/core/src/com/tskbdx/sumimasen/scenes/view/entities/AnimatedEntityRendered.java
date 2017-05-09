package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

/**
 * Created by viet khang on 09/05/2017.
 */

/**
 * Convention : sprite sheet taken are 1 col and many rows
 * Last frame is default (when not animating)
 */
public class AnimatedEntityRendered extends EntityRenderer {
    private float stateTime;
    private TextureRegion defaultTexture;
    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> animation;

    public AnimatedEntityRendered(Entity entity, String imagefile, int frameCount, int fps) {
        super(entity, imagefile);
        TextureRegion[] frames = initFrames(frameCount);
        animation = new Animation<>(1.f / fps, frames);
    }

    private TextureRegion[] initFrames(int frameCount) {
        TextureRegion[][] tmp = TextureRegion.split(image,
                image.getWidth(),
                image.getHeight() / frameCount);
        TextureRegion[] frames = new TextureRegion[frameCount - 1];
        for (int i = 0 ; i < frameCount - 1; i++) {
            frames[i] = tmp[i][0];
        }
        defaultTexture = tmp[frameCount - 1][0];
        return frames;
    }

    @Override
    public void render(Batch batch) {
        stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        if (isAnimating()) {
            updateAnimation();
            batch.draw(animation.getKeyFrame(stateTime, true), getX(), getY());
        } else {
            batch.draw(defaultTexture, getX(), getY());
        }

        renderMessage(batch);
    }
}
