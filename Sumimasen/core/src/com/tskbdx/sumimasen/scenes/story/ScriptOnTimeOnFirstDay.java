package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;

/*
 * Created by viet khang on 31/05/2017.
 */
class ScriptOnTimeOnFirstDay {
    static class Setup implements State {

        @Override
        public void process(Scene scene) {
            World world = scene.getWorld();
            Entity noname = world.getEntityByName("Pr. Noname");
            noname.setInteraction(new Dialogue("welcome.xml"));
        }

        @Override
        public State nextState(Event event) {
            if (event.is(Dialogue.class, "Pr. Noname")) {
                return new ScriptWorkToBreakMachine.Setup();
            }
            return null;
        }
    }
}
