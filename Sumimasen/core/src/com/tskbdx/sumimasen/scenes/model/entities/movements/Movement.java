package com.tskbdx.sumimasen.scenes.model.entities.movements;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

/**
 * Created by viet khang on 08/05/2017.
 */

/**
 * Movement is a callback linked always linked
 * with an entity
 * Dynamically changing this entity movement
 * behavior can be very useful
 */
public abstract class Movement implements Runnable {
    final protected Entity entity;

    Movement(Entity entity) {
        this.entity = entity;
    }
}
