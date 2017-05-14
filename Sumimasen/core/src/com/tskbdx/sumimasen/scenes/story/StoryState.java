package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;

import java.util.Map;

/*
 * Created by viet khang on 13/05/2017.
 */

/**
 * Story State is triggered by an interaction, via the StoryTeller.
 * Once triggered, it can make world modification with
 * <CODE>void process(World world)</CODE> method.
 *
 * Each state has to predefined its list of consecutive state.
 */
public interface StoryState {
    void process(World world);

    // TO DO, consider arguments in keys
    Map<Class<? extends Interaction>, StoryState> next();
    String description();
}

