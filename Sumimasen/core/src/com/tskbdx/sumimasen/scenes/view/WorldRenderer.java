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

    private ArrayList<EntityRenderer> entityRenderers;

    public WorldRenderer(TiledMap map, ArrayList<EntityRenderer> entityRenderers) {
        super(map);

        this.entityRenderers = entityRenderers;
    }

    @Override
    public void render() {
        beginRender();

        int currentLayer = 0;
        for (MapLayer layer : map.getLayers()) {

            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderTileLayer((TiledMapTileLayer) layer);
                    currentLayer++;

                    if (currentLayer == 2) {

                        Collections.sort(entityRenderers, new EntityRendererDrawOrderer());

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
