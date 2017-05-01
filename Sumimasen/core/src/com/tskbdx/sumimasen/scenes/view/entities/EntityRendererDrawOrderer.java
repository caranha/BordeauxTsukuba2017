package com.tskbdx.sumimasen.scenes.view.entities;

import java.util.Comparator;

/**
 * Created by Sydpy on 4/28/17.
 */
public class EntityRendererDrawOrderer implements Comparator<EntityRenderer> {
    @Override
    public int compare(EntityRenderer t1, EntityRenderer t2) {
        return (int) (t2.getEntity().getRectangle().getY() - t1.getEntity().getRectangle().getY());
    }
}
