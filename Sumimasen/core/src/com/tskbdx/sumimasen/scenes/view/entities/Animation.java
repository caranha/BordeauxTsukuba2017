package com.tskbdx.sumimasen.scenes.view.entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sydpy on 5/6/17.
 */
interface Animation {
    void start();
    void update();

    boolean isFinished();

    void setOnFinished(Runnable runnable);
}
