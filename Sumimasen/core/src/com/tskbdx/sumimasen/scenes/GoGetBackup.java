package com.tskbdx.sumimasen.scenes;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Player;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
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

        noname.setInteraction(new Dialogue("default.xml"));

        if (player.hasTag("late")) {
            world.removeEntity(world.getEntityByName("late sensor"));
            noname.moveTo(13, 4);
            noname.setSpeed(4);
            List<Direction> path = new LinkedList<>();
            Utility.repeat(() -> path.add(Direction.NORTH), 6);
            Utility.repeat(() -> path.add(Direction.EAST), 7);
            new Path(() -> noname.setDirection(Direction.SOUTH),
                    path.toArray(new Direction[path.size()])).move(noname);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
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
