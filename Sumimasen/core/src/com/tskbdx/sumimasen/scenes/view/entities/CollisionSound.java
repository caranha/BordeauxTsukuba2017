package com.tskbdx.sumimasen.scenes.view.entities;

/*
 * Created by viet khang on 13/05/2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.Observable;
import java.util.Observer;

/**
 * Trigger a sound when the player hits a wall or another entity
 */
public class CollisionSound implements Observer {

    private static final String FOLDER = "sounds/samples/";
    private final Sound sound;

    public CollisionSound(String fileName) {
        sound = Gdx.audio.newSound(Gdx.files.internal(FOLDER + fileName));
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg.equals(false)) {
            sound.play();
        }
    }
}
