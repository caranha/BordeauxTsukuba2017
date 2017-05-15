package com.tskbdx.sumimasen.scenes.model.entities;

import com.badlogic.gdx.math.Rectangle;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;

import java.util.List;

/**
 * Created by Sydpy on 5/14/17.
 */
public class Sensor extends Entity {

    public Interaction onCollision = null;

    public Sensor(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        Rectangle myRectangle = getRectangle(new Rectangle());

        List<Entity> entities = getWorld().getEntities(myRectangle);
        entities.remove(this);

        if (!entities.isEmpty()) {
            onCollision.start(this, entities.get(0));
        }

    }

    public void setOnCollision(Interaction interaction) {
        onCollision = interaction;
    }
}
