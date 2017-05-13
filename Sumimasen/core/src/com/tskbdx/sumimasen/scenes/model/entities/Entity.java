package com.tskbdx.sumimasen.scenes.model.entities;

import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.interactions.Interaction;
import com.tskbdx.sumimasen.scenes.model.entities.movements.Movement;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static com.tskbdx.sumimasen.scenes.model.entities.Direction.*;

/**
 * Created by Sydpy on 4/28/17.
 */
public abstract class Entity extends Observable {

    private World world;

    private Movement movement;
    private Interaction interaction;

    private int x, y;
    private int width, height;
    private String name;

    private boolean isInteracting = false;
    private Entity interactingWith = null;

    private List<Entity> stored = new ArrayList<>();

    /**
     * Message
     */
    private Message message = new Message(this);
    /**
     * direction is the current direction movement state
     * lastDirection is like the static direction state
     */
    private Direction direction;
    private Direction lastDirection = SOUTH; // by default

    //Number of cell per sec
    private int speed = 8;

    public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.direction = NONE;
        this.movement = null;
    }

    public void update(float dt) {
        if (movement != null) {
            movement.move(dt);
        }

        if (interaction != null
                && interaction.isStarted()
                && !interaction.isFinished()) {
            interaction.update();
        }
    }


    /**
     * Can only interact if there is a SceneObject
     * in front of the entity
     */
    public void tryInteract() {
        List<Object> neighbors = getInFrontOfNeighbors();
        for (Object neighbour : neighbors) {
            if (neighbour instanceof Entity) {

                Entity entity = (Entity) neighbour;

                if (entity.getInteraction() != null) {
                    // change target direction to face this
                    entity.setDirection(getOpposite(getLastDirection()));
                    entity.notifyObservers();
                    entity.getInteraction().start();
                }

                return;
            }
        }
        System.out.println("Nobody to interact with !");
    }

    private List<Object> getInFrontOfNeighbors() {
        List<Object> neighbors = new ArrayList<>();
        switch (getLastDirection()) {
            case WEST:
                for (int j = getY(); j != getY() + getHeight(); ++j)
                    neighbors.add(world.get(getX() - 1, j));
                break;
            case EAST:
                for (int j = getY(); j != getY() + getHeight(); ++j)
                    neighbors.add(world.get(getX() + getWidth() + 1, j));
                break;
            case NORTH:
                for (int i = getX(); i != getX() + getWidth(); ++i)
                    neighbors.add(world.get(i, getY() + getHeight()));
                break;
            case SOUTH:
                for (int i = getX(); i != getX() + getWidth(); ++i)
                    neighbors.add(world.get(i, getY() - 1));
                break;
        }
        return neighbors;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
        setChanged();
    }

    public void moveTo(int x, int y) {
        world.setEntityLocation(this, null);
        setX(x);
        setY(y);
        world.setEntityLocation(this, this);
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
        setChanged();
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
        if (direction != NONE) {
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
        setChanged();
    }

    public Direction getLastDirection() {
        return lastDirection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(String content, float timeToUnderstand,
                           float timeToAnswer, Entity receiver) {
        message.setContent(content);
        message.setTimeToAnswer(timeToAnswer);
        message.setTimeToUnderstand(timeToUnderstand);
        message.setReceiver(receiver);
        setChanged();
    }

    public boolean isInteractable() {
        return interaction != null;
    }

    public Interaction getInteraction() {
        return interaction;
    }

    public void setInteraction(Interaction interaction) {
        this.interaction = interaction;
    }

    public boolean isInteracting() {
        return isInteracting;
    }

    public void setInteracting(boolean interacting) {
        isInteracting = interacting;
    }

    public Entity getInteractingWith() {
        return interactingWith;
    }

    public void setInteractingWith(Entity interactingWith) {
        this.interactingWith = interactingWith;
    }

    public void store(Entity entity) {
        stored.add(entity);
        setChanged();
    }
}
