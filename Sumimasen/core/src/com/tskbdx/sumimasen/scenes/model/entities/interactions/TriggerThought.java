package com.tskbdx.sumimasen.scenes.model.entities.interactions;

/*
 * Created by viet khang on 31/05/2017.
 */
public class TriggerThought extends Interaction {
    private final String content;

    public TriggerThought(String content) {
        this.content = content;
    }

    @Override
    protected void run() {
        passive.think(content);

        end();
    }
}
