package com.tskbdx.sumimasen.scenes.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.tskbdx.sumimasen.Sumimasen;
import com.tskbdx.sumimasen.scenes.inputprocessors.GameCommands;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue.DialogueAnswer;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue.DialogueExchange;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/*
 * Created by viet khang on 15/05/2017.
 */

/**
 * Observers an entity and display
 * text buttons while it is answering
 * <p>
 * Act as an Gdx Scene2D actor, thus
 * handle on click event.
 */
final class AnswersSelector extends Group implements Observer {

    private final Entity entity;
    private final Skin skin = Sumimasen.getAssetManager().get(
            "skin/skin/cloud-form-ui.json",
            Skin.class);
    private final UserInterface userInterface;

    AnswersSelector(Entity entity, UserInterface userInterface) {
        this.entity = entity;
        entity.addObserver(this);

        userInterface.addActor(this);
        this.userInterface = userInterface;
    }

    @Override
    public void update(Observable o, Object arg) {
        Dialogue dialogue = arg instanceof Dialogue ? (Dialogue) arg : null;
        if (entity.isInteracting() && dialogue != null) {
            setAnswersButtons(dialogue);
            Gdx.input.setInputProcessor(userInterface);
        } else {
            for (Actor actor : getChildren()) {
                actor.setVisible(false);
            }
            getChildren().clear();
            Gdx.input.setInputProcessor(new GameCommands());
        }
    }

    private void setAnswersButtons(Dialogue dialogue) {
        DialogueExchange exchange = dialogue.getCurrentExchange();
        List<DialogueAnswer> answers = exchange.getAnswers();
        for (int i = 0; i != answers.size(); ++i) {
            Button button;
            final int finalI = i;
            String text = answers.get(i).getIdea();
            button = new TextButton(text, skin);
            addActor(button);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    dialogue.pickAnswer(finalI);
                }
            });
            button.setSize(Gdx.graphics.getWidth() / answers.size(), Gdx.graphics.getHeight() * 0.1f);
            button.setPosition(i * button.getWidth(), 0);
            button.setVisible(true);
        }
    }
}
