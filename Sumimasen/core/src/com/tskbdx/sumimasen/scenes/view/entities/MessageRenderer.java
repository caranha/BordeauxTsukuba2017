package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.view.Tween;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.graphics.Color.WHITE;
import static com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled;
import static com.tskbdx.sumimasen.scenes.IntroScene.camera;
import static com.tskbdx.sumimasen.scenes.model.entities.Direction.*;
import static com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer.TILE_SIZE;

/**
 * Created by viet khang on 09/05/2017.
 */

/**
 * When a entity talk, display a chat bubble
 * If he talks alone, this bubble appear above him
 * Else, it shows in the other side of his interlocutor
 */
class MessageRenderer implements Observer {

    private final Entity sender;
    private String message = null;
    private Entity receiver;
    private BitmapFont font = getFont(22, "OpenSans");
    private Vector3 onScreenPosition; // compared to camera point of view
    private Map<Direction, Runnable> positionCalculator = new HashMap<>();
    private ShapeRenderer background = new ShapeRenderer();
    private Tween alphaColorTween = new Tween(Interpolation.smooth);

    MessageRenderer(Entity sender) {
        this.sender = sender;
        sender.addObserver(this);
        initPositionCalculations();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (sender.getMessage() != null && !sender.getMessage().equals(message)) {
            font.setColor(BLACK);
            background.setColor(WHITE);
            message = sender.getMessage();
            alphaColorTween.playWith(1, 0, sender.getMessageDuration());
            receiver = sender.getMessageReceiver();
        }
    }

    /**
     * Main method of rendering called by an EntityRenderer associated with this
     * @param batch
     */
    void render(Batch batch) {
        if (message != null) {
            batch.end();
            screenBatch.begin();

            if (receiver != null) {
                calculatePositionFrom(receiver.getLastDirection());
            } else { // speaking alone, bubble above => same fx than :
                positionCalculator.get(Direction.NORTH).run(); //
            }

            drawBackground();
            draw();

            screenBatch.end();
            batch.begin();
        }
    }

    /**
     * To do : use Gdx Label
     */
    private void draw() {
        drawBackground();
        drawMessage();
    }

    private void drawBackground() {
        screenBatch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        background.begin(Filled);
        float interpolation = alphaColorTween.getInterpolation();
        background.setColor(new Color(1, 1,
                1, interpolation));
        background.rect(onScreenPosition.x, onScreenPosition.y - 50,
                message.length() * 6, (message.length() / 12) * 50);
        background.end();
        screenBatch.begin();
    }

    private void drawMessage() {
        font.setColor(new Color(0, 0, 0, alphaColorTween.getInterpolation()));
        font.draw(screenBatch, message,
                onScreenPosition.x,
                onScreenPosition.y,
                200,
                Align.left, true);
    }

    /**
     * Convert camera position to screen position
     * @param x
     * @param y
     * @return
     */
    private Vector3 toScreenPosition(int x, int y) {
        return camera.project(new Vector3(x * TILE_SIZE, y * TILE_SIZE, 0));
    }

    /**
     * Process the calculation for a direction
     * @param direction
     */
    private void calculatePositionFrom(Direction direction) {
        if (positionCalculator.containsKey(direction)) {
            positionCalculator.get(direction).run();
        }
    }

    // this batch draws without camera perspective
    private Batch screenBatch = new SpriteBatch();

    /**
     * @param size
     * @param name
     * @return a generated quality font at a certain size (no scaling)
     */
    private BitmapFont getFont(int size, String name) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + name + ".ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        return font;
    }

    /**
     * Associate each direction with an operation
     * /!\ these operations shall be improved
     */
    private void initPositionCalculations() {
        positionCalculator.put(SOUTH,
                () -> onScreenPosition = toScreenPosition(sender.getX(), sender.getY()));
        positionCalculator.put(NORTH,
                () -> onScreenPosition = toScreenPosition(sender.getX(),
                        sender.getY() + sender.getHeight() * 4));
        positionCalculator.put(EAST,
                () -> onScreenPosition = toScreenPosition(sender.getX() + sender.getWidth(),
                        sender.getY() + sender.getHeight() * 2));
        positionCalculator.put(WEST,
                () -> onScreenPosition = toScreenPosition(sender.getX() - sender.getWidth() * 3,
                        sender.getY() + sender.getWidth() * 2));
    }
}
