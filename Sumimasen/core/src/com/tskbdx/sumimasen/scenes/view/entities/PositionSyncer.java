package com.tskbdx.sumimasen.scenes.view.entities;

/**
 * Created by Sydpy on 5/6/17.
 */
public interface PositionSyncer {
    void start();
    void update();

    boolean isFinished();

    void setOnFinished(Runnable runnable);
}
