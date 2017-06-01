package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Player;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Path;
import com.tskbdx.sumimasen.scenes.utility.Utility;

import java.util.LinkedList;
import java.util.List;

/*
 * Created by viet khang on 31/05/2017.
 */
class ScriptGoGetBackup {
    static class Setup  implements State {

        @Override
        public void process(Scene scene) {
            Player player = GameScreen.getPlayer();
            World world = scene.getWorld();

            if (player.hasTag("late")) {
                Entity noname = world.getEntityByName("Pr. Noname");
                noname.setSpeed(4);
                world.removeEntity(world.getEntityByName("late sensor"));
                List<Direction> path = new LinkedList<>();
                Utility.repeat(() -> path.add(Direction.NORTH), 6);
                Utility.repeat(() -> path.add(Direction.EAST), 7);
                new Path(() -> noname.setDirection(Direction.SOUTH),
                        path.toArray(new Direction[path.size()])).move(noname);
            }
        }

        @Override
        public State nextState(Event event) {
            return null;
        }
    }
}
