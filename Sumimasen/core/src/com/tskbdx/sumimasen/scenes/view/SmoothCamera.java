package com.tskbdx.sumimasen.scenes.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;

/*
 * Created by viet khang on 08/05/2017.
 */

/**
 * Camera with smooth transitions.
 * Can be set with the transition duration on instanciation.
 */
public class SmoothCamera extends OrthographicCamera {
    private final Interpolation interpolation = Interpolation.linear;
    private final float duration;
    private final Tween tweenX = new Tween(interpolation),
            tweenY = new Tween(interpolation);
    private float targetX, targetY;

    public SmoothCamera(float transitionInSec) {
        //   camera.translate(player.getX()*8 - camera.position.x, player.getY()*8 - camera.position.y);

        duration = transitionInSec;
        super.update();
    }

    /**
     * Update position according to current tweens state.
     */
    @Override
    public void update() {
        if (tweenX.isPlaying()) {
            position.x = tweenX.getInterpolation();
        }
        if (tweenY.isPlaying()) {
            position.y = tweenY.getInterpolation();
        }
        super.update();
    }

    @Override
    public void translate(float dx, float dy) {
        if (dx != 0 && targetX != position.x + dx) {
            targetX = position.x + dx;
            tweenX.playWith(position.x,
                    targetX, duration);
        }
        if (dy != 0 && targetY != position.y + dy) {
            targetY = position.y + dy;
            tweenY.playWith(position.y,
                    targetY, duration);
        }
    }

    public void setTo(float x, float y) {
        targetX = position.x = x;
        targetY = position.y = y;
    }
}
