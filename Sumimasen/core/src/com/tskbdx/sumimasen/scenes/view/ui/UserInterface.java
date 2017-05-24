package com.tskbdx.sumimasen.scenes.view.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by viet khang on 15/05/2017.
 */

/**
 * UserInterface
 */
public class UserInterface extends Stage implements Disposable {

    private final InventoryRenderer inventoryRenderer;
    private final List<MessageRenderer> messageRenderers = new ArrayList<>();
    private final Group textButtons;

    public UserInterface(World world, Entity entity) {
        List<Entity> entities = world.getEntities();
        entities.forEach(worldEntity ->
                messageRenderers.add(new MessageRenderer(worldEntity.getMessage())));
        inventoryRenderer = new InventoryRenderer(entity.getInventory());
        textButtons = new AnswersSelector(entity, this);
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
        messageRenderers.forEach(MessageRenderer::dispose);
        inventoryRenderer.dispose();
        super.dispose();
    }

    @Override
    public void draw() {
        getBatch().begin();
        messageRenderers.forEach(message -> message.render(getBatch()));
        inventoryRenderer.render(getBatch());
        textButtons.draw(getBatch(), 1);
        getBatch().end();
    }
}
