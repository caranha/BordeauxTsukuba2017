package com.tskbdx.sumimasen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.LinkedList;
import java.util.List;

/*
 * Created by viet khang on 24/05/2017.
 */
final class MenuScreen extends Stage implements Screen {

    private final Sumimasen game;
    private final List<Button> buttons = new LinkedList<>();

    MenuScreen(Sumimasen game) {
        this.game = game;

        if (hasSavedGame()) {
            addButton(createContinueButton());
        }
        addButton(createNewGameButton());
        layoutButtons();
    }

    private void layoutButtons() {
        final float windowWidth = Gdx.graphics.getWidth(),
                windowHeight = Gdx.graphics.getHeight(),
                verticalPadding = 50f;
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(buttons.size() - i - 1);
            button.setSize(windowWidth * 0.3f,
                    windowHeight * 0.2f);
            button.setPosition((windowWidth - button.getWidth()) * .5f,
                    ((windowHeight - (verticalPadding * 2f)) / buttons.size()) * i +
                            ((windowHeight - (verticalPadding * 2f)) /
                                    buttons.size() - button.getHeight()) * .5f
                            + verticalPadding);
        }
    }

    private void addButton(Button button) {
        buttons.add(button);
        addActor(button);
    }

    private Button createNewGameButton() {
        TextButton button = new TextButton("New game",
                Sumimasen.getAssetManager().get("skin/skin/cloud-form-ui.json", Skin.class));
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goToGameScreen();
            }
        });
        return button;
    }

    private Button createContinueButton() {
        TextButton button = new TextButton("Continue",
                Sumimasen.getAssetManager().get("skin/skin/cloud-form-ui.json", Skin.class));
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameScreen gameScreen = new GameScreen(game, true);
                game.setScreen(gameScreen);
            }
        });
        return button;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        act(delta);
        draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private boolean hasSavedGame() {
        return true;
    }

    private void goToGameScreen() {
        game.setScreen(new GameScreen(game));
    }
}
