package com.tskbdx.sumimasen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tskbdx.sumimasen.scenes.IntroScene;
import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.entities.Player;

/**
 * Created by Sydpy on 4/27/17.
 */
public class GameScreen implements Screen {

    public static Stage gui;
    private final Sumimasen game;

    private static final Player player = new Player(0, 0 ,0, 0);


    private Scene currentScene = new IntroScene(player);

    public GameScreen(final Sumimasen game) {
        this.game = game;
        Gdx.gl20.glClearColor(0,0,0,1);
        currentScene.init();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        currentScene.update(delta);

        game.getBatch().begin();
        currentScene.render(game.getBatch());
        game.getBatch().end();

        if (gui != null) {
            gui.act(delta);
            gui.draw();
        }

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
        gui.dispose();
    }

    public static Player getPlayer() {
        return player;
    }
}
