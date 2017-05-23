package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;

/*
 * Created by viet khang on 23/05/2017.
 */
final public class Story {
    private Story() {}
    private static Scene scene;
    private static State state;

    public static void setScene(Scene scene) {
        Story.scene = scene;
        changeState(getFirstState(scene));
    }

    public static void update(Interaction interaction, Entity active, Entity passive) {
        assert scene != null : "Call Story::setScene first";

        State nextState = state.nextState(new Event(interaction, active, passive));
        if (nextState != null) {
            changeState(nextState);
        }
    }

    private static void changeState(State nextState) {
        state = nextState;
        state.process(scene);
    }

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
