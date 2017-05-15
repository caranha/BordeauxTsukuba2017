package com.tskbdx.sumimasen.scenes.model.entities.interactions;

/**
 * Created by viet khang on 08/05/2017.
 */

import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;

/**
 * Interaction is a callback always linked
 * with 2 (for the moment) entities : the active and
 * passive.
 * -> This dynamic behavior should be stored in the active.
 */
public abstract class Interaction {
    private Entity active;
    private Entity passive;

    private boolean started = false;
    private boolean finished = false;

    // The idea is :
    // An entity can't move during interaction
    // He recovers it movement back at the end
    // so we store it
    private Movement activeMovement;
    private Movement passiveMovement;

    Interaction() {}

    public void start(Entity active, Entity passive) {

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

        active.setInteractingWith(passive);
        passive.setInteractingWith(active);

        active.notifyObservers(getClass());
        passive.notifyObservers(getClass());

        started = true;
    }

    public abstract void update();

    public void end() {
        active.setInteracting(false);
        passive.setInteracting(false);

        active.setInteractingWith(null);
        passive.setInteractingWith(null);

        finished = true;

        active.setMovement(activeMovement);
        passive.setMovement(passiveMovement);
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isFinished() {
        return finished;
    }

    public Entity getActive() {
        return active;
    }

    public Entity getPassive() {
        return passive;
    }
}
