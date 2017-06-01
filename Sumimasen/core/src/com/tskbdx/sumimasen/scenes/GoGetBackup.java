package com.tskbdx.sumimasen.scenes;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Player;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Path;
import com.tskbdx.sumimasen.scenes.utility.Utility;

import java.util.LinkedList;
import java.util.List;

/*
 * Created by viet khang on 01/06/2017.
 */
public class GoGetBackup extends Scene {

    GoGetBackup() {
        currentMap = "lab";
        spawn = "left_entrance";
    }

    @Override
    public void init() {
        loadMap(currentMap, spawn);
        getCamera().setTo(GameScreen.getPlayer().getX() * 8.f, GameScreen.getPlayer().getY() * 8.f);


        Player player = GameScreen.getPlayer();
        World world = getWorld();

        if (player.hasTag("late")) {
            Entity noname = world.getEntityByName("Pr. Noname");
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
}
