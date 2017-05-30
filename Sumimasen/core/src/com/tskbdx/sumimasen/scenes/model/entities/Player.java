package com.tskbdx.sumimasen.scenes.model.entities;

import com.tskbdx.sumimasen.scenes.model.entities.movements.Walk;

import java.io.Serializable;

/*Z
 * Created by Sydpy on 4/27/17.
 */
public class Player extends Entity implements Serializable {

    private int kindness = 0, naiveness = 0;

    public Player() {
        super();
        setMovement(new Walk());
        setName("player");
        setWidth(2);
        setHeight(1);
    }
}
