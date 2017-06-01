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
//TODO: Rethink this class
public class UserInterface extends Stage implements Disposable, Observer {

    private final Map<Entity, MessageRenderer> messageRendererByEntity = new HashMap<>();
    private final Group textButtons;
    private final InventoryRenderer inventoryRenderer;

    private final Scene scene;

    public UserInterface(Scene scene, Entity entity) {

        this.scene = scene;

        textButtons = new AnswersSelector(entity, this);
        inventoryRenderer = new InventoryRenderer(entity.getInventory(), scene);
        messageRendererByEntity.put(entity,
                new MessageRenderer(entity.getMessage(), scene.getCamera()));
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

    public void init() {
        scene.getWorld().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {

        List<Entity> entities = scene.getWorld().getEntitiesInCurrentMap();

        for (Entity e : entities) {
            if (! messageRendererByEntity.containsKey(e)) {
                messageRendererByEntity.put(e, new MessageRenderer(e.getMessage(), scene.getCamera()));
            }
        }
    }
}
