package com.tskbdx.sumimasen.scenes.inputprocessors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.tskbdx.sumimasen.scenes.model.entities.Player;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Direction;

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
                player.setVDirection(Direction.Vertical.UP);
                break;

            case Input.Keys.S:
                player.setVDirection(Direction.Vertical.DOWN);
                break;

            case Input.Keys.D:
                player.setHDirection(Direction.Horizontal.RIGHT);
                break;

            case Input.Keys.A:
                player.setHDirection(Direction.Horizontal.LEFT);
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case Input.Keys.W:
                player.setVDirection(Direction.Vertical.NONE);
                break;

            case Input.Keys.S:
                player.setVDirection(Direction.Vertical.NONE);
                break;

            case Input.Keys.D:
                player.setHDirection(Direction.Horizontal.NONE);
                break;

            case Input.Keys.A:
                player.setHDirection(Direction.Horizontal.NONE);
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
