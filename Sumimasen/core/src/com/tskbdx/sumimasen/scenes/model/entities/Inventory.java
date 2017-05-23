package com.tskbdx.sumimasen.scenes.model.entities;

import java.util.*;

/*
 * Created by viet khang on 15/05/2017.
 */
public class Inventory extends Observable {
    private Set<Entity> objects = new HashSet<>();

    void store(Entity object) {
        if (objects.add(object)) {
            setChanged();
            notifyObservers();
        }
    }

    void remove(Entity object) {
        if (objects.remove(object)) {
            setChanged();
            notifyObservers();
        }
    }

    boolean contains(Entity entity) {
        return objects.add(entity);
    }

    public final Set<Entity> getObjects() {
        return objects;
    }
}
