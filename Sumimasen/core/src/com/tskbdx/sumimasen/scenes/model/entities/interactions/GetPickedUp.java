package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

/*
 * Created by viet khang on 13/05/2017.
 */

/**
 * GetPickedUp happens when active
 * is taken by passive.
 *
 * Result :
 * - active disappear from world
 * - passive stores a reference of it.
 */
public class GetPickedUp extends Interaction {
    public GetPickedUp(Entity producer, Entity consumer) {
        super(producer, consumer);
    }

    @Override
    public void update() {
        active.getWorld().removeEntity(active);
        passive.store(active);
        active.notifyObservers();
        passive.notifyObservers();
        end();
    }
}
