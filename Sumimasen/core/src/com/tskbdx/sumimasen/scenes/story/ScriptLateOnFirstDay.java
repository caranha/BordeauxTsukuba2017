package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.TriggerThought;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Path;
import com.tskbdx.sumimasen.scenes.utility.Utility;

import java.util.Collections;
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
            world.addEntity("late sensor", sensor);
            world.moveEntity(sensor, sensor.getX(), sensor.getY());
            sensor.setOnCollide(new TriggerThought("!"));
        }

        @Override
        public State nextState(Event event) {
            if (event.is(TriggerThought.class, "late sensor")) {
                return new NoNameComes();
            }
            return null;
        }
    }

    static private class NoNameComes implements State {

        @Override
        public void process(Scene scene) {
            World world = scene.getWorld();
            Entity noname = world.getEntityByName("Pr. Noname");
            Entity player = GameScreen.getPlayer();
            Movement playerMovement = player.getMovement();
            player.setMovement(null);

            List<Direction> path = new LinkedList<>();
            Utility.repeat(() -> path.add(Direction.WEST), 7);
            Utility.repeat(() -> path.add(Direction.SOUTH), 6);

            new Path(() -> {
                player.setMovement(playerMovement);
                new Dialogue("playerLate.xml").start(noname, player);
            }, path.toArray(new Direction[path.size()])).move(noname);
        }

        @Override
        public State nextState(Event event) {
            if (event.is(Dialogue.class, "Pr. Noname")) {
                return new LetsWork();
            }
            return null;
        }
    }

    static private class LetsWork implements State {

        @Override
        public void process(Scene scene) {
            World world = scene.getWorld();
            world.removeEntity(world.getEntityByName("late sensor"));

            Entity noname = world.getEntityByName("Pr. Noname");
            noname.setSpeed(4);

            List<Direction> path = new LinkedList<>();
            Utility.repeat(() -> path.add(Direction.NORTH), 6);
            Utility.repeat(() -> path.add(Direction.WEST), 5);

            List<Direction> patrol = new LinkedList<>();
            Utility.repeat(() -> patrol.add(Direction.WEST), 3);
            Utility.repeat(() -> patrol.add(Direction.EAST), 3);
            Utility.repeat(() -> patrol.add(Direction.NONE), 10);
            Collections.shuffle(patrol);

            new Path(() ->
                    new Path(true,
                            patrol.toArray(new Direction[patrol.size()])).move(noname),
                    path.toArray(new Direction[path.size()])).move(noname);
        }

        @Override
        public State nextState(Event event) {
            return null;
        }
    }
}
