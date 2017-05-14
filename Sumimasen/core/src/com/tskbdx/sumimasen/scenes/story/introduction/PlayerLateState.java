package com.tskbdx.sumimasen.scenes.story.introduction;

import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;
import com.tskbdx.sumimasen.scenes.story.StoryState;

import java.util.HashMap;
import java.util.Map;

/*
 * Created by viet khang on 14/05/2017.
 */
public class PlayerLateState implements StoryState {

    @Override
    public void process(World world) {
    }

    @Override
    public Map<Class<? extends Interaction>, StoryState> next() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }
}
