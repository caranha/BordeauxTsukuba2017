package com.tskbdx.sumimasen.scenes.view.ui;

import com.badlogic.gdx.math.Interpolation;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.view.Tween;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;

import java.util.Observable;
import java.util.Observer;

/*
 * Created by viet khang on 31/05/2017.
 */

/**
 * get offset from a starting point to entity current location
 * with interpolation
 */
final class OffsetEntity implements Observer {
    private final Entity entity;
    private final Tween tweenX = new Tween(Interpolation.linear),
            tweenY = new Tween(Interpolation.linear);
    private int startX;
    private int startY;
    private float duration;

    OffsetEntity(Entity entity) {
        this.entity = entity;
        reset();
        entity.addObserver(this);
    }

    void reset() {
        startX = entity.getX();
        startY = entity.getY();
        tweenX.stop();
        tweenY.stop();
        duration = 2.f / entity.getSpeed();
    }

    @Override
    public void update(Observable o, Object arg) {
        float offsetX = (entity.getX() - startX) * EntityRenderer.TILE_SIZE;
        float offsetY = (entity.getY() - startY) * EntityRenderer.TILE_SIZE;
        tweenX.playWith(tweenX.getInterpolation(), offsetX, duration);
        tweenY.playWith(tweenY.getInterpolation(), offsetY, duration);
    }

    float getOffsetX() {
        return tweenX.getInterpolation();
    }

    float getOffsetY() {
        return tweenY.getInterpolation();
    }
}
