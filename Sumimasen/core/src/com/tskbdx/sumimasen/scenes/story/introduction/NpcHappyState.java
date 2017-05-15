package com.tskbdx.sumimasen.scenes.story.introduction;

import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.GetPickedUp;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;
import com.tskbdx.sumimasen.scenes.story.StoryState;

import java.util.HashMap;
import java.util.Map;

import static com.tskbdx.sumimasen.GameScreen.getPlayer;

/*
 * Created by viet khang on 14/05/2017.
 */
public class NpcHappyState implements StoryState {

    private static final Map<Class<? extends Interaction>,StoryState> NEXT = new HashMap<>();
    static {
        NEXT.put(GetPickedUp.class, new NpcDisappearState());
    }

    @Override
    public void process(World world) {
        Entity entity = world.getEntityByName("entity");
        assert entity != null;
        entity.setInteraction(new GetPickedUp(entity, getPlayer()));
    }

    public Map<Class<? extends Interaction>, StoryState> next() {
        return NEXT;
    }

    @Override
    public String description() {
        String description;
        description = "You decided to take the present.\n" +
                "The NPC will be happy.\n" +
                "Interact with him !";
        return description;
    }
}
