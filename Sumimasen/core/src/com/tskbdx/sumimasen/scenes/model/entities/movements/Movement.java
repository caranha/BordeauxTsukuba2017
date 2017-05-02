package com.tskbdx.sumimasen.scenes.model.entities.movements;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

/**
 * Created by Sydpy on 4/27/17.
 */
public interface Movement {
    void move(Entity entity, float dt);
}
