package com.tskbdx.sumimasen.scenes.model.entities.movements;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.io.Serializable;

/*
 * Created by viet khang on 08/05/2017.
 */

/**
 * Movement is a callback linked always linked
 * with an entity
 * Dynamically changing this entity movement
 * behavior can be very useful
 */
public interface Movement extends Serializable {
    void move(Entity entity);
}
