package com.tskbdx.sumimasen.scenes;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;

/*
 * Created by Sydpy on 4/27/17.
 */
public class IntroScene extends Scene {

    public IntroScene() {
        currentMap = "home";
        spawn = "entrance";
    }

    @Override
    public void init() {
        loadMap(currentMap, spawn);
        getCamera().setTo(GameScreen.getPlayer().getX() * 8.f, GameScreen.getPlayer().getY() * 8.f);

        World world = getWorld();
        Entity cat = world.getEntityByName("Cat"),
                coffee = world.getEntityByName("Coffee Machine");

        cat.setInteraction(new Dialogue("feedOrNot.xml"));
        coffee.setInteraction(new Dialogue("drinkOrNot.xml"));
    }

    @Override
    public boolean isFinished() {
        Entity player = GameScreen.getPlayer();
        return player.hasInteractedWith("To lab from left...") ||
                player.hasInteractedWith("To lab from right...");
    }

    @Override
    public Scene getNextScene() {
        Entity player = GameScreen.getPlayer();
        return player.hasTag("late") ?
                new LateOnFirstDay() :
                new OnTimeOnFirstDay();
    }

    @Override
    public void dispose() {
    }


}
