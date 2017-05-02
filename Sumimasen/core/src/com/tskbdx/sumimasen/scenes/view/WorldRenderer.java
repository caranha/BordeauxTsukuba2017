package com.tskbdx.sumimasen.scenes.view;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRendererDrawOrderer;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Sydpy on 4/28/17.
 */
public class WorldRenderer extends OrthogonalTiledMapRenderer {

    private ArrayList<EntityRenderer> entityRenderers = new ArrayList<EntityRenderer>();

    public WorldRenderer(TiledMap map) {
        super(map);
    }

    public void addEntityRenderer(EntityRenderer entityRenderer) {
        entityRenderers.add(entityRenderer);
    }

    public void removeEntityRenderer(EntityRenderer entityRenderer) {
        entityRenderers.remove(entityRenderer);
    }

    @Override
    public void render() {
        Collections.sort(entityRenderers, new EntityRendererDrawOrderer());

        beginRender();

        int currentLayer = 0;
        for (MapLayer layer : map.getLayers()) {

            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderTileLayer((TiledMapTileLayer) layer);
                    currentLayer++;

                    if (currentLayer == 1) {

                        for (EntityRenderer entityRenderer: entityRenderers) {
                            entityRenderer.render( getBatch() );
                        }
                    }
                }
            }
        }

        endRender();
    }
}
