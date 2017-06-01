package com.tskbdx.sumimasen.scenes.view.entities.animator;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;

/**
 * Created by Sydpy on 5/12/17.
 */
public interface Animator {

    TextureRegion update();

    void setDirection(Direction direction);
}
