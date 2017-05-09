package com.tskbdx.sumimasen.scenes.model.entities.interactions;

/**
 * Created by viet khang on 08/05/2017.
 */

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

/**
 * Interaction is a callback always linked
 * with 2 (for the moment) entities : the active and
 * passive.
 * -> This dynamic behavior should be stored in the active.
 */
public abstract class Interaction {
    final protected Entity active;
    final protected Entity passive;

    private boolean started = false;
    private boolean finished = false;

    Interaction(Entity producer, Entity consumer) {
        this.active = producer;
        this.passive = consumer;
    }

    public void start() {
        active.setInteracting(true);
        passive.setInteracting(true);

        active.setInteractingWith(passive);
        passive.setInteractingWith(active);

        started = true;
    }

    public abstract void update();

    public void end() {
        active.setInteracting(false);
        passive.setInteracting(false);

        active.setInteractingWith(null);
        passive.setInteractingWith(null);

        finished = true;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isFinished() {
        return finished;
    }
}
