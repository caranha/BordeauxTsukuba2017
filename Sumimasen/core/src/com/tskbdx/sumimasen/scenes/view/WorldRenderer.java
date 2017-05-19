package com.tskbdx.sumimasen.scenes.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.tskbdx.sumimasen.Sumimasen;
import com.tskbdx.sumimasen.scenes.Pair;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.view.effects.Effect;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRendererDrawOrderer;
import com.tskbdx.sumimasen.scenes.view.entities.animator.DirectionSpriteSheetAnimator;

import java.util.*;

/**
 * Created by Sydpy on 4/28/17.
 */

public class WorldRenderer implements Observer{

    private Map<Entity, EntityRenderer> rendererByEntity = new HashMap<>();

    private Effect effect;

    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private Batch batch = new SpriteBatch();

    private OrthographicCamera camera;

    public WorldRenderer(World world, OrthographicCamera camera) {
        world.addObserver(this);

        this.camera = camera;
    }

    //TODO: Maybe to ptimize
    public void render() {

        batch.begin();

        tiledMapRenderer.setView(camera);

        List<EntityRenderer> renderers = new ArrayList<>(rendererByEntity.values());
        Collections.sort(renderers, new EntityRendererDrawOrderer());

        TiledMapTileLayer floating = (TiledMapTileLayer) tiledMapRenderer.getMap().getLayers().get("Floating");

        List<EntityRenderer> beforeFloating = new ArrayList<>();
        List<EntityRenderer> afterFloating = new ArrayList<>();

        for (EntityRenderer renderer : renderers) {
            if(floating.getCell((int) renderer.getX() / 8 , (int) renderer.getY() / 8) != null
                    || floating.getCell((int) (renderer.getX() + renderer.getWidth()) / 8 - 1 , (int) renderer.getY() / 8) != null) {
                beforeFloating.add(renderer);
            } else {
                afterFloating.add(renderer);
            }
        }

        for (MapLayer layer : tiledMapRenderer.getMap().getLayers()) {

            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {

                    if (layer.getName().equals("Floating")) {

                        for (EntityRenderer beforeFloatingRenderer : beforeFloating) {

                            for (int i = 0; i < afterFloating.size(); i++) {
                                EntityRenderer afterFloatingRenderer = afterFloating.get(i);

                                if (afterFloatingRenderer.getY() > beforeFloatingRenderer.getY()
                                        && afterFloatingRenderer.getRectangle().overlaps(beforeFloatingRenderer.getRectangle())) {

                                    afterFloatingRenderer.render(batch);
                                    afterFloating.remove(i);
                                    i--;
                                }
                            }

                            beforeFloatingRenderer.render(batch);
                        }

                        tiledMapRenderer.renderTileLayer((TiledMapTileLayer) layer);

                        for (EntityRenderer entityRenderer : afterFloating) {
                            entityRenderer.render(batch);
                        }

                    } else {
                        tiledMapRenderer.renderTileLayer((TiledMapTileLayer) layer);
                    }
                }
            }
        }

        if (effect != null && !effect.isFinished()) {
            effect.render(batch);
        }

        batch.end();
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    @Override
    public void update(Observable observable, Object o) {

        if (o instanceof TiledMap) {
            tiledMapRenderer = new OrthogonalTiledMapRenderer((TiledMap) o, batch);
        } else if (o instanceof Pair) {

            if (((Pair) o).getLeft() instanceof Entity
                    && ((Pair) o).getRight() instanceof String) {

                Entity entity = (Entity) ((Pair) o).getLeft();
                String imageFile = (String) ((Pair) o).getRight();
                EntityRenderer entityRenderer = new EntityRenderer(entity, imageFile, Sumimasen.getAssetManager());

                if (entity.getName().equals("player")) {
                    entityRenderer.setAnimator(new DirectionSpriteSheetAnimator(entityRenderer, 1, 2, 12, 16, 0.4f));
                }

                entityRenderer.setWorldRenderer(this);

                rendererByEntity.put(entity, entityRenderer);
            }

        } else if (o instanceof Entity) {
            rendererByEntity.remove(o);
        }

    }
}
