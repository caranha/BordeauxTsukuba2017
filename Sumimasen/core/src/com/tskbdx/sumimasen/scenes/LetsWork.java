package com.tskbdx.sumimasen.scenes;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;

/*
 * Created by viet khang on 01/06/2017.
 */
public class LetsWork extends Scene {
    @Override
    public void init() {
        World world = getWorld();
        Entity noname = world.getEntityByName("Pr. Noname");
        Entity machine = world.getEntityByName("Machine");

        machine.setInteraction(new Dialogue("letsWork.xml"));
        noname.setInteraction(new Dialogue("default.xml"));
    }

    @Override
    public boolean isFinished() {
        Entity player = GameScreen.getPlayer();
        return player.hasInteractedWith("Machine");
    }

    @Override
    public Scene getNextScene() {
        return new GoGetBackup();
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
    public String description() {
        return "It's my first day, I must not do any mistake.";
    }
}
