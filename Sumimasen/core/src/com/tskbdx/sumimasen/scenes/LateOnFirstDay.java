package com.tskbdx.sumimasen.scenes;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.TriggerThought;
import com.tskbdx.sumimasen.scenes.view.WorldRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.SpritesheetUtils;

/*
 * Created by viet khang on 01/06/2017.
 */
public class LateOnFirstDay extends Scene {

    LateOnFirstDay() {
        currentMap = "lab";
        spawn = "left_entrance";
    }

    @Override
    public void init() {
        loadMap(currentMap, spawn);
        getCamera().setTo(GameScreen.getPlayer().getX() * 8.f, GameScreen.getPlayer().getY() * 8.f);

        World world = getWorld();

        Entity sensor = new Entity();
        sensor.setWorld(world);
        sensor.setWidth(4);
        sensor.setHeight(1);
        world.addEntity("late sensor", sensor);
        sensor.moveTo(11, 4);
        world.moveEntity(sensor, sensor.getX(), sensor.getY());
        sensor.setOnCollide(new TriggerThought("!"));

        WorldRenderer worldRenderer = getWorldRenderer();
        Entity machine = world.getEntityByName("Machine");
        EntityRenderer machineSprite = worldRenderer.getRendererByEntity(machine);
        machine.setInteraction(new Dialogue("broken.xml"));
        machine.addTag("broken");
        machineSprite.setStandingAnimator(
                SpritesheetUtils.getAnimatorFromSpritesheet("machine_broken.png"));
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
    public String getName() {
        return null;
    }
}
