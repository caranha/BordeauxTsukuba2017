package com.tskbdx.sumimasen.scenes.model.entities;


import java.util.Observable;

/**
 * Created by viet khang on 10/05/2017.
 */

public class Message extends Observable {
    private String content = "";
    private float duration;
    private float delay;
    private final Entity sender;
    private Entity receiver;

    Message(Entity entity) {
        sender = entity;
    }

    public int getLength() {
        return content.length();
    }

    public final String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? "" : content;
        setChanged();
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
        setChanged();
    }

    public final Entity getSender() {
        return sender;
    }

    public final Entity getReceiver() {
        return receiver;
    }

    public void setReceiver(Entity receiver) {
        this.receiver = receiver;
        setChanged();
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
        setChanged();
    }
}
