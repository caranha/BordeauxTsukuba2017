package com.tskbdx.sumimasen.scenes.model.entities;

import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;

/**
 * Created by Sydpy on 6/1/17.
 */
public class Sensor {

    private String name;
    private int x, y;
    private int width, height;

    private Interaction onCollide;

    public Sensor() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Interaction getOnCollide() {
        return onCollide;
    }

    public void setOnCollide(Interaction onCollide) {
        this.onCollide = onCollide;
    }
}
