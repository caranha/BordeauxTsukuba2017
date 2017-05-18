package com.tskbdx.sumimasen.scenes.model.entities.interactions;

/**
 * Created by Sydpy on 5/17/17.
 */
public class ChangeMap extends Interaction {

    public static final float DELAY = 2.f;

    private String mapName;

    public ChangeMap(String mapName) {
        this.mapName = mapName;
    }


}
