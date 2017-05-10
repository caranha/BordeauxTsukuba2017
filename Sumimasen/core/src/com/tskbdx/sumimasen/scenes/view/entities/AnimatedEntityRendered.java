package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.HashMap;
import java.util.Map;

import static com.tskbdx.sumimasen.scenes.model.entities.Direction.*;

/**
 * Created by viet khang on 09/05/2017.
 */

/**
 * Convention : sprite sheet taken are 1 col and many rows
 * Last frame is default (when not animating)
 */
public class AnimatedEntityRendered extends EntityRenderer {
    private float stateTime;

    private Map<Direction, TextureRegion> defaultTextures = new HashMap<>();
    private Map<Direction, Animation<TextureRegion>> animations = new HashMap<>();

    public AnimatedEntityRendered(Entity entity, String imagefile,
                                  int cols, int rows, int fps) {
        super(entity, imagefile);
        initFrames(cols, rows, fps);
    }

    private void initFrames(int cols, int rows, int fps) {
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
    public void render(Batch batch) {

        if (isAnimating()) {
            stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
            updateAnimation();
            batch.draw(animations.get(entity.getLastDirection()).
                    getKeyFrame(stateTime, true), getX() + 2, getY());
        } else {
            batch.draw(defaultTextures.get(entity.getLastDirection()), getX() + 2, getY());
        }

        renderMessage(batch);
    }
}
