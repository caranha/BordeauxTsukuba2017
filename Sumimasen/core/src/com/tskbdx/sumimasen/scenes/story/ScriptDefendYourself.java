package com.tskbdx.sumimasen.scenes.story;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Path;
import com.tskbdx.sumimasen.scenes.utility.Utility;

import java.util.LinkedList;
import java.util.List;

/*
 * Created by viet khang on 31/05/2017.
 */
class ScriptDefendYourself {

    static class Setup implements State {
        @Override
        public void process(Scene scene) {
            World world = scene.getWorld();
            Entity noname = world.getEntityByName("Pr. Noname");
            Entity player = GameScreen.getPlayer();
            Movement playerMovement = player.getMovement();
            player.setMovement(null);
            player.setInteracting(true);

            List<Direction> path = new LinkedList<>();
            Utility.repeat(() -> path.add(Direction.WEST), 7);
            Utility.repeat(() -> path.add(Direction.SOUTH), 6);

            new Path(() -> {
                player.setMovement(playerMovement);
                player.setDirection(Direction.NONE);
                new Dialogue("playerLate.xml").start(noname, player);
            }, path.toArray(new Direction[path.size()])).move(noname);
        }

        @Override
        public State nextState(Event event) {
            if (event.is(Dialogue.class, "Pr. Noname")) {
                return new ScriptGoGetBackup.Setup();
            }
            return null;
        }
    }
}