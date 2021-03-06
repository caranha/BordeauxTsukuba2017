package com.tskbdx.sumimasen.scenes.model.entities;

import java.io.Serializable;

/**
 * Created by Sydpy on 5/26/17.
 */
public class Spawn implements Serializable {

    private String name;
    private int x, y;

    public Spawn(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
