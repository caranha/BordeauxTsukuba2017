package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

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
    BitmapFont font = new BitmapFont();

    MessageRenderer(Entity sender) {
        /**
         * TO DO : scale with Gdx FreeTypeFont
         */
        font.getData().setScale(1.f/4, 1.f/4);
        this.sender = sender;
        sender.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        String message = sender.getMessage();
        if (message != null && ! current.equals(message)) {
            current =  message;
            duration = sender.getMessageDuration();
            receiver = sender.getMessageReceiver();
        }
    }

    public void render(Batch batch, float x, float y) {
        if (current != null) {
            font.draw(batch, current, x, y);
        }
    }
}
