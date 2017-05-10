package com.tskbdx.sumimasen.scenes.model.entities.interactions;

/**
 * Created by viet khang on 08/05/2017.
 */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.inputprocessors.BasicInputProcessor;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Walk;

/**
 * Interaction is a callback always linked
 * with 2 (for the moment) entities : the active and
 * passive.
 * -> This dynamic behavior should be stored in the active.
 */
public abstract class Interaction {
    final protected Entity active;
    final protected Entity passive;

    private boolean started = false;
    private boolean finished = false;

    // The idea is :
    // An entity can't move during interaction
    // He recovers it movement back at the end
    // so we store it
    private Movement activeMovement;
    private Movement passiveMovement;

    Interaction(Entity producer, Entity consumer) {
        this.active = producer;
        this.passive = consumer;
    }

    public void start() {
        active.setInteracting(true);
        passive.setInteracting(true);

        activeMovement = active.getMovement();
        passiveMovement = passive.getMovement();

        active.setMovement(null);
        passive.setMovement(null);

        active.setInteractingWith(passive);
        passive.setInteractingWith(active);

        started = true;

        System.out.println("Start of interaction");
    }

    public abstract void update();

    public void end() {
        active.setInteracting(false);
        passive.setInteracting(false);

        active.setInteractingWith(null);
        passive.setInteractingWith(null);

        finished = true;

        active.setMovement(activeMovement);
        passive.setMovement(passiveMovement);

        System.out.println("End of interaction");
        GameScreen.gui = null;
        Gdx.input.setInputProcessor(new BasicInputProcessor());

        active.nextInteraction();
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isFinished() {
        return finished;
    }
}
