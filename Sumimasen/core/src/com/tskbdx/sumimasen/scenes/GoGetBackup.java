package com.tskbdx.sumimasen.scenes;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.concurrent.ThreadLocalRandom;

/*
 * Created by viet khang on 01/06/2017.
 */
public class GoGetBackup extends Scene {
    @Override
    public void init() {

    }

    @Override
    public boolean isFinished() {
        Entity player = GameScreen.getPlayer();
        return player.hasInteractedWith("To Progress ...");
    }

    @Override
    public Scene getNextScene() {
        return new Restaurant();
    }

    @Override
    public void dispose() {

    }

    @Override
    protected String defaultMap() {
        return null;
    }

    @Override
    protected String defaultSpawn() {
        return null;
    }

    @Override
    public String description() {
        boolean randomBool = ThreadLocalRandom.current().nextBoolean();
        return randomBool ? "Hum I feel so hungry..." : "I heard that a new restaurant opened recently";
    }
}
