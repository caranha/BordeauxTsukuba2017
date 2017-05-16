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
 * Space
 */
public class GameCommands extends InputAdapter {

    /**
     * Commands identified by an Integer keycode.
     */
    private final Map<Integer, Runnable> keyDownCommands = new HashMap<>();
    private final Map<Integer,Runnable> keyUpCommands = new HashMap<>();
    private int currentKeycode;

    /**
     * Constructor : set association between keycode and commands
     */
    public GameCommands() {
        initKeyDown();
        initKeyUp();
    }

    private void initKeyUp() {
        associate(keyUpCommands, () -> getPlayer().setDirection(NONE),
                W, UP, S, DOWN, D, RIGHT, A, LEFT);
    }

    private void initKeyDown() {
        associate(keyDownCommands, () -> getPlayer().move(NORTH),
                W, UP);
        associate(keyDownCommands, () -> getPlayer().move(SOUTH),
                S, DOWN);
        associate(keyDownCommands, () -> getPlayer().move(EAST),
                D, RIGHT);
        associate(keyDownCommands, () -> getPlayer().move(WEST),
                A, LEFT);
        associate(keyDownCommands, () -> getPlayer().tryInteract(),
                SPACE);
    }

    private void associate(Map<Integer, Runnable> map, Runnable callback, int... keys) {
        for (int key : keys) {
            map.put(key, callback);
        }
    }

    private boolean execute(Map<Integer, Runnable> commands, int keycode) {
        if (commands.containsKey(keycode)) {
            commands.get(keycode).run();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (execute(keyDownCommands, keycode)) {
            currentKeycode = keycode;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return currentKeycode == keycode && execute(keyUpCommands, keycode);
    }
}
