package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.tskbdx.sumimasen.scenes.model.entities.Entity;

/*
 * Created by viet khang on 31/05/2017.
 */
public class TriggerThought extends Interaction {
    private final String content;

    public TriggerThought(String content) {
        this.content = content;
    }

    @Override
    public void start(Entity active, Entity passive) {
        super.start(active, passive);
        passive.think(content);
        end();
    }

    @Override
    public void end() {
        super.end();
    }
}
