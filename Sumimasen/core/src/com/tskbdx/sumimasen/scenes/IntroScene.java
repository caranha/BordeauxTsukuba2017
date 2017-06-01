package com.tskbdx.sumimasen.scenes;

import com.tskbdx.sumimasen.GameScreen;

/*
 * Created by Sydpy on 4/27/17.
 */
public class IntroScene extends Scene {

    public IntroScene() {
        super();
        currentMap = "home";
        spawn = "entrance";
    }

    @Override
    public void init() {
        loadMap(currentMap, spawn);

        story.setScene(this);
        getCamera().setTo(GameScreen.getPlayer().getX() * 8.f, GameScreen.getPlayer().getY() * 8.f);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Scene getNextScene() {
        return null;
    }

    @Override
    public void dispose() {
    }


}
