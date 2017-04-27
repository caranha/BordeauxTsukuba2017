package com.tskbdx.sumimasen.scenes.model.entities;

import com.badlogic.gdx.math.Vector2;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;
import com.tskbdx.sumimasen.scenes.model.entities.mouvements.Mouvement;

/**
 * Created by Sydpy on 4/27/17.
 */
public abstract class Entity {

    private Vector2 pos, size;
    private Vector2 hitBoxPos, hitBoxSize;

    private Interaction interaction;
    private Mouvement mouvement;

}
