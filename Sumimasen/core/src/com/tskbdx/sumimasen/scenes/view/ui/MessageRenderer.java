package com.tskbdx.sumimasen.scenes.view.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.tskbdx.sumimasen.Sumimasen;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.model.entities.Message;
import com.tskbdx.sumimasen.scenes.view.Tween;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * When a entity talk, display a chat bubble
 * If he talks alone, this bubble scaleTween above him
 * Else, it shows in the other side of his interlocutor
 * <p>
 * Public method :
 * + update() (from Obserrver)
 * + render(Batch) (Main method)
 * + dispose() (from disposable)
 */
final class MessageRenderer implements Observer, Disposable, Serializable {

    private final Message message;
    private final GlyphLayout layout = new GlyphLayout();
    private final BitmapFont font = Sumimasen.getFont(19, "OpenSans");
    private final Camera camera;
    private final Sprite bubbleHorizontal = new Sprite(Sumimasen.getAssetManager().get("images/bubbleHorizontal.png", Texture.class));
    private final Sprite bubbleVertical = new Sprite(Sumimasen.getAssetManager().get("images/bubbleVertical.png", Texture.class));
    private Sprite currentBubble;
    private String content;
    private Direction direction;
    private Vector2 onScreenPosition;
    private Vector2 startingPosition; // on world
    private Map<Direction, Runnable> positionCalculator = new HashMap<>();
    private Map<Direction, Runnable> offsetCalculator = new HashMap<>();
    private Map<Direction, Runnable> rotationCalculator = new HashMap<>();
    private Tween alphaTween = new Tween(Interpolation.smooth);
    private Tween bounceTween = new Tween(Interpolation.bounceOut);
    private boolean firstTime = true;
    private float targetWidth;
    private float padding = 10.f;

    MessageRenderer(Message message, Camera camera) {
        this.message = message;
        this.camera = camera;
        message.addObserver(this);
        initPositionCalculations();
        initOffsetCalculations();
        initRotationCalculator();
    }

    @Override
    public void dispose() {
        font.dispose();
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
            layout.setText(font, content, Color.BLACK, targetWidth,
                    Align.center, true);

            // Calculate position and bubble rotation
            if (receiver != null) {
                execute(positionCalculator, direction);
                execute(rotationCalculator, direction);
                currentBubble = direction.isHorizontal() ? bubbleHorizontal : bubbleVertical;
            } else { // speaking alone, bubble above => same fx than :
                execute(positionCalculator, Direction.SOUTH);
                execute(rotationCalculator, Direction.SOUTH);
                currentBubble = bubbleVertical;
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
            drawBubble(screenBatch);
            drawMessage(screenBatch);
        }
    }


    private float getTargetWidth(boolean horizontal) {
        float scale = horizontal ? 0.4f : 1.f;
        return 400.f * scale;
    }

    private void initScreenPosition(float x, float y) {
        startingPosition = new Vector2(x * EntityRenderer.TILE_SIZE, y * EntityRenderer.TILE_SIZE);
        onScreenPosition = toScreenPosition(startingPosition.x, startingPosition.y);
        onScreenPosition.x -= targetWidth / 2;
    }

    private void drawBubble(Batch screenBatch) {
        float interpolation = bounceTween.isPlaying() ? bounceTween.getInterpolation() : 1;
        float x = onScreenPosition.x + (targetWidth - layout.width) / 2 - padding + (1 - interpolation) * (padding + layout.width / 2),
                y = onScreenPosition.y - layout.height - padding + (1 - interpolation) * (padding + layout.height / 2),
                w = (layout.width + padding * 2) * interpolation,
                h = (layout.height + padding * 2) * interpolation;
        screenBatch.draw(currentBubble, x, y,w * .5f,h * .5f, w, h,
                1, 1, currentBubble.getRotation());
    }

    private void drawMessage(Batch screenBatch) {
        float deltaX = 0.f;
        if (direction.equals(Direction.WEST)) {
            deltaX = padding * .5f;
        }
        font.draw(screenBatch, layout, onScreenPosition.x + deltaX, onScreenPosition.y);
    }

    private boolean update() {
        if (message != null && content != null &&
                onScreenPosition != null && !content.equals("")) {
            Color processedColor = new Color(0, 0, 0, alphaTween.getInterpolation());
            layout.setText(font, content, processedColor,
                    getTargetWidth(direction.isHorizontal()), Align.center, true);
            onScreenPosition = toScreenPosition(startingPosition.x, startingPosition.y);
            padding = content.length() < 10 && direction.equals(Direction.EAST) ? 20.f : 10.f;
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
        positionCalculator.put(Direction.NORTH,
                () -> initScreenPosition(sender.getX() + sender.getWidth() * 0.5f,
                        sender.getY()));
        positionCalculator.put(Direction.SOUTH,
                () -> initScreenPosition(sender.getX() + sender.getWidth() * 0.5f,
                        sender.getY() + sender.getHeight() * 2));
        positionCalculator.put(Direction.WEST,
                () -> initScreenPosition(sender.getX() + sender.getWidth(),
                        sender.getY() + sender.getHeight()));
        positionCalculator.put(Direction.EAST,
                () -> initScreenPosition(sender.getX(),
                        sender.getY() + sender.getHeight()));
    }

    private void initOffsetCalculations() {
        offsetCalculator.put(Direction.NORTH, () -> onScreenPosition.add(-targetWidth / 2, -padding * 3));
        offsetCalculator.put(Direction.SOUTH, () -> onScreenPosition.add(-targetWidth / 2, layout.height + padding * 4));
        offsetCalculator.put(Direction.EAST, () -> onScreenPosition.add(-Math.min(targetWidth, layout.width) - padding * 5, layout.height / 2));
        offsetCalculator.put(Direction.WEST, () -> onScreenPosition.add(padding * 3, layout.height / 2));
    }

    private void initRotationCalculator() {
        rotationCalculator.put(Direction.SOUTH, () -> bubbleVertical.setRotation(0));
        rotationCalculator.put(Direction.EAST, () -> bubbleHorizontal.setRotation(0));
        rotationCalculator.put(Direction.NORTH, () -> bubbleVertical.setRotation(180));
        rotationCalculator.put(Direction.WEST, () -> bubbleHorizontal.setRotation(180));
    }
}
