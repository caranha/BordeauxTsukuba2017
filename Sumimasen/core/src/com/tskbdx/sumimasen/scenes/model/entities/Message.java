package com.tskbdx.sumimasen.scenes.model.entities;


import java.io.Serializable;
import java.util.Observable;

/*
 * Created by viet khang on 10/05/2017.
 */

public final class Message extends Observable implements Serializable {
    private String content = "";
    private float timeToUnderstand;
    private float timeToAnswer;
    private final Entity sender;
    private Entity receiver;
    private boolean important = false;

    Message(Entity entity) {
        sender = entity;
    }

    public final String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content == null ? "" : content;
        setChanged();
    }

    public final Entity getSender() {
        return sender;
    }

    public final Entity getReceiver() {
        return receiver;
    }

    void setReceiver(Entity receiver) {
        this.receiver = receiver;
        setChanged();
    }

    public float getTimeToUnderstand() {
        return timeToUnderstand;
    }

    void setTimeToUnderstand(float timeToUnderstand) {
        this.timeToUnderstand = timeToUnderstand;
        setChanged();
    }

    public float getTimeToAnswer() {
        return timeToAnswer;
    }

    void setTimeToAnswer(float timeToAnswer) {
        this.timeToAnswer = timeToAnswer;
        setChanged();
    }

    float getTotalDuration() {
        return timeToAnswer + timeToUnderstand;
    }


    void setImportant(boolean important) {
        this.important = important;
        setChanged();
    }

    public boolean isImportant() {
        return important;
    }
}
