package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/*
 * Created by viet khang on 13/05/2017.
 */

/**
 * StoryTeller will observe the player.
 * More accurately, it will look after its interaction
 * (for the moment only the type of the interaction)
 * and will change the story state.
 */
public final class StoryTeller implements Observer {

    private final World world;
    private StoryState state;

    public StoryTeller(World world, StoryState firstState) {
        System.out.println("(debug) I'm the story teller.\n" +
                "I observe Player interactions.\n" +
                "I'll change the current story state according to it,\n" +
                "it's the state itself that will change the model\n" +
                "according of its predefined behavior.");
        this.world = world;
        state = firstState;
        tellStory();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Class) {
            Class interactionClass = (Class) arg;
            if (Interaction.class.isAssignableFrom(interactionClass)) {
                System.out.println("\nInteraction : " + interactionClass.getSimpleName());
                if (changeState(interactionClass)) {
                    System.out.println("State changed to "
                            + state.getClass().getSimpleName() + "\n");
                    tellStory();
                }
            }
        }
    }

    private void tellStory() {
        System.out.println("Let's talk about " + state.getClass().getSimpleName() + " :\n");
        state.process(world);
    }

    private boolean changeState(Class interactionType) {
        Map<Class<? extends Interaction>, StoryState> nextState = state.next();
        if (nextState == null) {
            System.out.println("No more scene !");
        } else if (nextState.containsKey(interactionType)) {
            state = nextState.get(interactionType);
            return true;
        }
        return false;

    }
}
