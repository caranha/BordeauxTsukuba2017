package com.tskbdx.sumimasen.scenes.model.entities.movements;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sydpy on 5/16/17.
 */
public class MovementResult {

    private List<Entity> entitiesAround = new ArrayList<>();

    public void addEntityAround(Entity entity) {
        if (entity != null) entitiesAround.add(entity);
    }

    public List<Entity> getEntitiesAround() {
        return entitiesAround;
    }
}
