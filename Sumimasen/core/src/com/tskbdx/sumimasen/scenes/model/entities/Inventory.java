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

    public final Set<Entity> getObjects() {
        return objects;
    }

    void randomRemove() { //testing remove (press M)
        try {
            int random = new Random().nextInt(objects.size()), index = 0;
            for (Entity object: objects) {
                if (random == index++) {
                    objects.remove(object);
                    setChanged();
                    break;
                }
            }
            notifyObservers();
        } catch (Exception ignored) {}
    }
}
