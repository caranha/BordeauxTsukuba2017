package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.tskbdx.sumimasen.scenes.TiledMapUtils;
import com.tskbdx.sumimasen.scenes.model.entities.Sensor;
import com.tskbdx.sumimasen.scenes.view.entities.animator.Animator;

import java.awt.*;

/**
 * Created by Sydpy on 6/1/17.
 */
public class SensorRenderer {

    private Sensor sensor;

    private Animator animator;

    private Rectangle rectangle = new Rectangle();

    public SensorRenderer(Sensor sensor, TiledMapUtils.SensorDescriptor sensorDescriptor) {

        this.sensor = sensor;
        animator = SpritesheetUtils.getAnimatorFromSpritesheet(sensorDescriptor.imageFile);
        rectangle.setBounds(
                sensor.getX() * TiledMapUtils.TILE_SIZE,
                sensor.getY() * TiledMapUtils.TILE_SIZE,
                sensor.getWidth() * TiledMapUtils.TILE_SIZE,
                sensor.getHeight() * TiledMapUtils.TILE_SIZE);
    }

    public void render(Batch batch) {

        batch.draw(animator.update(), rectangle.x, rectangle.y, rectangle.width, rectangle.height);

    }
}
