package com.tskbdx.sumimasen.scenes.model.entities;

import com.tskbdx.sumimasen.scenes.model.entities.movements.Walk;

/*Z
 * Created by Sydpy on 4/27/17.
 */
public class Player extends Entity {

    public Player() {
        setMovement(new Walk());
    }

    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
        setMovement(new Walk());
    }

    public void inventoryRandomRemove() {
        getInventory().randomRemove();
    }
}
