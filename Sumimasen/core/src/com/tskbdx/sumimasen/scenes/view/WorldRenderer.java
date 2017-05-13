package com.tskbdx.sumimasen.scenes.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRendererDrawOrderer;
import com.tskbdx.sumimasen.scenes.view.entities.MessageRenderer;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Sydpy on 4/28/17.
 */
public class WorldRenderer extends OrthogonalTiledMapRenderer {

    private ArrayList<EntityRenderer> entityRenderers = new ArrayList<EntityRenderer>();
    private ArrayList<MessageRenderer> messageRenderers = new ArrayList<>();
    private Batch screenBatch = new SpriteBatch();

    public WorldRenderer(TiledMap map) {
        super(map);
    }

    public void addEntityRenderer(EntityRenderer entityRenderer) {
        entityRenderers.add(entityRenderer);
    }

    public void removeEntityRenderer(EntityRenderer entityRenderer) {
        entityRenderers.remove(entityRenderer);
    }

    public void addMessageRenderer(MessageRenderer messageRenderer) { messageRenderers.add(messageRenderer); }

    @Override
    public void render() {
        Collections.sort(entityRenderers, new EntityRendererDrawOrderer());

        beginRender();

        for (MapLayer layer : map.getLayers()) {

            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderTileLayer((TiledMapTileLayer) layer);
                } else if (layer.getName().equals("Entities")) {
                    for (EntityRenderer entityRenderer: entityRenderers) {
                        entityRenderer.render( getBatch() );
                    }
                }
            }
        }

        endRender();

       screenBatch.begin();
        for (MessageRenderer messageRenderer : messageRenderers) {
            messageRenderer.render(screenBatch);
        }
        screenBatch.end();
    }
}
