package com.tskbdx.sumimasen.scenes.inputprocessors;

import com.badlogic.gdx.InputProcessor;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.entities.Player;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.Input.Keys.*;
import static com.tskbdx.sumimasen.scenes.model.entities.Direction.*;
import static javafx.scene.input.KeyCode.NUMPAD0;

/**
 * Created by Sydpy on 4/27/17.
 */

public class BasicInputProcessor implements InputProcessor{

    private Player player = GameScreen.getPlayer();

    /**
     * Commands identified by an Integer keycode.
     */
    private final Map<Integer, Runnable> keyDownCommands = new HashMap<>();
    private final Map<Integer, Runnable> keyUpCommands = new HashMap<>();
    private int currentKey;

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

        System.out.println("Associating M key with inventory random remove for test");
        associate(keyDownCommands, () -> player.inventoryRandomRemove(), M);
    }

    private void initKeyUp() {
        associate(keyUpCommands, () -> player.setDirection(NONE),
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
        return  false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode != currentKey) {
            return false;
        }
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
