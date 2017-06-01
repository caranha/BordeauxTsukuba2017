package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.Gdx;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Player;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Path;
import com.tskbdx.sumimasen.scenes.utility.Utility;

import java.util.LinkedList;
import java.util.List;

/*
 * Created by viet khang on 01/06/2017.
 */
public class GoGetBackup extends Scene {

    @Override
    public void init() {
        World world = getWorld();
        Player player = GameScreen.getPlayer();
        Entity noname = world.getEntityByName("Pr. Noname");

        if (player.hasTag("late")) {
            world.removeSensor(world.getSensorByName("late sensor"));
            noname.moveTo(13, 4);
            noname.setSpeed(4);
            List<Direction> path = new LinkedList<>();
            Utility.repeat(() -> path.add(Direction.NORTH), 6);
            Utility.repeat(() -> path.add(Direction.EAST), 7);
            new Path(() -> noname.setDirection(Direction.SOUTH),
                    path.toArray(new Direction[path.size()])).move(noname);
            noname.setInteraction(new Dialogue("default.xml"));
        } else {
            Gdx.input.setInputProcessor(null);
            new Path(() -> {
                Interaction defaultDialogue = new Dialogue("default.xml");
                Interaction dialogue = new Dialogue("brokenMachine.xml");
                dialogue.setOnFinished(() ->
                        new Path(() -> {
                            noname.setDirection(Direction.SOUTH);
                            noname.notifyObservers();
                            noname.setInteraction(defaultDialogue);
                        }, Direction.WEST, Direction.WEST).move(noname));
                noname.setInteraction(dialogue);
                player.setDirection(Direction.WEST);
                noname.getInteraction().start(noname, player);
            }, Direction.EAST, Direction.EAST).move(noname);
        }
    }

    @Override
    public boolean isFinished() {
        Entity player = GameScreen.getPlayer();
        return player.hasInteractedWith("Pr. Noname");
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
        return "lab";
    }

    @Override
    protected String defaultSpawn() {
        return "left_entrance";
    }

    @Override
    public String description() {
        return "I have to get that back up, let's go !";
    }
}
