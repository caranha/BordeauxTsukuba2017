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
    private static Scene scene; // Story can modify a context scene,
    private static State state; // according to a specific state.

    /**
     * Don't instance a story, just use its public methods.
     */
    private Story() {
    }

    /**
     * To call first and to recall if the scene has changed.
     */
    public static void setScene(Scene scene) {
        Story.scene = scene;
        changeState(getFirstState(scene));
    }

    /**
     * To call at the start of every Model interaction.
     * An interaction is an event which can trigger an new state.
     * @see State
     * @see Event
     */
    public static void update(Interaction interaction, Entity active, Entity passive) {
        assert scene != null : "Call Story::setScene first";

        State nextState = state.nextState(new Event(interaction, active, passive));
        if (nextState != null) {
            changeState(nextState);
        }
    }

    /**
     * Change state update the current state and make it process.
     */
    private static void changeState(State nextState) {
        state = nextState;
        state.process(scene);
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
}
