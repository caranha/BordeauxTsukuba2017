package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.scenes.Scene;

/*
 * Created by viet khang on 23/05/2017.
 */
public interface State {
    void process(Scene scene);
    State nextState(Event event);
}
