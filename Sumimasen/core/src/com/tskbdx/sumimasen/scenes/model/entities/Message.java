package com.tskbdx.sumimasen.scenes.model.entities;


import java.io.Serializable;
import java.util.Observable;

/**
 * Created by viet khang on 10/05/2017.
 */

public class Message extends Observable {
    private String content = "";
    private float timeToUnderstand;
    private float timeToAnswer;
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

    public float getTimeToUnderstand() {
        return timeToUnderstand;
    }

    public void setTimeToUnderstand(float timeToUnderstand) {
        this.timeToUnderstand = timeToUnderstand;
        setChanged();
    }

    public float getTimeToAnswer() {
        return timeToAnswer;
    }

    public void setTimeToAnswer(float timeToAnswer) {
        this.timeToAnswer = timeToAnswer;
        setChanged();
    }

    public float getTotalDuration() {
        return timeToAnswer + timeToUnderstand;
    }
}
