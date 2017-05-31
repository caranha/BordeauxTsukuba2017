package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.Scene;
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
 * Created by viet khang on 31/05/2017.
 */
class ScriptWorkToBreakMachine {

    static class Setup implements State {

        @Override
        public void process(Scene scene) {
            World world = scene.getWorld();
            Player player = GameScreen.getPlayer();
            if (player.hasTag("late")) {
                Entity noname = world.getEntityByName("Pr. Noname");
                noname.setSpeed(4);
                List<Direction> patrol = new LinkedList<>();
                List<Direction> path = new LinkedList<>();
                world.removeEntity(world.getEntityByName("late sensor"));
                Utility.repeat(() -> path.add(Direction.NORTH), 6);
                Utility.repeat(() -> path.add(Direction.WEST), 5);
                Utility.repeat(() -> patrol.add(Direction.WEST), 3);
                Utility.repeat(() -> patrol.add(Direction.NONE), 6);
                Utility.repeat(() -> patrol.add(Direction.EAST), 3);
                Utility.repeat(() -> patrol.add(Direction.NONE), 6);
                new Path(() ->
                        new Path(true,
                                patrol.toArray(new Direction[patrol.size()])).move(noname),
                        path.toArray(new Direction[path.size()])).move(noname);
            }
            Entity machine = world.getEntityByName("Machine");
            machine.setInteraction(new Dialogue("letsWork.xml"));
        }

        @Override
        public State nextState(Event event) {
            return null;
        }
    }
}
