package com.tskbdx.sumimasen.scenes.story.scriptIntroScene;

import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Player;
import com.tskbdx.sumimasen.scenes.story.Event;
import com.tskbdx.sumimasen.scenes.story.State;

/*
 * Created by viet khang on 23/05/2017.
 */
class MeetNoname implements State {
    @Override
    public void process(Scene scene) {
        World world = scene.getWorld();
        Player player = GameScreen.getPlayer();
        if (player.has("magnet")) {

        } else {

        }
    }

    @Override
    public State nextState(Event event) {
        return null;
    }
}
