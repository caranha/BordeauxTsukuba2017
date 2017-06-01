package com.tskbdx.sumimasen.scenes.model.entities;

import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Dialogue;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;
import com.tskbdx.sumimasen.scenes.utility.Utility;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Sydpy on 4/28/17.
 */
public class Entity extends Observable implements Serializable {

    /**
     * Message
     */
    private final Message message = new Message(this);
    private World world;
    private Movement movement;
    private Interaction interaction;
    private Interaction onCollide;
    private int x, y;
    private int width, height;
    private String name;
    private boolean isInteracting = false;
    private Inventory inventory = new Inventory();
    /**
     * dd
     * direction is the current direction movement state
     * lastDirection is like the static direction state
     */
    private Direction direction = Direction.NONE;
    private Direction lastDirection = Direction.SOUTH; // by default

    //Number of cell per sec
    private int speed = 8;
    private Dialogue nextInteraction;

    private Set<String> tags = new HashSet<>();

    public Entity() {
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    /**
     * Can only interact if there is a SceneObject
     * in front of the entity
     */
    public boolean tryInteract() {
        List<Entity> neighbors = getInFrontOfNeighbors();
        for (Entity neighbour : neighbors) {
            if (neighbour != null) {

                if (neighbour.isInteractable()) {
                    neighbour.getInteraction().start(neighbour, this);
                }

                return true;
            }
        }
        return false;
    }

    private List<Entity> getInFrontOfNeighbors() {
        List<Entity> neighbors = new ArrayList<>();

        switch (getLastDirection()) {
            case WEST:
                for (int j = getY(); j < getY() + getHeight(); ++j) {
                    neighbors.add(world.getEntity(getX() - 1, j));
                }
                break;
            case EAST:
                for (int j = getY(); j < getY() + getHeight(); ++j) {
                    neighbors.add(world.getEntity(getX() + getWidth() + 1, j));
                }
                break;
            case NORTH:
                for (int i = getX(); i < getX() + getWidth(); ++i) {
                    neighbors.add(world.getEntity(i, getY() + getHeight()));
                }
                break;
            case SOUTH:
                for (int i = getX(); i < getX() + getWidth(); ++i) {
                    neighbors.add(world.getEntity(i, getY() - 1));
                }
                break;
        }

        return neighbors;
    }

    public int getX() {
        return x;
    }

    public void moveTo(int x, int y) {

        int prevX = this.x;
        int prevY = this.y;

        this.x = x;
        this.y = y;

        if (world != null) world.moveEntity(this, prevX, prevY);
        setChanged();
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        setChanged();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        setChanged();
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
        setChanged();
    }

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
        setChanged();
    }

    public final Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        if (!direction.equals(Direction.NONE)) {
            lastDirection = direction;
        }
        this.direction = direction;
        setChanged();
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Direction getLastDirection() {
        return lastDirection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setChanged();
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(String content,
                           float timeToAnswer, Entity receiver) {
        message.setContent(content);
        message.setTimeToAnswer(timeToAnswer);
        message.setTimeToUnderstand(
                Math.min(Utility.getWordCount(content) * .44f, 3.2f));
        message.setReceiver(receiver);
        message.notifyObservers();
        setChanged();
        if (receiver != null) {
            receiver.setChanged();
        }
    }

    public void setMessage(String content,
                           float timeToAnswer, Entity receiver, boolean important) {
        message.setImportant(important);
        setMessage(content, timeToAnswer, receiver);
    }

    private boolean isInteractable() {
        return interaction != null;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
        setChanged();
    }

    public boolean isInteracting() {
        return isInteracting;
    }

    public void setInteracting(boolean interacting) {
        isInteracting = interacting;
        setChanged();
    }

    public void store(Entity entity) {
        inventory.store(entity);
        setChanged();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Interaction getOnCollide() {
        return onCollide;
    }

    public void setOnCollide(Interaction onCollide) {
        this.onCollide = onCollide;
        setChanged();
    }

    public void move(Direction direction) {
        setDirection(direction);

        if (movement != null) {

            movement.move(this);
            if (world != null) {

                List<Entity> entitiesAround = world.getEntitiesAround(this);

                if (!entitiesAround.isEmpty()) {

                    for (Entity entity : entitiesAround) {
                        if (entity != this && entity.getOnCollide() != null) {
                            entity.getOnCollide().start(entity, this);
                        }
                    }

                }
            }
        }


    }

    public boolean has(String object) {
        Entity entity = world.getEntityByName(object);
        return getInventory().contains(entity);
    }

    public boolean isWalking() {
        return direction != Direction.NONE && movement != null;
    }

    public void think(String content) {
        setMessage(content,
                0.3f, null, true);
    }

    public Interaction getNextInteraction() {
        return nextInteraction;
    }

    public void setNextInteraction(Dialogue nextInteraction) {
        this.nextInteraction = nextInteraction;
    }
}
