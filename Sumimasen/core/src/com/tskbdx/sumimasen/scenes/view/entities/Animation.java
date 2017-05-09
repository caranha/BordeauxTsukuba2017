package com.tskbdx.sumimasen.scenes.view.entities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sydpy on 5/6/17.
 */
interface Animation {
    abstract void start();
    abstract void update();

    abstract boolean isFinished();
}
