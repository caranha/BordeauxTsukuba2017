package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.TriggerThought;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Path;
import com.tskbdx.sumimasen.scenes.utility.Utility;

import java.util.LinkedList;
import java.util.List;

/*
 * Created by viet khang on 31/05/2017.
 */
class ScriptLateOnFirstDay {

    static class Setup implements State {

        @Override
        public void process(Scene scene) {
            World world = scene.getWorld();

            Entity sensor = new Entity();
            sensor.setWorld(world);
            sensor.moveTo(11, 4);
            sensor.setWidth(4);
            sensor.setHeight(1);
            sensor.setOnCollide(new TriggerThought("!"));

            Entity noname = world.getEntityByName("Pr. Noname");

            List<Direction> path = new LinkedList<>();
            Utility.repeat(() -> path.add(Direction.WEST), 7);
            Utility.repeat(() -> path.add(Direction.SOUTH), 5);

            Direction[] directions = new Direction[path.size()];
            new Path(false, path.toArray(directions)).move(noname);

            System.out.println("LATE !");
        }

        @Override
        public State nextState(Event event) {
            return null;
        }
    }
}
