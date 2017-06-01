package com.tskbdx.sumimasen.scenes.model.entities.interactions;

/**
 * Created by viet khang on 08/05/2017.
 */

import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;

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
    private Direction activeDirection;
    private Runnable onFinished;

    Interaction() {
    }

    public void start(Entity active, Entity passive) {
        this.active = active;
        this.passive = passive;

        activeDirection = active.getLastDirection();
        // change target direction to face the passive
        active.setDirection(
                Direction.getOpposite(passive.getLastDirection()));

        active.notifyObservers();

        System.out.println("start -> " + active.getDirection() + " " + activeDirection);

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

        active.setDirection(activeDirection);
        active.notifyObservers();

        active.addInteracted(passive.getName());
        passive.addInteracted(active.getName());

        if (onFinished != null) {
            onFinished.run();
        }
    }

    final Entity getActive() {
        return active;
    }

    final Entity getPassive() {
        return passive;
    }

    public void setOnFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }
}
