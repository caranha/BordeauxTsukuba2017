package com.tskbdx.sumimasen.scenes.inputprocessors;

import com.badlogic.gdx.InputAdapter;

import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;
import static com.tskbdx.sumimasen.GameScreen.getPlayer;
import static com.tskbdx.sumimasen.scenes.model.entities.Direction.*;

/*
 * Created by Sydpy on 4/27/17.
 */

/**
 * Map keyboard events to action.
 *
 * To move :
 * Arrows or Z Q S D
 *
 * To interact :
 * Space or Enter
 */
public class GameCommands extends InputAdapter {

    /**
     * Commands identified by an Integer keycode.
     */
    private final Map<Integer, Runnable> keyDownCommands = new HashMap<>();
    private final Map<Integer, Runnable> keyUpCommands = new HashMap<>();
    private int currentKey;

    /**
     * Constructor : set association between keycode and commands
     */
    public GameCommands() {
        initKeyUp();
        initKeyDown();
    }

    private void initKeyDown() {
        associate(keyDownCommands, () -> getPlayer().setDirection(NORTH),
                W, UP);
        associate(keyDownCommands, () -> getPlayer().setDirection(SOUTH),
                S, DOWN);
        associate(keyDownCommands, () -> getPlayer().setDirection(EAST),
                D, RIGHT);
        associate(keyDownCommands, () -> getPlayer().setDirection(WEST),
                A, LEFT);
        associate(keyDownCommands, () -> getPlayer().tryInteract(),
                ENTER, SPACE);
    }

    private void initKeyUp() {
        associate(keyUpCommands, () -> getPlayer().setDirection(NONE),
                W, S, D, A, UP, DOWN, RIGHT, LEFT
        );
    }

    private void associate(Map<Integer, Runnable> map, Runnable command, int... keys) {
        for (int key : keys) {
            map.put(key, command);
        }
    }

    private boolean execute(Map<Integer, Runnable> commands, int keycode) {
        if (commands.containsKey(keycode)) {
            commands.get(keycode).run();
            return true;
        }
        return false;
    }

    /**
     * Unused methods
     */

    @Override
    public boolean keyDown(int keycode) {
        if (execute(keyDownCommands, keycode)) {
            currentKey = keycode;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return keycode == currentKey && execute(keyUpCommands, keycode);
    }
}
