package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.Gdx;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Path;
import com.tskbdx.sumimasen.scenes.utility.Utility;

import java.util.LinkedList;
import java.util.List;

/*
 * Created by viet khang on 01/06/2017.
 */
public class DefendYourself extends Scene {

    @Override
    public void init() {
        World world = getWorld();
        Entity noname = world.getEntityByName("Pr. Noname");
        Entity player = GameScreen.getPlayer();
        player.setDirection(Direction.NONE);
        Gdx.input.setInputProcessor(null);

        List<Direction> path = new LinkedList<>();
        Utility.repeat(() -> path.add(Direction.WEST), 7);
        Utility.repeat(() -> path.add(Direction.SOUTH), 6);

        new Path(() -> {
            new Dialogue("playerLate.xml").start(noname, player);
        }, path.toArray(new Direction[path.size()])).move(noname);
    }

    @Override
    public boolean isFinished() {
        Entity player = GameScreen.getPlayer();
        return player.hasInteractedWith("Pr. Noname");
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
    protected String description() {
        return null;
    }
}
