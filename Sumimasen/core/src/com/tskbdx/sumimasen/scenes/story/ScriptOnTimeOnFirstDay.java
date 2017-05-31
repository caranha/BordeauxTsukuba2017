package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Path;
import com.tskbdx.sumimasen.scenes.utility.Utility;

import java.util.LinkedList;
import java.util.List;

/*
 * Created by viet khang on 31/05/2017.
 */
class ScriptOnTimeOnFirstDay {
    static class Setup implements State {

        @Override
        public void process(Scene scene) {
            World world = scene.getWorld();
            Entity noname = world.getEntityByName("Pr. Noname");

            List<Direction> patrol = new LinkedList<>();
            Utility.repeat(() -> patrol.add(Direction.WEST), 3);
            Utility.repeat(() -> patrol.add(Direction.NONE), 6);
            Utility.repeat(() -> patrol.add(Direction.EAST), 3);
            Utility.repeat(() -> patrol.add(Direction.NONE), 6);

            noname.setMovement(new Path(true,
                    patrol.toArray(new Direction[patrol.size()])));
            noname.getMovement().move(noname); // // 

            noname.setInteraction(new Dialogue("welcome.xml"));
        }

        @Override
        public State nextState(Event event) {
            if (event.is(Dialogue.class, "Pr. Noname")) {
                return new LetsWork();
            }
            return null;
        }
    }

    private static class LetsWork implements State {

        @Override
        public void process(Scene scene) {
            World world = scene.getWorld();

            Entity noname = world.getEntityByName("Pr. Noname");
            noname.setSpeed(4);

            List<Direction> path = new LinkedList<>();
            Utility.repeat(() -> path.add(Direction.WEST), 7);

            List<Direction> patrol = new LinkedList<>();
            Utility.repeat(() -> patrol.add(Direction.WEST), 2);
            Utility.repeat(() -> patrol.add(Direction.NONE), 5);
            Utility.repeat(() -> patrol.add(Direction.EAST), 2);
            Utility.repeat(() -> patrol.add(Direction.NONE), 5);

            Entity machine = world.getEntityByName("Machine");
            machine.setInteraction(new Dialogue("letsWork.xml"));
        }

        @Override
        public State nextState(Event event) {
            return null;
        }
    }
}
