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

    public static MovementResult computeMovementResult(Entity entity) {

        MovementResult mvtResult = new MovementResult();
        mvtResult.addEntityAround(entity.getWorld().getEntity(entity.getX(), entity.getY() - 1));
        mvtResult.addEntityAround(entity.getWorld().getEntity(entity.getX() - 1, entity.getY()));
        mvtResult.addEntityAround(entity.getWorld().getEntity(entity.getX(), entity.getY() + entity.getHeight()));
        mvtResult.addEntityAround(entity.getWorld().getEntity(entity.getX() + entity.getWidth(), entity.getY()));

        return mvtResult;
    }
}
