package com.tskbdx.sumimasen.scenes;

/*
 * Created by viet khang on 01/06/2017.
 */
public class OnTimeOnFirstDay extends Scene {
    @Override
    public void init() {

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
