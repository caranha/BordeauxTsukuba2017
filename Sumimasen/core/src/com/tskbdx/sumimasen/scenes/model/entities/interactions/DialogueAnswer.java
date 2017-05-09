package com.tskbdx.sumimasen.scenes.model.entities.interactions;

/**
 * Created by Sydpy on 5/9/17.
 */
public class DialogueAnswer {

    String text = "";
    Integer nextExchange;

    public DialogueAnswer() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getNextExchange() {
        return nextExchange;
    }

    public void setNextExchange(int nextExchange) {
        this.nextExchange = nextExchange;
    }
}
