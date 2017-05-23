package com.tskbdx.sumimasen.scenes.story.scriptIntroScene;

import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Path;
import com.tskbdx.sumimasen.scenes.story.Event;
import com.tskbdx.sumimasen.scenes.story.State;

import static com.tskbdx.sumimasen.scenes.model.entities.Direction.NORTH;
import static com.tskbdx.sumimasen.scenes.model.entities.Direction.SOUTH;
import static com.tskbdx.sumimasen.scenes.model.entities.Direction.WEST;

/*
 * Created by viet khang on 23/05/2017.
 */

/**
 * State created as a test
 */
class AfterTeleport implements State {
    @Override
    public void process(Scene scene) {
        World world = scene.getWorld();
        Entity entity = world.getEntitiesByName("entity").get(0);
        new Path(false,
                WEST, WEST, WEST, WEST, WEST, WEST, WEST, WEST, WEST
                , WEST, WEST, WEST, WEST, WEST, WEST, WEST, WEST, NORTH,
                NORTH, WEST, WEST, SOUTH, SOUTH).move(entity);

    }

    @Override
    public State nextState(Event event) {
        return null;
    }
}
