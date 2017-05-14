package com.tskbdx.sumimasen.scenes.model.entities;

/**
 * Created by Sydpy on 5/2/17.
 */

import sun.security.pkcs11.Secmod;

import java.util.HashMap;
import java.util.Map;

/**
 * Changed to S W N E (because of collisions with keyboard keys)
 */
public enum Direction {
    NORTH,
    SOUTH,
    WEST,
    EAST,
    NONE;

    private static Map<Direction, Direction> opposites = new HashMap<>();
    static {
        opposites.put(WEST, EAST);
        opposites.put(EAST, WEST);
        opposites.put(NORTH, SOUTH);
        opposites.put(SOUTH, NORTH);
        opposites.put(NONE, NONE);
    }

    public static Direction getOpposite(Direction direction) {
        return opposites.get(direction);
    }

    public boolean isHorizontal() {
        return equals(WEST) || equals(EAST);
    }
}