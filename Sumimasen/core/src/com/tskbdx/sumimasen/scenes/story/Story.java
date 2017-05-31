package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;

/*
 * Created by viet khang on 23/05/2017.
 */

/**
 * Story is designed to menage Story events and Story current state.
 *
 * @see Event
 * @see State
 */
final public class Story {
    private static Story instance;
    private Scene scene; // Story can modify a context scene,
    private State state; // according to a specific state.

    /**
     * Story is now a singleton.
     */
    private Story() {
    }

    public static Story getInstance() {
        if (instance == null) {
            instance = new Story();
        }
        return instance;
    }

    public static String getSceneName() {
        assert instance != null : "story isn't set up with a scene";
        return instance.getClass().getSimpleName();
    }

    /**
     * Try to instance a class named FirstState in a subpackage
     * "script{SceneClassName}".
     */
    private static State getFirstState(Scene scene) {
        String name = Story.class.getPackage().getName() +
                ".script" + scene.getClass().getSimpleName() + ".FirstState";
        try {
            return (State) Class.forName(name).newInstance();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Does " + name + " exist ?");
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Is " + name + " constructor public ?");
        } catch (InstantiationException e) {
            throw new IllegalStateException("Is " + name + " concrete ?");
        }
    }

    /**
     * To call first and to recall if the scene has changed.
     */
    public void setScene(Scene scene) {
        this.scene = scene;
        changeState(getFirstState(scene));
    }

    /**
     * To call at the end of every Model interaction.
     * An interaction is an event which can trigger an new state.
     *ddddddddd
     * @see State
     * @see Event
     */
    public void update(Interaction interaction, Entity active, Entity passive) {
        assert scene != null : "Call Story::setScene first";

        State nextState = state.nextState(new Event(interaction, active, passive));
        if (nextState != null) {
            changeState(nextState);
        }
    }

    /**
     * Change state init the current state and make it process.
     */
    private void changeState(State nextState) {
        state = nextState;
        state.process(scene);
    }
}
