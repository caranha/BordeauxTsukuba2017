package com.tskbdx.sumimasen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.tskbdx.sumimasen.scenes.IntroScene;
import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Player;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Sydpy on 4/27/17.
 */
public class GameScreen implements Screen {

    private final String SAVE_DIR = "";

    private final Sumimasen game;

    private static Player player = new Player();

    private static Scene currentScene;

    public GameScreen(final Sumimasen game) {
        this(game, false);
    }

    public GameScreen(final Sumimasen game, boolean loadFromSave) {
        this.game = game;
        Gdx.gl20.glClearColor(0,0,0,1);

        if (loadFromSave) {
            loadFromSave();
            currentScene.init();
        } else {
            currentScene = new IntroScene();
            currentScene.init();
        }
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
        try {
            System.out.println("Serializing current scene");
            FileOutputStream fout1 = new FileOutputStream(SAVE_DIR + "scene.save");
            ObjectOutputStream out1 = new ObjectOutputStream(fout1);
            out1.writeObject(currentScene.getClass());
            out1.close();
            fout1.close();

            System.out.println("Serializing player'");
            FileOutputStream fout2 = new FileOutputStream(SAVE_DIR + "player.save");
            ObjectOutputStream out2 = new ObjectOutputStream(fout2);
            player.setWorld(null);
            out2.writeObject(player);
            out2.close();
            fout2.close();

        } catch (IOException e) {
            System.err.println("Error while saving current game state");
            e.printStackTrace();
        }
    }

    private void loadFromSave() {

        try {

            System.out.println("Deserializing current scene");
            FileInputStream fin1                = new FileInputStream(SAVE_DIR + "scene.save");
            ObjectInputStream in1               = new ObjectInputStream(fin1);
            Class<? extends Scene> sceneClass   = (Class<? extends Scene>) in1.readObject();
            in1.close();
            fin1.close();

            System.out.println("Deserializing player");
            FileInputStream fin2    = new FileInputStream(SAVE_DIR + "player.save");
            ObjectInputStream in2   = new ObjectInputStream(fin2);
            Entity player           = (Entity) in2.readObject();
            in2.close();
            fin2.close();

            GameScreen.player.setHeight(player.getHeight());
            GameScreen.player.setWidth(player.getWidth());
            GameScreen.player.setName(player.getName());
            GameScreen.player.setDirection(player.getDirection());
            GameScreen.player.setOnCollide(player.getOnCollide());
            GameScreen.player.setInventory(player.getInventory());
            GameScreen.player.setMovement(player.getMovement());
            GameScreen.player.setInteraction(player.getInteraction());


            this.currentScene = sceneClass.getConstructor().newInstance();
            this.currentScene.init();

        } catch (IOException |
                ClassNotFoundException |
                NoSuchMethodException |
                IllegalAccessException |
                InvocationTargetException |
                InstantiationException e) {

            System.out.println("Error while loading previously saved state");
            e.printStackTrace();
        }
    }

    public static Player getPlayer() {
        return player;
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }
}
