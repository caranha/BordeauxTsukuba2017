package com.tskbdx.sumimasen.scenes;

import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.utility.Utility;

import java.util.LinkedList;
import java.util.List;

/*
 * Created by viet khang on 03/06/2017.
 */
public class RunOutOfRestaurant extends Scene {
    @Override
    public void init() {
        World world = getWorld();
        Entity ytrichat = world.getEntityByName("Ytricha");

        List<Direction> path = new LinkedList<>();
        path.add(Direction.NORTH);
        Utility.repeat(() -> path.add(Direction.WEST), 3);
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
        return null;
    }

    @Override
    protected String defaultSpawn() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }
}
