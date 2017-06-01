package com.tskbdx.sumimasen.scenes;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Sensor;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.TriggerThought;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;

/*
 * Created by viet khang on 01/06/2017.
 */
public class LateOnFirstDay extends Scene {


    @Override
    public void init() {
        World world = getWorld();

        Sensor sensor = new Sensor();
        sensor.setName("late sensor");
        sensor.setWidth(4);
        sensor.setHeight(1);
        sensor.setX(11);
        sensor.setY(4);
        sensor.setOnCollide(new TriggerThought("!"));
        world.addSensor(sensor);

        WorldRenderer worldRenderer = getWorldRenderer();
        Entity machine = world.getEntityByName("Machine");
        EntityRenderer machineSprite = worldRenderer.getRendererByEntity(machine);
        // TO DO change machine sprite to broken
        machine.setInteraction(new Dialogue("broken.xml"));
        machine.addTag("broken");
    }

    @Override
    public boolean isFinished() {
        Entity player = GameScreen.getPlayer();
        return player.hasInteractedWith("late sensor");
    }

    @Override
    public Scene getNextScene() {
        return new DefendYourself();
    }

    @Override
    public void dispose() {

    }

    @Override
    protected String defaultMap() {
        return "lab";
    }

    @Override
    protected String defaultSpawn() {
        return "left_entrance";
    }

    @Override
    public String description() {
        return "I'm late !";
    }
}
