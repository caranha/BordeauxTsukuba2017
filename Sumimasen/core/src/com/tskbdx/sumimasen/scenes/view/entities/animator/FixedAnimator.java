package com.tskbdx.sumimasen.scenes.view.entities.animator;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;

/**
 * Created by Sydpy on 5/23/17.
 */
public class FixedAnimator implements Animator {

    private TextureRegion textureRegion;

    public FixedAnimator(Texture texture) {
        textureRegion = new TextureRegion(texture);
    }

    @Override
    public TextureRegion update() {
        return textureRegion;
    }

    @Override
    public void setDirection(Direction direction) {

    }
}
