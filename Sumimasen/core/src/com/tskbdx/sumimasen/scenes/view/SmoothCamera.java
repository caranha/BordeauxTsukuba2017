package com.tskbdx.sumimasen.scenes.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.tskbdx.sumimasen.scenes.model.entities.Player;

import static com.tskbdx.sumimasen.GameScreen.getPlayer;

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
    private float targetX, targetY;
    private final Tween tweenX = new Tween(interpolation),
            tweenY = new Tween(interpolation);

    public SmoothCamera(float transitionInSec) {
        duration = transitionInSec;
        Player player = getPlayer();
        position.x = player.getX() * 8;
        position.y = player.getY() * 8;
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
}
