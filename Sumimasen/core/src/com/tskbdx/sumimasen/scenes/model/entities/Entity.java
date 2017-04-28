package com.tskbdx.sumimasen.scenes.model.entities;

import com.badlogic.gdx.math.Rectangle;

import java.util.Observable;

/**
 * Created by Sydpy on 4/28/17.
 */
public abstract class Entity extends Observable {

    private Rectangle rectangle;

    public Rectangle getRectangle() {
        return rectangle;
    }
}
