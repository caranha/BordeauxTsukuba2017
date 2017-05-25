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

import static com.tskbdx.sumimasen.Sumimasen.getAssetManager;

/*
 * Created by viet khang on 25/05/2017.
 */
final class SavedGamesScreen extends Stage implements Screen {

    private final Screen previousScreen;
    private final Game game;
    
    SavedGamesScreen(Game game, Screen previousScreen) {
        this.previousScreen = previousScreen;
        this.game = game;
        addActor(createBackButton());
    }

    private Actor createBackButton() {
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
}
