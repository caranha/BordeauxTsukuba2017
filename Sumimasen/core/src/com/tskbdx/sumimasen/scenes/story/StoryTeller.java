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
        this.world = world;
        state = firstState;
        tellStory();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Class) {
            Class interactionClass = (Class) arg;
            if (Interaction.class.isAssignableFrom(interactionClass)) {
                if (changeState(interactionClass)) {
                    tellStory();
                }
            }
        }
    }

    private void tellStory() {
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
