package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.TriggerThought;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;

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

            WorldRenderer worldRenderer = scene.getWorldRenderer();
            Entity machine = world.getEntityByName("Machine");
            machine.setInteraction(new Dialogue("broken.xml"));
            machine.addTag("broken");
        }

        @Override
        public State nextState(Event event) {
            if (event.is(TriggerThought.class, "late sensor")) {
                return new ScriptDefendYourself.Setup();
            }
            return null;
        }
    }
}
