package com.tskbdx.sumimasen.scenes.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tskbdx.sumimasen.scenes.view.effects.Effect;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRendererDrawOrderer;

import java.util.ArrayList;

/**
 * Created by Sydpy on 4/28/17.
 */

public class WorldRenderer extends OrthogonalTiledMapRenderer {

    private ArrayList<EntityRenderer> entityRenderers = new ArrayList<EntityRenderer>();

    private Effect effect = null;

    public WorldRenderer(TiledMap map) {
        super(map, new SpriteBatch());
    }

    public void addEntityRenderer(EntityRenderer entityRenderer) {
        entityRenderers.add(entityRenderer);
        entityRenderer.setWorldRenderer(this);
    }

    public void removeEntityRenderer(EntityRenderer entityRenderer) {
        entityRenderers.remove(entityRenderer);
        entityRenderer.setWorldRenderer(null);
    }

    @Override
    public void render() {
        entityRenderers.sort(new EntityRendererDrawOrderer());

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

        if (effect != null && !effect.isFinished()) {
            effect.render(getBatch());
        }

        endRender();
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }
}
