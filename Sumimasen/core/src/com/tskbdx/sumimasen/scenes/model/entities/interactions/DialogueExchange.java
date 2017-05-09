package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sydpy on 5/9/17.
 */
public class DialogueExchange {

    private String text ="";
    private List<DialogueAnswer> answers = new ArrayList<>();

    public DialogueExchange() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<DialogueAnswer> getAnswers() {
        return answers;
    }

    public void addAnswer(DialogueAnswer answer) {
        answers.add(answer);
    }

    public void removeAnswer(DialogueAnswer answer) {
        answers.remove(answer);
    }
}
