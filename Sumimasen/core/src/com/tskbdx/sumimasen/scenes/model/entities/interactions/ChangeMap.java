package com.tskbdx.sumimasen.scenes.model.entities.interactions;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.tskbdx.sumimasen.scenes.TiledMapUtils;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;
import com.tskbdx.sumimasen.scenes.utility.Utility;

import java.util.List;

/**
 * Created by Sydpy on 5/17/17.
 */
public class ChangeMap extends Interaction {

    public static final float DELAY = 2.f;

    private String mapName;

    public ChangeMap(String mapName) {
        this.mapName = mapName;
    }

    @Override
    public void start(Entity active, Entity passive) {

        super.start(active, passive);

        TiledMap tiledMap = new TmxMapLoader().load("maps/" + mapName + ".tmx");

        Movement backup = passive.getMovement();
        passive.setMovement(null);
        passive.setDirection(Direction.NONE);

        passive.notifyObservers(ChangeMap.class);

        Utility.setTimeout(() -> {
            List<TiledMapUtils.MapObjectMapping> mapObjectMappings = TiledMapUtils.mapObjectMappings(tiledMap);
            passive.getWorld().init(tiledMap, mapObjectMappings);
            passive.setMovement(backup);
            passive.notifyObservers();
            end();
        }, DELAY);

    }
}
