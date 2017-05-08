package com.tskbdx.sumimasen.scenes.inputprocessors;

import com.badlogic.gdx.InputProcessor;
import com.tskbdx.sumimasen.GameScreen;

import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;
import static com.tskbdx.sumimasen.GameScreen.player;
import static com.tskbdx.sumimasen.scenes.model.entities.Direction.SOUTH;
import static com.tskbdx.sumimasen.scenes.model.entities.Direction.WEST;
import static com.tskbdx.sumimasen.scenes.model.entities.Direction.EAST;
import static com.tskbdx.sumimasen.scenes.model.entities.Direction.NORTH;
import static com.tskbdx.sumimasen.scenes.model.entities.Direction.NONE;

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

    private void initKeyDown() {
        associate(keyDownCommands, () -> player.setDirection(NORTH),
                W, UP);
        associate(keyDownCommands, () -> player.setDirection(SOUTH),
                S, DOWN);
        associate(keyDownCommands, () -> player.setDirection(EAST),
                D, RIGHT);
        associate(keyDownCommands, () -> player.setDirection(WEST),
                A, LEFT);
        associate(keyDownCommands, () -> player.tryInteract(),
                ENTER, SPACE);
    }

    private void initKeyUp() {
        associate(keyUpCommands, () -> player.setDirection(NONE),
                W, S, D, A, UP, DOWN, RIGHT, LEFT);
    }

    private void associate(Map<Integer, Runnable> map, Runnable command, int... keys) {
        for (int key : keys) {
            map.put(key, command);
        }
    }

    private boolean execute(Map<Integer, Runnable> commands, int keycode) {
        try {
            commands.get(keycode).run();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Unused methods
     */

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
}
