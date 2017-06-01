package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.badlogic.gdx.Gdx;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.inputprocessors.GameCommands;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;
import com.tskbdx.sumimasen.scenes.utility.Utility;

/**
 * Created by Sydpy on 5/17/17.
 */
public class ChangeMap extends Interaction {

    public static final float DELAY = 2.f;

    private String mapName;
    private String spawnName;

    public ChangeMap(String mapName, String spawnName) {
        this.mapName = mapName;
        this.spawnName = spawnName;
    }

    @Override
    public void start(Entity active, Entity passive) {

        super.start(active, passive);

        Movement backup = passive.getMovement();
        passive.setMovement(null);
        passive.setDirection(Direction.NONE);

        passive.notifyObservers(ChangeMap.class);
        Gdx.input.setInputProcessor(null);

        Utility.setTimeout(() -> {
            GameScreen.getCurrentScene().loadMap(mapName, spawnName);
            passive.setMovement(backup);
            passive.notifyObservers();
            Gdx.input.setInputProcessor(GameCommands.getInstance());
            end();
        }, DELAY);

    }
}
