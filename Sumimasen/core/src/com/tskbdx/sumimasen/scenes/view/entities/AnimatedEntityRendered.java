package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import static com.tskbdx.sumimasen.scenes.model.entities.Direction.*;

/**
 * Created by viet khang on 09/05/2017.
 */

/**
 */
public class AnimatedEntityRendered extends EntityRenderer {
    private float stateTime;

    private Map<Direction, TextureRegion> defaultTextures = new HashMap<>();
    private Map<Direction, Animation<TextureRegion>> animations = new HashMap<>();
    private Direction processingDirection; // to avoid changing duration during animation

    public AnimatedEntityRendered(Entity entity, String imagefile,
                                  int cols, int rows, float fps, AssetManager assetManager) {
        super(entity, imagefile, assetManager);
        initFrames(cols, rows, fps);
        processingDirection = entity.getLastDirection();
    }

    private void initFrames(int cols, int rows, float fps) {
        TextureRegion[][] tmp = TextureRegion.split(image,
                image.getWidth() / cols,
                image.getHeight() / rows);

        // Set default textures
        defaultTextures.put(NORTH, tmp[0][0]);
        defaultTextures.put(SOUTH, tmp[1][0]);
        defaultTextures.put(WEST, tmp[2][0]);
        defaultTextures.put(EAST, tmp[3][0]);

        // Set animation textures for each direction
        int index = 4; // offset in the sprite sheet
        for (Direction direction : Direction.values()) {
            if (!direction.equals(NONE)) {
                TextureRegion[] frames = new TextureRegion[cols];
                for (int i = 0; i != cols; ++i) {
                    frames[i] = tmp[index][i];
                }
                animations.put(direction, new Animation<>(1.f / fps, frames));
                ++index;
            }
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        super.update(observable, o);
            /*
             * Menage the case where the entity tried to move
             * but hasn't succeeded
             * In that cas, the direction has actually changed
             */
        updateDirection();
    }

    private void updateDirection() {
        Direction direction = entity.getDirection();
        if (!direction.equals(NONE)) {
            processingDirection = direction;
        }
    }

    @Override
    public void render(Batch batch) {

        if (isAnimating()) {
            stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
            updateAnimation();
            batch.draw(animations.get(processingDirection).
                    getKeyFrame(stateTime, true), getX() + 2, getY());
        } else {
            batch.draw(defaultTextures.get(processingDirection), getX() + 2, getY());
        }

    }
}
