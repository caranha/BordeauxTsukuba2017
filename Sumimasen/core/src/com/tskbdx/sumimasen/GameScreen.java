package com.tskbdx.sumimasen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.tskbdx.sumimasen.scenes.IntroScene;
import com.tskbdx.sumimasen.scenes.Scene;

/**
 * Created by Sydpy on 4/27/17.
 */
public class GameScreen implements Screen {

    private final Sumimasen game;


    private Scene currentScene = new IntroScene();

    public GameScreen(final Sumimasen game) {

        this.game = game;


        currentScene.init();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        currentScene.update(delta);


        game.getBatch().begin();
        currentScene.render(game.getBatch());
        game.getBatch().end();

        if (currentScene.isFinished()) {
            Scene nextScene = currentScene.getNextScene();

            if (nextScene != null) {
                currentScene = nextScene;
                currentScene.init();
            }
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
        currentScene.dispose();
    }


}
