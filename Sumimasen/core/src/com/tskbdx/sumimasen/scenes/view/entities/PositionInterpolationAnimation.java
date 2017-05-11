package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sydpy on 5/6/17.
 */
public class PositionInterpolationAnimation implements Animation {

    private Vector2 start, target;
    private final float duration;

    private Rectangle rectangle;

    private boolean started = false;
    private boolean running = false;
    private Runnable onFinished;

    private float clock = 0.f;

    public PositionInterpolationAnimation(Rectangle rectangle, Vector2 target, float duration) {
        this.start      = new Vector2(rectangle.x, rectangle.y);
        this.target     = target;
        this.duration   = duration;

        this.rectangle = rectangle;
    }

    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

    @Override
    public void start() {
        started = true;
        running = true;
    }

    @Override
    public void update() {
        if(started && running) {
            float dt = Gdx.graphics.getDeltaTime();
            clock += dt;

            float progress = Math.min(1.f, clock/duration);
            rectangle.x = Interpolation.linear.apply(start.x, target.x, progress);
            rectangle.y = Interpolation.linear.apply(start.y, target.y, progress);
            if (progress == 1.f) {
                running = false;
                if (onFinished != null) {
                    onFinished.run();
                }
            }
        }
    }

    @Override
    public boolean isFinished() {
        return started && !running;
    }
}
