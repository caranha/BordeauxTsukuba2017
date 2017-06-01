package com.tskbdx.sumimasen.scenes;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
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

        Entity sensor = new Entity();
        sensor.setWorld(world);
        sensor.setWidth(4);
        sensor.setHeight(1);
        world.addEntity("late sensor", sensor);
        sensor.moveTo(11, 4);
        sensor.setOnCollide(new TriggerThought("!"));

        WorldRenderer worldRenderer = getWorldRenderer();
        Entity machine = world.getEntityByName("Machine");
        EntityRenderer machineSprite = worldRenderer.getRendererByEntity(machine);
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
    protected String description() {
        return null;
    }
}
