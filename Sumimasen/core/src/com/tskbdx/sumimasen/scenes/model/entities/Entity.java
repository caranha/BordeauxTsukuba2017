package com.tskbdx.sumimasen.scenes.model.entities;

import com.badlogic.gdx.math.Rectangle;

import java.util.Observable;

/**
 * Created by Sydpy on 4/28/17.
 */
public abstract class Entity extends Observable {

    private Rectangle rectangle;

    public abstract void update(float dt);

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
        setChanged();
        notifyObservers();
    }

    public float getX() {
        return rectangle.getX();
    }

    public float getY() {
        return rectangle.getY();
    }

    public float getWidth() {
        return rectangle.getWidth();
    }

    public float getHeight() {
        return rectangle.getHeight();
    }
}
