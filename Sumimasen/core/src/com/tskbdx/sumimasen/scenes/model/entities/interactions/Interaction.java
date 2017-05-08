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
public abstract class Interaction implements Runnable {
    final protected Entity active;
    final protected Entity passive;

    Interaction(Entity producer, Entity consumer) {
        this.active = producer;
        this.passive = consumer;
    }
}
