package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.scenes.Scene;

/*
 * Created by viet khang on 23/05/2017.
 */

/**
 * Base interface for each Story State.
 * A state can modify its context with +process(Scene scene).
 * It knows its successor according to an Event.
 * @see Event
 */
public interface State {
    void process(Scene scene);
    State nextState(Event event);
}
