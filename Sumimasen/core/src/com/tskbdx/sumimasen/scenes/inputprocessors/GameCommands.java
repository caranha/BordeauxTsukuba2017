package com.tskbdx.sumimasen.scenes.inputprocessors;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;

import java.util.HashMap;
import java.util.Map;

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
        associate(keyUpCommands, () -> GameScreen.getPlayer().setDirection(Direction.NONE),
                Input.Keys.W, Input.Keys.UP, Input.Keys.S, Input.Keys.DOWN, Input.Keys.D, Input.Keys.RIGHT, Input.Keys.A, Input.Keys.LEFT);
    }

    private void initKeyDown() {
        associate(keyDownCommands, () -> GameScreen.getPlayer().move(Direction.NORTH),
                Input.Keys.W, Input.Keys.UP);
        associate(keyDownCommands, () -> GameScreen.getPlayer().move(Direction.SOUTH),
                Input.Keys.S, Input.Keys.DOWN);
        associate(keyDownCommands, () -> GameScreen.getPlayer().move(Direction.EAST),
                Input.Keys.D, Input.Keys.RIGHT);
        associate(keyDownCommands, () -> GameScreen.getPlayer().move(Direction.WEST),
                Input.Keys.A, Input.Keys.LEFT);
        associate(keyDownCommands, () -> GameScreen.getPlayer().tryInteract(),
                Input.Keys.SPACE);
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
            if(keycode != Input.Keys.SPACE) {
                currentKeycode = keycode;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return currentKeycode == keycode && execute(keyUpCommands, keycode);
    }
}
