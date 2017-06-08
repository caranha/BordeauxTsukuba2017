package com.tskbdx.sumimasen.scenes.model.entities.interactions;

/**
 * Created by viet khang on 08/05/2017.
 */

import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Sensor;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;

import java.io.Serializable;

/**
 * Interaction is a callback always linked
 * with 2 (for the moment) entities : the active and
 * passive.
 * -> This dynamic behavior should be stored in the active.
 */
public abstract class Interaction implements Serializable {

    protected Sensor sensor;
    protected Entity active;
    protected Entity passive;

    // The idea is :
    // An entity can't move during interaction
    // He recovers its movement back at the end
    // so we store it
    private Movement activeMovement;
    private Movement passiveMovement;
    private Direction activeDirection;
    private Runnable onFinished;

    Interaction() {
    }

    public final void start(Entity active, Entity passive) {
        this.active = active;
        this.passive = passive;

        activeDirection = active.getLastDirection();
        // change target direction to face the passive
        active.setDirection(
                passive.getLastDirection().getOpposite());

        active.notifyObservers();

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

        run();
    }

    public void start(Sensor sensor, Entity passive){

        this.sensor     = sensor;
        this.passive    = passive;

        passiveMovement = passive.getMovement();
        passive.setInteracting(true);
        passive.setInteraction(this);
        passive.setMovement(null);
        passive.notifyObservers(getClass());


        run();
    }

    protected abstract void run();

    protected void end() {

        if (active != null) {
            active.setInteracting(false);

            active.setMovement(activeMovement);

            active.setDirection(activeDirection);
            active.notifyObservers();

            active.addInteracted(passive.getName());
            passive.addInteracted(active.getName());

        }

        if (sensor != null) {
            passive.addInteracted(sensor.getName());
        }

        passive.setInteracting(false);
        passive.setMovement(passiveMovement);
        passive.notifyObservers();

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
