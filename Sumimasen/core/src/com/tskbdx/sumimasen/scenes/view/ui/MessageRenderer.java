package com.tskbdx.sumimasen.scenes.view.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Message;
import com.tskbdx.sumimasen.scenes.view.Tween;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.tskbdx.sumimasen.Sumimasen.getFont;
import static com.tskbdx.sumimasen.scenes.IntroScene.camera;
import static com.tskbdx.sumimasen.scenes.model.entities.Direction.*;
import static com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer.TILE_SIZE;

/**
 * When a entity talk, display a chat bubble
 * If he talks alone, this bubble scaleTween above him
 * Else, it shows in the other side of his interlocutor
 *
 * Public method :
 * + update() (from Obserrver)
 * + render(Batch) (Main method)
 * + dispose() (from disposable)
 */
final class MessageRenderer implements Observer, Disposable {

    private final Message message;
    private final GlyphLayout layout = new GlyphLayout();
    private final BitmapFont font = getFont(19, "OpenSans");
    private String content;
    private Direction direction;
    private Vector2 onScreenPosition;
    private Vector2 startingPosition; // on world
    private Map<Direction, Runnable> positionCalculator = new HashMap<>();
    private Map<Direction, Runnable> offsetCalculator = new HashMap<>();
    private Texture background = new Texture("images/chatbubble.png");
    private Tween alphaTween = new Tween(Interpolation.smooth);
    private Tween bounceTween = new Tween(Interpolation.bounceOut);
    private boolean firstTime = true;
    private float targetWidth;
    private float padding = 10.f;

    MessageRenderer(Message message) {
        this.message = message;
        message.addObserver(this);
        initPositionCalculations();
        initOffsetCalculations();
    }

    @Override
    public void dispose() {
        font.dispose();
        background.dispose();
    }

    @Override
    public void update(Observable o, Object arg) {
        // Get message content
        Entity receiver = message.getReceiver();
        content = message.getContent();

        if (!content.equals("")) {
            // Set layout
            direction = message.getSender().getLastDirection();
            targetWidth = getTargetWidth(direction.isHorizontal());
            layout.setText(font, content, BLACK, targetWidth,
                    Align.center, true);

            // Calculate position
            if (receiver != null) {
                execute(positionCalculator, direction);
            } else { // speaking alone, bubble above => same fx than :
                execute(positionCalculator, SOUTH);
            }

            // Start tweens
            alphaTween.playWith(1, message.getTimeToAnswer() != 0.f ? 0 : 1, message.getTimeToAnswer(), message.getTimeToUnderstand(), true);
            if (firstTime && !message.getSender().getName().equals("player")) {
                bounceTween.playWith(0, 1, 0.5f, 0.f, true);
                firstTime = false;
            }
        }
    }

    /**
     * Main method of rendering
     */
    public void render(Batch screenBatch) {
        if (update()) {
            // set alpha
            screenBatch.setColor(1, 1, 1, alphaTween.getInterpolation());

            // draw
            drawBackground(screenBatch);
            drawMessage(screenBatch);
        }
    }


    private float getTargetWidth(boolean horizontal) {
        float scale = horizontal ? 0.4f : 1.f;
        return 400.f * scale;
    }

    private void initScreenPosition(float x, float y) {
        startingPosition = new Vector2(x * TILE_SIZE, y * TILE_SIZE);
        onScreenPosition = toScreenPosition(startingPosition.x, startingPosition.y);
        onScreenPosition.x -= targetWidth / 2;
    }

    private void drawBackground(Batch screenBatch) {
        float interpolation = bounceTween.isPlaying() ? bounceTween.getInterpolation() : 1;
        float x = onScreenPosition.x + (targetWidth - layout.width) / 2 - padding,
                y = onScreenPosition.y - layout.height - padding,
                w = (layout.width + padding * 2),
                h = (layout.height + padding * 2);
        screenBatch.draw(background,
                x + (1 - interpolation) * (padding + layout.width / 2),
                y + (1 - interpolation) * (padding + layout.height / 2),
                w * interpolation,
                h * interpolation);
    }

    private void drawMessage(Batch screenBatch) {
        font.draw(screenBatch, layout, onScreenPosition.x, onScreenPosition.y);
    }

    private boolean update() {
        if (message != null && content != null &&
                onScreenPosition != null && !content.equals("")) {
            Color processedColor = new Color(0, 0, 0, alphaTween.getInterpolation());
            layout.setText(font, content, processedColor,
                    getTargetWidth(direction.isHorizontal()), Align.center, true);
            onScreenPosition = toScreenPosition(startingPosition.x, startingPosition.y);
            padding = content.length() < 10 && direction.equals(EAST) ? 20.f : 10.f;
            execute(offsetCalculator, direction);
            return true;
        }
        return false;
    }

    private Vector2 toScreenPosition(float x, float y) {
        Vector3 result = camera.project(new Vector3(x, y, 0));
        return new Vector2(result.x, result.y);
    }

    /**
     * Process the calculation for a direction
     */
    private void execute(Map<Direction, Runnable> map, Direction direction) {
        if (map.containsKey(direction)) {
            map.get(direction).run();
        }
    }

    /**
     * Associate each direction with an operation
     */
    private void initPositionCalculations() {
        Entity sender = message.getSender();
        positionCalculator.put(NORTH,
                () -> initScreenPosition(sender.getX() + sender.getWidth() * 0.5f,
                        sender.getY()));
        positionCalculator.put(SOUTH,
                () -> initScreenPosition(sender.getX() + sender.getWidth() * 0.5f,
                        sender.getY() + sender.getHeight() * 2));
        positionCalculator.put(WEST,
                () -> initScreenPosition(sender.getX() + sender.getWidth(),
                        sender.getY() + sender.getHeight()));
        positionCalculator.put(EAST,
                () -> initScreenPosition(sender.getX(),
                        sender.getY() + sender.getHeight()));
    }

    private void initOffsetCalculations() {
        offsetCalculator.put(NORTH, () -> onScreenPosition.add(-targetWidth / 2, -padding * 3));
        offsetCalculator.put(SOUTH, () -> onScreenPosition.add(-targetWidth / 2, layout.height + padding * 4));
        offsetCalculator.put(EAST, () -> onScreenPosition.add(-Math.min(targetWidth, layout.width) - padding * 5, layout.height / 2));
        offsetCalculator.put(WEST, () -> onScreenPosition.add(padding * 3, layout.height / 2));
    }
}
