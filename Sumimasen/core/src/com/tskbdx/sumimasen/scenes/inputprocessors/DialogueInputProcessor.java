package com.tskbdx.sumimasen.scenes.inputprocessors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.DialogueAnswer;

import java.util.*;
import java.util.List;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.tskbdx.sumimasen.Sumimasen.getFont;

/**
 * Created by Sydpy on 4/27/17.
 */

public class DialogueInputProcessor extends Stage {

    private List<TextButton> buttons = new ArrayList<>();
    private TextButtonStyle style = new TextButtonStyle();
    private final Dialogue dialogue;
    private boolean stopped = true;

    public DialogueInputProcessor(Dialogue dialogue) {
        this.dialogue = dialogue;
        configureStyle();

        // Create buttons
        List<DialogueAnswer> answers = dialogue.getCurrentExchange().getAnswers();
        for (int i = 0 ; i != answers.size() ; ++i) {
            TextButton button = new TextButton(answers.get(i).getText(), style);
            // Add a listener to the button.
            int finalI = i;
            button.addListener(new ChangeListener() {
                public void changed (ChangeEvent event, Actor actor) {
                    dialogue.pickAnswer(finalI);
                }
            });

            button.setSize(Gdx.graphics.getWidth() / answers.size(), Gdx.graphics.getHeight() * 0.3f);
            button.setPosition(i * button.getWidth(),0);
            buttons.add(button);
            addActor(button);
        }

        // Specify that the current gui is this to get drawn
        GameScreen.gui = this;
    }

    private void configureStyle() {
        style.fontColor = BLACK;
        style.font = getFont(20, "OpenSans");
    }

    @Override
    public void draw() {
        if (!stopped) {
            super.draw();
        }
    }

    public void update() {
        List<DialogueAnswer> answers = dialogue.getCurrentExchange().getAnswers();
        for (int i = 0 ; i != answers.size() ; ++i) {
            buttons.get(i).setText(answers.get(i).getText());
        }
    }

    public void stop() {
        stopped = true;
    }

    public void start() {
        stopped = false;
    }
}
