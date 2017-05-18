package com.tskbdx.sumimasen.scenes.story.introduction;

import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.GetPickedUp;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;
import com.tskbdx.sumimasen.scenes.story.StoryState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        List<Entity> entitiesByName = world.getEntitiesByName("entity");

        // Let the game crash if we can't find the entity
        Entity entity = entitiesByName.get(0);
        entity.setInteraction(new GetPickedUp());
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
