package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.view.Tween;
import com.tskbdx.sumimasen.scenes.view.ui.UserInterface;

/*
 * Created by Sydpy on 4/27/17.
 */
public class IntroScene extends Scene {


    private UserInterface userInterface;

    public IntroScene() {
        super();
        userInterface = new UserInterface(this, GameScreen.getPlayer());
        currentMap = "home";
        spawn = "entrance";
    }

    @Override
    public void init() {

        loadMap(currentMap, spawn);
        userInterface.init();

        story.setScene(this);
        getCamera().setTo(GameScreen.getPlayer().getX() * TiledMapUtils.TILE_SIZE, GameScreen.getPlayer().getY() * TiledMapUtils.TILE_SIZE);
    }

    @Override
    public void update(float dt) {

        Tween.updateAll(dt);

        getCamera().translate(
                GameScreen.getPlayer().getX()*TiledMapUtils.TILE_SIZE - getCamera().position.x,
                GameScreen.getPlayer().getY()*TiledMapUtils.TILE_SIZE - getCamera().position.y);
        getCamera().update();

        userInterface.act(dt);

        Gdx.input.setInputProcessor(GameScreen.getPlayer().isInteracting() ? userInterface : getInputProcessor());
    }

    @Override
    public void render(Batch batch) {

        batch.setProjectionMatrix(getCamera().combined);

        getWorldRenderer().render();

        userInterface.draw();
    }

    @Override
    public void dispose() {
        userInterface.dispose();
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Scene getNextScene() {
        return null;
    }

}
