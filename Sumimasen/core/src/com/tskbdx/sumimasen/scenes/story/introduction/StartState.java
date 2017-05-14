package com.tskbdx.sumimasen.scenes.story.introduction;

import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.GetPickedUp;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;
import com.tskbdx.sumimasen.scenes.story.StoryState;

import java.util.HashMap;
import java.util.Map;

/*
 * Created by viet khang on 14/05/2017.
 */
public class StartState implements StoryState {

    private static final Map<Class<? extends Interaction>,
            StoryState> NEXT = new HashMap<>();
    static {
        NEXT.put(GetPickedUp.class, new NpcHappyState());
        NEXT.put(Dialogue.class, new PlayerLateState());
    }

    @Override
    public void process(World world) {
        System.out.println(description());
    }

    @Override
    public Map<Class<? extends Interaction>, StoryState> next() {
        return NEXT;
    }

    @Override
    public String description() {
        String description;
        description = "Welcome.\n" +
                "This is the starting stage.\n" +
                "Nothing to do here " +
                "because everything is already loaded.\n" +
                "You can take the present, so the state will change or " +
                "directly talk to the NPC.";
        return description;
    }
}
