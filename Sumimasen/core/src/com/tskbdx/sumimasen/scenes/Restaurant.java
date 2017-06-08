package com.tskbdx.sumimasen.scenes;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;

/*
 * Created by viet khang on 01/06/2017.
 */
public class Restaurant extends Scene {
    @Override
    public void init() {
        World world = getWorld();
        Entity ytricha = world.getEntityByName("Ytricha");
        ytricha.setInteraction(new Dialogue("welcome.xml"));
    }

    @Override
    public boolean isFinished() {
        Entity player = GameScreen.getPlayer();
        return player.hasInteractedWith("Ytricha");
    }

    @Override
    public Scene getNextScene() {
        Entity player = GameScreen.getPlayer();
        if (player.hasTag("stealFood")) {
            return new RunOutOfRestaurant();
        } else if (player.hasTag("compromiseToEat")) {
            return new BackUpCompany();
        }

        throw new IllegalStateException("can't find new state");
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
