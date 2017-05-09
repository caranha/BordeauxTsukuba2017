package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialog;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by viet khang on 09/05/2017.
 */

/**
 * When a entity talk, display a chat bubble
 */
class MessageRenderer implements Observer {
    private final Entity sender;
    private String current = "";
    private float duration = 0.f;
    private Entity receiver;

    MessageRenderer(Entity sender) {
        this.sender = sender;
        sender.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        String message = sender.getMessage();
        if (current != null && ! current.equals(message)) {
            current =  message;
            duration = sender.getMessageDuration();
            receiver = sender.getMessageReceiver();
            System.out.println(message);
        }
    }

    public void render(Batch batch) {
    }
}
