package com.tskbdx.sumimasen.scenes.story.scriptIntroScene;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.ChangeMap;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.story.Event;
import com.tskbdx.sumimasen.scenes.story.State;

/*
 * Created by viet khang on 23/05/2017.
 */
public class FirstState implements State {
    @Override
    public void process(Scene scene) {
        World world = scene.getWorld();
        Entity cat = world.getEntityByName("Cat"),
                coffee = world.getEntityByName("Coffee Machine");

        cat.setInteraction(new Dialogue("feedornot.xml"));
        coffee.setInteraction(new Dialogue("drinkornot.xml"));
    }

    @Override
    public State nextState(Event event) {
        if (event.is(ChangeMap.class, "To lab from left...") ||
                event.is(ChangeMap.class, "To lab fom right ...")) {

            if (GameScreen.getPlayer().hasTag("late")) {
                return new LateOnFirstDay();
            } else {
                return new OnTimeOnFirstDay();
            }

        }

        return null;
    }
}
