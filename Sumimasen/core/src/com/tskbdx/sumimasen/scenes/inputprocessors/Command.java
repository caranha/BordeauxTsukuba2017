package com.tskbdx.sumimasen.scenes.inputprocessors;

/**
 * Created by viet khang on 01/05/2017.
 */

import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Player;

/**
 * Command :
 * Trigger an event at a later time.
 */
interface Command {
    /**
     * Command execution
     */
    void apply();
}