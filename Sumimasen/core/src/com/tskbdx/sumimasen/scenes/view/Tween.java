package com.tskbdx.sumimasen.scenes.view;

/**
 * Created by viet khang on 08/05/2017.
 */

import com.badlogic.gdx.math.Interpolation;

import java.util.ArrayList;
import java.util.List;

/**
 * Define a tween based on GDX interpolations
 */

/**
 * You'd want to call updateAll in main loop to update all tweens.
 */
public class Tween {
    /**
     * All the tweens gathered in the class.
     */
    private static List<Tween> tweens = new ArrayList<>();

    /**
     * Properties
     */
    private final Interpolation interpolation;
    private float currentPosition = 0;
    private boolean playing = false;
    private float start, end, duration;
    private Runnable onFinished;
    private float delay = 0.f;
    private float delayTimer = 0.f;

    public Tween(Interpolation interpolation) {
        Tween.tweens.add(this);
        this.interpolation = interpolation;
    }

    /**
     * Update all tweens
     *
     * @param dt delta time from GDX loop
     */
    public static void updateAll(float dt) {
        for (Tween tween : tweens) {
            tween.update(dt);
        }
    }

    public void playWith(float start, float end, float durationInSec) {
        currentPosition = 0;
        duration = durationInSec;
        this.start = start;
        this.end = end;
        delayTimer = 0.f;
        play();
    }

    public void playWith(float start, float end, float durationInSec, float delay) {
        playWith(start, end, durationInSec);
        this.delay = delay;
    }

    /**
     * Start the tween.
     */
    public void play() {
        playing = true;
    }

    /**
     * Pause the twee.
     */
    void pause() {
        playing = false;
    }

    /**
     * Pause and restart the tween.
     */
    void stop() {
        pause();
        currentPosition = 0;
    }

    public boolean isPlaying() {
        return playing;
    }

    /**
     * @return calculate interpolation with start, end, current and duration
     * according to the GDX Interpolation chosen.
     */
    public float getInterpolation() {
        return interpolation.apply(start, end, progress());
    }

    /**
     * @return progression value between 0 and 1.
     */
    private float progress() {
        return currentPosition / duration;
    }

    /**
     * Update the tween according to elapsed time.
     *
     * @param dt
     */
    private void update(float dt) {
        if (playing) {
            if (delayTimer <= delay) {
                currentPosition += dt;
                if (currentPosition > duration) {
                    currentPosition = duration;
                    playing = false;
                    if (onFinished != null) {
                        onFinished.run();
                    }
                }
            } else {
                delayTimer += dt;
            }
        }
    }

    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }
}
