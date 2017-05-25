package com.tskbdx.sumimasen;

import com.badlogic.gdx.Game;
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

import static com.tskbdx.sumimasen.Sumimasen.getAssetManager;

/*
 * Created by viet khang on 25/05/2017.
 */
final class SavedGamesScreen extends Stage implements Screen {

    private final Screen previousScreen;
    private final Game game;
    private final List<Button> buttons = new LinkedList<>();

    SavedGamesScreen(Game game, Screen previousScreen) {
        this.previousScreen = previousScreen;
        this.game = game;
        String[] savedGames = getSavedGames();
        for (String savedGame : savedGames) {
            addButton(createButtonFor(savedGame));
        }
        addActor(createBackButton());
        layoutButtons();
    }

    private Button createButtonFor(String savedGame) {
        Button button = new TextButton(savedGame,
                getAssetManager().get("skin/skin/cloud-form-ui.json",
                        Skin.class));
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                goTo(savedGame);
            }
        });
        return button;
    }

    private void goTo(String savedGame) {
        System.out.println("loading " + savedGame);
    }

    private void addButton(Button backButton) {
        addActor(backButton);
        buttons.add(backButton);
    }

    private Button createBackButton() {
        Button button = new TextButton("Back",
                getAssetManager().get(
                        "skin/skin/cloud-form-ui.json", Skin.class));
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(previousScreen);
            }
        });

        // layout
        final float windowWidth = Gdx.graphics.getWidth(),
                windowHeight = Gdx.graphics.getHeight();
        button.setSize(windowWidth * .3f, windowHeight * .2f);
        button.setPosition((windowWidth - button.getWidth()) * .5f, 22.f);
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

    private void layoutButtons() {
        final float windowWidth = Gdx.graphics.getWidth(),
                windowHeight = Gdx.graphics.getHeight(),
                bottomPadding = 50f,
                topPadding = 150f;
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(buttons.size() - i - 1);
            button.setSize(windowWidth * 0.3f,
                    windowHeight * 0.14f);
            button.setPosition((windowWidth - button.getWidth()) * .5f,
                    ((windowHeight - topPadding - bottomPadding) / buttons.size()) * i +
                            ((windowHeight - topPadding - bottomPadding) /
                                    buttons.size() - button.getHeight()) * .5f
                            + topPadding);
        }
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

    private String[] getSavedGames() {
        return new String[]{"A", "B", "C"};
    }
}
