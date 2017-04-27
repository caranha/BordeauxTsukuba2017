package com.tskbdx.sumimasen.scenes.model.entities;

import com.badlogic.gdx.math.Rectangle;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;
import com.tskbdx.sumimasen.scenes.model.entities.mouvements.Mouvement;

/**
 * Created by Sydpy on 4/27/17.
 */
public abstract class Entity {

    private Rectangle rectangle;

    private Interaction interaction;
    private Mouvement mouvement;

}
