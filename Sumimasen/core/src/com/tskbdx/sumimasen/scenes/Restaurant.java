package com.tskbdx.sumimasen.scenes;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.concurrent.ThreadLocalRandom;

/*
 * Created by viet khang on 01/06/2017.
 */
public class Restaurant extends Scene {
    @Override
    public void init() {

    }

    @Override
    public boolean isFinished() {
        Entity player = GameScreen.getPlayer();
        return player.hasTag("fed");
    }

    @Override
    public Scene getNextScene() {
        return null;
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
        return "Finally at the restaurant ! Oh I don't have money on me...";
    }
}
