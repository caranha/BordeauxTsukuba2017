package com.tskbdx.sumimasen.scenes.model.entities;

import java.util.*;

/*
 * Created by viet khang on 15/05/2017.
 */
public class Inventory extends Observable {
    private Set<SceneObject> objects = new HashSet<>();

    void store(SceneObject object) {
        if (objects.add(object)) {
            setChanged();
            notifyObservers();
        }
    }

    void remove(SceneObject object) {
        if (objects.remove(object)) {
            setChanged();
            notifyObservers();
        }
    }

    public final Set<SceneObject> getObjects() {
        return objects;
    }
}