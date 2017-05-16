package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

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

    public GetPickedUp() {
        super();
    }

    @Override
    public void start(Entity active, Entity passive) {
        super.start(active, passive);
        getPassive().store(getActive());
        getActive().getWorld().removeEntity(getActive());
        getPassive().notifyObservers(getActive());
        getActive().notifyObservers();
        end();
    }
}
