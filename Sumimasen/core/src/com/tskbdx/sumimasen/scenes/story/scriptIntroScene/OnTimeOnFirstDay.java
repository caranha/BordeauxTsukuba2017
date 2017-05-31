package com.tskbdx.sumimasen.scenes.story.scriptIntroScene;

import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.story.Event;
import com.tskbdx.sumimasen.scenes.story.State;

/*
 * Created by viet khang on 31/05/2017.
 */
class OnTimeOnFirstDay implements State {
    @Override
    public void process(Scene scene) {
        System.out.println("ON TIME");
    }

    @Override
    public State nextState(Event event) {
        return null;
    }
}
