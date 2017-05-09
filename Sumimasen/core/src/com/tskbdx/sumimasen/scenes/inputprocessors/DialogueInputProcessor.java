package com.tskbdx.sumimasen.scenes.inputprocessors;

import com.badlogic.gdx.InputProcessor;
import com.tskbdx.sumimasen.scenes.Scene;

import javax.smartcardio.CommandAPDU;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sydpy on 4/27/17.
 */

public class DialogueInputProcessor implements InputProcessor {

    private Scene scene;
    private Map<Integer, Runnable> commands = new HashMap<>();

    public DialogueInputProcessor(Scene scene) {
        this.scene = scene;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
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
