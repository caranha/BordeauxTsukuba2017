package com.tskbdx.sumimasen.scenes.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;

/**
 * Created by viet khang on 08/05/2017.
 */

/**
 * Camera with transitions.
 */
public class SmoothCamera extends OrthographicCamera {
    private final Interpolation interpolation;
    private final float duration;
    private float targetX, targetY;
    private Tween tweenX, tweenY;

    public SmoothCamera(Interpolation interpolation, float transitionInSec) {
        this.interpolation = interpolation;
        duration = transitionInSec;
    }

    @Override
    public void update() {
        if (tweenX != null && tweenX.isPlaying()) {
            position.x = tweenX.getInterpolation();
        }
        if (tweenY != null && tweenY.isPlaying()) {
            position.y = tweenY.getInterpolation();
        }
        super.update();
        System.out.println("pdate");
    }

    @Override
    public void translate(float dx, float dy) {
        if (dx != 0 && dy != 0 &&
                targetX != position.x + dx &&
                targetY != position.y + dy) {
            targetX = position.x + dx;
            targetY = position.y + dy;
            tweenX = new Tween(interpolation, position.x,
                    targetX, duration);
            tweenY = new Tween(interpolation, position.y,
                    targetY, duration);
        }
    }
}
