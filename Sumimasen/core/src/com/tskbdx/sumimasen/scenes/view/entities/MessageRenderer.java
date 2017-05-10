package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Message;
import com.tskbdx.sumimasen.scenes.view.Tween;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static com.tskbdx.sumimasen.Sumimasen.getFont;
import static com.tskbdx.sumimasen.scenes.IntroScene.camera;
import static com.tskbdx.sumimasen.scenes.model.entities.Direction.*;
import static com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer.TILE_SIZE;

/**
 * When a entity talk, display a chat bubble
 * If he talks alone, this bubble appear above him
 * Else, it shows in the other side of his interlocutor
 */
class MessageRenderer implements Observer {

    private final Message message;
    private Direction receiverDirection; // direction when the message is sent
    private BitmapFont font = getFont(22, "OpenSans");
    private Vector3 onScreenPosition;
    private Vector3 onWorldPosition;
    private Vector3 startingPosition; // on camera
    private Map<Direction, Runnable> positionCalculator = new HashMap<>();
    private Texture background = new Texture("images/chatbubble.png");
    private Tween alphaTween = new Tween(Interpolation.smooth);
    // this batch draws without camera perspective
    private Batch screenBatch = new SpriteBatch();

    MessageRenderer(Message message) {
        this.message = message;
        message.addObserver(this);
        initPositionCalculations();
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("update");
        alphaTween.playWith(1, 0, message.getDuration());
        receiverDirection = message.getReceiver().getLastDirection();

        // Init position of the bubble
        if (receiverDirection != null) {
            calculatePositionFrom(receiverDirection);
        } else { // speaking alone, bubble above => same fx than :
            positionCalculator.get(Direction.NORTH).run();
        }
    }

    private void initScreenPosition(float x, float y) {
        onWorldPosition = new Vector3(x * TILE_SIZE, y * TILE_SIZE, 0);
        startingPosition = new Vector3(x * TILE_SIZE, y * TILE_SIZE, 0);
        onScreenPosition = camera.project(new Vector3(startingPosition.x, startingPosition.y, 0));
    }

    /**
     * Main method of rendering called by an EntityRenderer associated with this
     */
    void render(Batch worldBatch) {
        if (message != null && message.getContent() != null) {
            worldBatch.end();
            screenBatch.begin();

            updatePosition();
            draw(worldBatch);

            screenBatch.end();
            worldBatch.begin();
        }
    }

    /**
     * To do : use Gdx Label
     */
    private void draw(Batch worldBatch) {
        if (background != null && onScreenPosition != null) {
            drawBackground(worldBatch);
            drawMessage();
        }
    }

    // TO DO
    private void drawBackground(Batch worldBatch) {
        screenBatch.end();
        worldBatch.begin();
    //    worldBatch.draw(background, onWorldPosition.x, onWorldPosition.y - 50,
   //             message.getLength() * 6, (message.getLength() / 12) * 50);
        worldBatch.end();
        screenBatch.begin();
    }

    private void updatePosition() {
        if (onScreenPosition !=  null && onWorldPosition != null) {
            onScreenPosition = camera.project(new Vector3(startingPosition.x, startingPosition.y, 0));
            onWorldPosition = camera.unproject(new Vector3(onScreenPosition.x, onScreenPosition.y, 0));
        }
    }

    private void drawMessage() {
        font.setColor(1, 1, 1, alphaTween.getInterpolation());
        font.draw(screenBatch, message.getContent(),
                onScreenPosition.x,
                onScreenPosition.y,
                200,
                Align.left, true);
    }

    /**
     * Process the calculation for a direction
     *
     */
    private void calculatePositionFrom(Direction direction) {
        if (positionCalculator.containsKey(direction)) {
            positionCalculator.get(direction).run();
        }
    }

    /**
     * Associate each direction with an operation
     * /!\ these operations shall be improved
     */
    private void initPositionCalculations() {
        Entity sender = message.getSender();
        positionCalculator.put(SOUTH,
                () -> initScreenPosition(sender.getX(), sender.getY()));
        positionCalculator.put(NORTH,
                () -> initScreenPosition(sender.getX(),
                        sender.getY() + sender.getHeight() * 4));
        positionCalculator.put(EAST,
                () -> initScreenPosition(sender.getX() + sender.getWidth(),
                        sender.getY() + sender.getHeight() * 2));
        positionCalculator.put(WEST,
                () -> initScreenPosition(sender.getX() - sender.getWidth() * 3,
                        sender.getY() + sender.getWidth() * 2));
    }
}
