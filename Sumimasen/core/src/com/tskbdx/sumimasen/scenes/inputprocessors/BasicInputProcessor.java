package com.tskbdx.sumimasen.scenes.inputprocessors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Direction;

import java.util.HashMap;
import java.util.Map;

import static com.tskbdx.sumimasen.scenes.model.entities.movements.Direction.*;

/**
 * Created by Sydpy on 4/27/17.
 */

public class BasicInputProcessor implements InputProcessor{

    /**
     * Commands identified by an Integer keycode.
     */
    private final Map<Integer, Runnable> keyDownCommands = new HashMap<>();
    private final Map<Integer, Runnable> keyUpCommands = new HashMap<>();

    /**
     * Constructor : set association between keycode and commands
     */
    public BasicInputProcessor() {
        initKeyUp();
        initKeyDown();
    }

    @Override
    public boolean keyDown(int keycode) {
        return execute(keyDownCommands, keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return execute(keyUpCommands, keycode);
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

    private void initKeyDown() {
        keyDownCommands.put(Input.Keys.W, () -> GameScreen.player.setDirection(UP));
        keyDownCommands.put(Input.Keys.S, () -> GameScreen.player.setDirection(DOWN));
        keyDownCommands.put(Input.Keys.D, () -> GameScreen.player.setDirection(RIGHT));
        keyDownCommands.put(Input.Keys.A, () -> GameScreen.player.setDirection(LEFT));
    }

    private void initKeyUp() {
        keyUpCommands.put(Input.Keys.W, () -> GameScreen.player.setDirection(NONE));
        keyUpCommands.put(Input.Keys.S, () -> GameScreen.player.setDirection(NONE));
        keyUpCommands.put(Input.Keys.D, () -> GameScreen.player.setDirection(NONE));
        keyUpCommands.put(Input.Keys.A, () -> GameScreen.player.setDirection(NONE));
    }

    private boolean execute(Map<Integer, Runnable> commands, int keycode) {
        try {
            commands.get(keycode).run();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
