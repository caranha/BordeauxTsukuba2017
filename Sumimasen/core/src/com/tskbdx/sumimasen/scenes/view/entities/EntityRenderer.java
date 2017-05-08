package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.view.Tween;

import javax.xml.soap.Text;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sydpy on 4/28/17.
 */

/**
 * Sprite class
 */
public class EntityRenderer extends Sprite implements Observer {
    private static String IMAGES_RES_FOLDER = "images/";
    private static int TILE_SIZE = 8;
    private Entity entity;
    private final Tween animationX = new Tween(Interpolation.linear),
            animationY = new Tween(Interpolation.linear);

    public EntityRenderer(Entity entity, String imagefile) {
        super(new Texture(Gdx.files.internal(IMAGES_RES_FOLDER + imagefile)),
                0, 0, 16, 16);
        setPosition(entity.getX() * TILE_SIZE, entity.getY() * TILE_SIZE);
        this.entity = entity;
        entity.addObserver(this);
    }

    /**
     * On update, calculate observable location and
     * prepare the animation to reach it
     * @param observable
     * @param o
     */
    @Override
    public void update(Observable observable, Object o) {
        int targetX = entity.getX() * TILE_SIZE;
        int targetY = entity.getY() * TILE_SIZE;
        if (getX() != targetX) {
            animationX.playWith(getX(),
                    targetX, 1.f / entity.getSpeed());
        }
        if (getY() != targetY) {
            animationY.playWith(getY(),
                    targetY, 1.f / entity.getSpeed());
        }
    }

    public void render(Batch batch) {
        move();
        draw(batch);
    }

    /**
     * Animate the player movements.
     */
    private void move() {
        if (animationX.isPlaying()) {
            setX(animationX.getInterpolation());
        }
        if (animationY.isPlaying()) {
            setY(animationY.getInterpolation());
        }
    }
}
