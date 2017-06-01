package com.tskbdx.sumimasen.scenes.view.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.tskbdx.sumimasen.scenes.Scene;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.*;

/*
 * Created by viet khang on 15/05/2017.
 */

/**
 * UserInterface
 */
final public class UserInterface extends Stage implements Disposable, Observer {

    private static UserInterface instance;
    private final Map<Entity, MessageRenderer> messageRendererByEntity = new HashMap<>();
    private final Group textButtons;
    private final InventoryRenderer inventoryRenderer;

    private Scene scene;

    private UserInterface(Scene scene, Entity entity) {
        this.scene = scene;
        textButtons = new AnswersSelector(entity, this);
        inventoryRenderer = new InventoryRenderer(entity.getInventory(), scene);
        messageRendererByEntity.put(entity,
                new MessageRenderer(entity.getMessage(), scene.getCamera()));

        scene.getWorld().addObserver(this);
    }

    public static void setScene(Scene scene) {
        if (getInstance() != null) {
            UserInterface userInterface = getInstance();
            userInterface.scene = scene;
            userInterface.scene.getWorld().addObserver(userInterface);
        }
    }

    public static void init(Scene scene, Entity entity) {
        instance = new UserInterface(scene, entity);

        scene.getWorld().addObserver(instance);
    }

    public static UserInterface getInstance() {
        return instance;
    }

    @Override
    public void act(float dt) {
        try {
            super.act(dt);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void dispose() {
        for (MessageRenderer messageRenderer : messageRendererByEntity.values()) {
            messageRenderer.dispose();
        }

        inventoryRenderer.dispose();
        super.dispose();
    }

    @Override
    public void draw() {
        getBatch().begin();

        for (MessageRenderer messageRenderer : messageRendererByEntity.values()) {
            messageRenderer.render(getBatch());
        }

        inventoryRenderer.render(getBatch());
        textButtons.draw(getBatch(), 1);
        getBatch().end();
    }

    @Override
    public void update(Observable o, Object arg) {

        List<Entity> entities = scene.getWorld().getEntities();

        for (Entity e : entities) {
            if (!messageRendererByEntity.containsKey(e)) {
                messageRendererByEntity.put(e, new MessageRenderer(e.getMessage(), scene.getCamera()));
            }
        }
    }
}
