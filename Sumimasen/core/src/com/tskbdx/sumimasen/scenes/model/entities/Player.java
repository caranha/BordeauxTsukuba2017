package com.tskbdx.sumimasen.scenes.model.entities;

import com.tskbdx.sumimasen.scenes.model.entities.movements.Path;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Walk;

import static com.tskbdx.sumimasen.scenes.model.entities.Direction.*;

/**
 * Created by Sydpy on 4/27/17.
 */
public class Player extends Entity {

    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
        setMovement(new Walk(this));
    }

}
