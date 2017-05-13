package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Player;
import com.tskbdx.sumimasen.scenes.model.entities.SceneObject;

/*
 * Created by viet khang on 13/05/2017.
 */

/**
 * GetPickedUp happens when active
 * is taken by passive.
 *
 * The passive is necessary the player
 * (the entity who picks up)
 * The active is a scene object
 *
 * Result :
 * - active disappear from world
 * - passive stores a reference of it in its inventory
 */
public class GetPickedUp extends Interaction {

    public GetPickedUp(Entity active, Player passive) {
        super(active, passive);
    }

    @Override
    public void update() {
        passive.store(active);
        active.getWorld().removeEntity(active);
        passive.notifyObservers(active);
        active.notifyObservers();
        end();
    }
}
