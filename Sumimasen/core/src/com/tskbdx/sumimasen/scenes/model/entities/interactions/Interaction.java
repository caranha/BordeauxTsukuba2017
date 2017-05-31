package com.tskbdx.sumimasen.scenes.model.entities.interactions;

/**
 * Created by viet khang on 08/05/2017.
 */

import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;
import com.tskbdx.sumimasen.scenes.story.Story;

import java.io.Serializable;

/**
 * Interaction is a callback always linked
 * with 2 (for the moment) entities : the active and
 * passive.
 * -> This dynamic behavior should be stored in the active.
 */
public abstract class Interaction implements Serializable {
    private Entity active;
    private Entity passive;

    // The idea is :
    // An entity can't move during interaction
    // He recovers it movement back at the end
    // so we store it
    private Movement activeMovement;
    private Movement passiveMovement;

    Interaction() {}

    public void start(Entity active, Entity passive) {

        System.out.println("starting with " + active.getName() + " " + passive.getName());
        this.active = active;
        this.passive = passive;

        active.setInteracting(true);
        passive.setInteracting(true);

        active.setInteraction(this);
        passive.setInteraction(this);

        activeMovement = active.getMovement();
        passiveMovement = passive.getMovement();

        active.setMovement(null);
        passive.setMovement(null);

        active.notifyObservers(getClass());
        passive.notifyObservers(getClass());
    }

    public void end() {
        active.setInteracting(false);
        passive.setInteracting(false);

        active.setMovement(activeMovement);
        passive.setMovement(passiveMovement);

        Story.getInstance().update(this, active, passive);
    }

    public final Entity getActive() {
        return active;
    }

    public final Entity getPassive() {
        return passive;
    }
}
