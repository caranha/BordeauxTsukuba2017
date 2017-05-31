package com.tskbdx.sumimasen.scenes.story;

import com.badlogic.gdx.Game;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
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
            sensor.setName("late sensor");
            sensor.setWorld(world);
            sensor.moveTo(11, 4);
            sensor.setWidth(4);
            sensor.setHeight(1);
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
            player.setMovement(null);

            List<Direction> path = new LinkedList<>();
            Utility.repeat(() -> path.add(Direction.WEST), 7);
            Utility.repeat(() -> path.add(Direction.SOUTH), 6);

            Direction[] directions = new Direction[path.size()];
            new Path(false, path.toArray(directions)).move(noname);

            noname.setInteraction(new Dialogue("playerLate.xml"));
            noname.getInteraction().start(noname, player);
        }

        @Override
        public State nextState(Event event) {
            return null;
        }
    }
}
