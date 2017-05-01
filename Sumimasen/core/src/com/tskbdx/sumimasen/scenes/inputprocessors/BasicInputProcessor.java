package com.tskbdx.sumimasen.scenes.inputprocessors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.tskbdx.sumimasen.scenes.model.entities.Player;

/**
 * Created by Sydpy on 4/27/17.
 */
public class BasicInputProcessor implements InputProcessor{

    private Player player;

    public BasicInputProcessor(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Input.Keys.W:
                player.setUpMove(true);
                break;

            case Input.Keys.S:
                player.setDownMove(true);
                break;

            case Input.Keys.D:
                player.setRightMove(true);
                break;

            case Input.Keys.A:
                player.setLeftMove(true);
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case Input.Keys.W:
                player.setUpMove(false);
                break;

            case Input.Keys.S:
                player.setDownMove(false);
                break;

            case Input.Keys.D:
                player.setRightMove(false);
                break;

            case Input.Keys.A:
                player.setLeftMove(false);
                break;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
