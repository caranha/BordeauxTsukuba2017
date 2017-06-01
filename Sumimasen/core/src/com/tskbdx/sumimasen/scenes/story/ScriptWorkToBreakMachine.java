package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Player;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Path;
import com.tskbdx.sumimasen.scenes.utility.Utility;

import java.util.LinkedList;
import java.util.List;

/*
 * Created by viet khang on 31/05/2017.
 */
class ScriptWorkToBreakMachine {

    static class Setup implements State {

        @Override
        public void process(Scene scene) {
            World world = scene.getWorld();
            Player player = GameScreen.getPlayer();
            if (player.hasTag("late")) {
            }
            Entity machine = world.getEntityByName("Machine");
            machine.setInteraction(new Dialogue("letsWork.xml"));
        }

        @Override
        public State nextState(Event event) {
            return null;
        }
    }
}
