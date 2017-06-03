package com.tskbdx.sumimasen.scenes.model.entities.movements;

import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.LinkedList;
import java.util.List;

/*
 * Created by viet khang on 03/06/2017.
 */
public class Reach implements Movement {

    private final Entity toReach;
    private List<Direction> bestPath = null;

    public Reach(Entity toReach) {
        this.toReach = toReach;
    }

    @Override
    public void move(Entity entity) {

        Mover mover = new Mover(entity, toReach);
        for (Direction direction : mover.bestNextDirections(null)) {
            test(mover, direction);
        }

        new Path(false,
                    bestPath.toArray(new Direction[bestPath.size()])).move(entity);
    }

    private void test(Mover mover, Direction direction) {

        switch (mover.move(direction)) {

            case FAIL:

                Direction last = mover.getPath().removeLast();
                for (Direction direction1 :
                        mover.bestNextDirections(last)) {
                    test(mover, direction1);
                }

                break;

            case CONTINUE:

                for (Direction direction1 :
                        mover.bestNextDirections(mover.getPath().getLast())) {
                    test(mover, direction1);
                }

                break;

            case SUCCESS:

                List<Direction> path = mover.getPath();

                if (bestPath == null ||
                        path.size() < path.size()) {
                    bestPath = mover.getPath();
                }

                break;
        }
    }

    private static final class Mover {
        final int w, h, startX, startY;
        final Entity entity, toReach;
        int x, y;
        LinkedList<Direction> traveled = new LinkedList<>();

        Mover(Entity entity, Entity toReach) {
            this.x = startX = entity.getX();
            this.y = startY = entity.getY();
            this.w = entity.getWidth();
            this.h = entity.getHeight();
            this.entity = entity;
            this.toReach = toReach;
        }

        Progression move(Direction direction) {
            traveled.add(direction);

            int newX = x, newY = y;
            switch (direction) {
                case EAST:
                    ++newX;
                    break;
                case WEST:
                    --newX;
                    break;
                case NORTH:
                    ++newY; //--newY;
                    break;
                case SOUTH:
                    --newY; //++newY;
                    break;
            }

            List<Entity> entityColliding = entity.getWorld().getEntities(newX, newY, w, h);
            entityColliding.remove(entity);

            System.out.println(entityColliding.size());
            if (entityColliding.contains(toReach)) {
                return Progression.SUCCESS;
            } else if (entity.getWorld().isWall(newX, newY, w, h) ||
                    !(entityColliding.isEmpty())) {
                return Progression.FAIL;
            } else {
                x = newX;
                y = newY;
                return Progression.CONTINUE;
            }
        }

        LinkedList<Direction> getPath() {
            return traveled;
        }

        List<Direction> bestNextDirections(Direction notIncluded) {
            List<Direction> directions = new LinkedList<>();
            int offsetX = toReach.getX() - x,
                    offetY = toReach.getY() - y;
            if (Math.abs(offsetX) < Math.abs(offetY)) {

                if (y < toReach.getY()) {
                    directions.add(Direction.NORTH);
                    if (x < toReach.getX()) {
                        directions.add(Direction.EAST);
                        directions.add(Direction.WEST);
                    } else {
                        directions.add(Direction.WEST);
                        directions.add(Direction.EAST);
                    }
                    directions.add(Direction.SOUTH);
                } else {
                    directions.add(Direction.SOUTH);
                    if (x < toReach.getX()) {
                        directions.add(Direction.EAST);
                        directions.add(Direction.WEST);
                    } else {
                        directions.add(Direction.WEST);
                        directions.add(Direction.EAST);
                    }
                    directions.add(Direction.NORTH);
                }

                if (x < toReach.getX()) {
                    directions.add(Direction.EAST);
                    if (y < toReach.getY()) {
                        directions.add(Direction.NORTH);
                        directions.add(Direction.SOUTH);
                    } else {
                        directions.add(Direction.NORTH);
                        directions.add(Direction.SOUTH);
                    }
                    directions.add(Direction.WEST);
                } else {
                    directions.add(Direction.WEST);
                    if (y < toReach.getY()) {
                        directions.add(Direction.NORTH);
                        directions.add(Direction.SOUTH);
                    } else {
                        directions.add(Direction.NORTH);
                        directions.add(Direction.SOUTH);
                    }
                    directions.add(Direction.EAST);
                }

            } else {
                directions.add(x < toReach.getX() ? Direction.EAST : Direction.WEST);
                directions.add(y < toReach.getY() ? Direction.NORTH : Direction.SOUTH);
            }

            if (notIncluded != null) {
                directions.remove(notIncluded);
            }

            return directions;
        }

        enum Progression {
            FAIL,
            CONTINUE,
            SUCCESS
        }
    }
}
