package com.tskbdx.sumimasen.scenes.model;

import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.ArrayList;

/**
 * Created by Sydpy on 4/27/17.
 */
public class World {

    private ArrayList<PolygonMapObject> walls = new ArrayList<PolygonMapObject>();
    private ArrayList<Entity> entities = new ArrayList<Entity>();

    public void addWall(PolygonMapObject wall) {
        walls.add(wall);
    }

    public void removeWall(PolygonMapObject wall) {
        walls.remove(wall);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void update(float dt) {

        for (Entity entity : entities) {

            Rectangle previousRect = new Rectangle(
                    entity.getX(), entity.getY(),
                    entity.getWidth(), entity.getHeight());

            entity.update(dt);

            for (PolygonMapObject wall : walls) {
                if(checkEntityWallCollision(entity, wall)) {
                    entity.setRectangle(previousRect);
                }
            }

            for (Entity e : entities) {
                if(e != entity && checkEntityEntityCollision(entity, e)) {
                    entity.setRectangle(previousRect);
                }
            }
        }
    }

    private boolean checkEntityWallCollision(Entity entity, PolygonMapObject wall) {
        Polygon polygon = wall.getPolygon();

        return polygon.contains(entity.getX(), entity.getY())
                || polygon.contains(entity.getX(), entity.getY() + entity.getHeight())
                || polygon.contains(entity.getX() + entity.getWidth(), entity.getY())
                || polygon.contains(entity.getX()+ entity.getWidth(), entity.getY() + entity.getHeight());
    }

    private boolean checkEntityEntityCollision(Entity e1, Entity e2) {
        return e1.getRectangle().overlaps(e2.getRectangle());
    }

}
