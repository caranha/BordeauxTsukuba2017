package com.tskbdx.sumimasen.scenes.view.effects;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Created by Sydpy on 5/17/17.
 */
public abstract  class Effect {

    private Runnable callback = null;

    private boolean isFinished = false;

    public abstract void render(Batch batch);

    public void setCallback(Runnable callback) {
        this.callback = callback;
    }

    protected void setFinished() {
        isFinished = true;
        if (callback != null) callback.run();
    }

    public boolean isFinished() {
        return isFinished;
    }
}
