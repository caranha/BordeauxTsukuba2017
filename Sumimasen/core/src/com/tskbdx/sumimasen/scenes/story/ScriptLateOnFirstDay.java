package com.tskbdx.sumimasen.scenes.story;

import com.badlogic.gdx.Gdx;
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
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.SpritesheetUtils;
import com.tskbdx.sumimasen.scenes.view.entities.animator.DirectionSpriteSheetAnimator;

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

            WorldRenderer worldRenderer = scene.getWorldRenderer();
            Entity machine = world.getEntityByName("Machine");
            EntityRenderer machineSprite = worldRenderer.getRendererByEntity(machine);
            machine.setInteraction(new Dialogue("broken.xml"));
            machine.addTag("broken");
            machineSprite.setStandingAnimator(
                    SpritesheetUtils.getAnimatorFromSpritesheet("machine_broken.png"));
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
