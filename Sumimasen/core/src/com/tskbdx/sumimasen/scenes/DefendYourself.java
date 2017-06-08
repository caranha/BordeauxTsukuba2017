package com.tskbdx.sumimasen.scenes;

import com.badlogic.gdx.Gdx;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;
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
            Interaction interaction =
                    new Dialogue("playerLate.xml");
            interaction.setOnFinished(() -> {
                world.removeSensor(world.getSensorByName("late sensor"));
                noname.moveTo(13, 4);
                noname.setSpeed(4);
                List<Direction> path2 = new LinkedList<>();
                Utility.repeat(() -> path2.add(Direction.NORTH), 6);
                Utility.repeat(() -> path2.add(Direction.EAST), 7);
                new Path(() -> noname.setDirection(Direction.SOUTH),
                        path2.toArray(new Direction[path2.size()])).move(noname);
                noname.setInteraction(new Dialogue("default.xml"));

            });
            interaction.start(noname, player);
        }, path.toArray(new Direction[path.size()])).move(noname);
    }

    @Override
    public boolean isFinished() {
        Entity player = GameScreen.getPlayer();
        return player.hasInteractedWith("Pr. Noname");
    }

    @Override
    public Scene getNextScene() {
        Entity player = GameScreen.getPlayer();
        return player.hasTag("fired") ? new FiredOnFirstDay() : new GoGetBackup();
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
        return "I have to defend myself not to be fired.";
    }
}
