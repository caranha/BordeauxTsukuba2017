package com.tskbdx.sumimasen.scenes.model.entities;

import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;

/**
 * Created by Sydpy on 4/27/17.
 */
public class SceneObject extends Entity {

    private Interaction interaction;

    public SceneObject(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public boolean isInteractable() {
        return interaction != null;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    public void doInteraction() {
        interaction.run();
    }
}
