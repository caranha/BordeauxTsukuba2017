package com.tskbdx.sumimasen.scenes;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;

/*
 * Created by viet khang on 01/06/2017.
 */
public class OnTimeOnFirstDay extends Scene {
    @Override
    public void init() {
        World world = getWorld();
        Entity noname = world.getEntityByName("Pr. Noname");
        noname.setInteraction(new Dialogue("welcome.xml"));
    }

    @Override
    public boolean isFinished() {
        Entity player = GameScreen.getPlayer();
        return player.hasInteractedWith("Pr. Noname");
    }

    @Override
    public Scene getNextScene() {
        return new LetsWork();
    }

    @Override
    public void dispose() {

    }

    @Override
    protected String defaultMap() {
        return "lab";
    }

    @Override
    protected String defaultSpawn() {
        return "left_entrance";
    }

    @Override
    protected String description() {
        return null;
    }
}
