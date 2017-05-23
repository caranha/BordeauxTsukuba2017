package com.tskbdx.sumimasen.scenes.story.scriptIntroScene;

import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.ChangeMap;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Teleport;
import com.tskbdx.sumimasen.scenes.story.Event;
import com.tskbdx.sumimasen.scenes.story.State;

/*
 * Created by viet khang on 23/05/2017.
 */
public class FirstState implements State {
    @Override
    public void process(Scene scene) {

    }

    @Override
    public State nextState(Event event) {
        if (event.is(ChangeMap.class, "Lab")) {
            return new MeetNoname();
        }

        // for test
        if (event.is(Teleport.class, "sensor")) {
            return new AfterTeleport();
        }

        return null;
    }
}
