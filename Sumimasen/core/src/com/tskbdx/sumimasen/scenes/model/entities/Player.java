package com.tskbdx.sumimasen.scenes.model.entities;

import com.tskbdx.sumimasen.scenes.model.entities.movements.Walk;

import java.io.Serializable;

/*Z
 * Created by Sydpy on 4/27/17.
 */
public class Player extends Entity implements Serializable {

    private int kindness = 0, naiveness = 0;

    public Player() {
        setMovement(new Walk());
    }

    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
        setMovement(new Walk());
    }

    public void addKindness(int value) {
        kindness += value;
        setChanged();
    }

    public void addNaiveness(int value) {
        naiveness += value;
        setChanged();
    }
}
