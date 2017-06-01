package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;

/*
 * Created by viet khang on 23/05/2017.
 */

/**
 * Encapsulating class that gather necessary properties
 * from an Interaction instance.
 */
final class Event {

    final private Class<? extends Interaction> interactionType;
    final private String activeName;
    final private String passiveName;

    Event(Interaction interaction,
          Entity active,
          Entity passive) {
        interactionType = interaction.getClass();
        activeName = active.getName();
        passiveName = passive.getName();
    }

    public boolean is(Class interactionType,
                      String activeName, String passiveName) {
        return this.interactionType.equals(interactionType)
                && this.activeName.equals(activeName)
                && this.passiveName.equals(passiveName);
    }

    public boolean is(Class interactionType,
                      String activeName) {
        return is(interactionType, activeName, "player");
    }

}
