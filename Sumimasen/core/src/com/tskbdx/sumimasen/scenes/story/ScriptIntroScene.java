package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.ChangeMap;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;

/*
 * Created by viet khang on 31/05/2017.
 */

class ScriptIntroScene {

    static class Setup implements State {
        @Override
        public void process(Scene scene) {
            World world = scene.getWorld();
            Entity cat = world.getEntityByName("Cat"),
                    coffee = world.getEntityByName("Coffee Machine");

            cat.setInteraction(new Dialogue("feedOrNot.xml"));
            coffee.setInteraction(new Dialogue("drinkOrNot.xml"));
        }

        @Override
        public State nextState(Event event) {
            if (event.is(ChangeMap.class, "To lab from left...") ||
                    event.is(ChangeMap.class, "To lab fom right ...")) {

                if (GameScreen.getPlayer().hasTag("late")) {
                    return new ScriptLateOnFirstDay.Setup();
                } else {
                    return new ScriptOnTimeOnFirstDay.Setup();
                }
            }

            return null;
        }
    }
}
