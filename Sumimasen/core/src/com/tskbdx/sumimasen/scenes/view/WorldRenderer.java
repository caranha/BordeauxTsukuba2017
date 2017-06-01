package com.tskbdx.sumimasen.scenes.view;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.tskbdx.sumimasen.GameScreen;
import com.tskbdx.sumimasen.scenes.TiledMapUtils;
import com.tskbdx.sumimasen.scenes.model.World;
import com.tskbdx.sumimasen.scenes.model.entities.Entity;
import com.tskbdx.sumimasen.scenes.view.effects.Effect;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRenderer;
import com.tskbdx.sumimasen.scenes.view.entities.EntityRendererDrawOrderer;
import com.tskbdx.sumimasen.scenes.view.entities.SpritesheetUtils;
import com.tskbdx.sumimasen.scenes.view.entities.animator.Animator;
import com.tskbdx.sumimasen.scenes.view.entities.animator.DirectionSpriteSheetAnimator;

import java.util.*;

/**
 * Created by Sydpy on 4/28/17.
 */
public class WorldRenderer implements Observer {

    private World world;
    private Map<Entity, EntityRenderer> rendererByEntity = new HashMap<>();

    private Effect effect;

    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private Batch batch = new SpriteBatch();

    private SmoothCamera camera;

    public WorldRenderer(World world, SmoothCamera camera) {
        world.addObserver(this);
        this.world = world;
        this.camera = camera;
    }

    public void init(TiledMap tiledMap, List<TiledMapUtils.MapObjectMapping> mappings) {

        rendererByEntity.clear();
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);

        tiledMap.getLayers().get("Collision").setVisible(false);


        for (TiledMapUtils.MapObjectMapping mapObjectMapping : mappings) {

            buildEntityRendererFromMapObjectMapping(mapObjectMapping);

        }

        getCamera().setTo(GameScreen.getPlayer().getX() * 8,
                GameScreen.getPlayer().getY() * 8);
    }

    //TODO: Maybe to ptimize
    public void render() {

        batch.begin();

        tiledMapRenderer.setView(camera);

        List<EntityRenderer> renderers = new ArrayList<>(rendererByEntity.values());
        Collections.sort(renderers, new EntityRendererDrawOrderer());

        TiledMapTileLayer floating = (TiledMapTileLayer) tiledMapRenderer.getMap().getLayers().get("Floating");

        for (MapLayer layer : tiledMapRenderer.getMap().getLayers()) {

            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {

                    if (layer.getName().equals("Floating")) {

                        for (int i = floating.getHeight() - 1; i >= 0; i--) {

                            for (int ri = 0; ri < renderers.size(); ri++) {
                                EntityRenderer entityRenderer = renderers.get(ri);
                                if (entityRenderer.getEntity().getY() < i) break;

                                if (entityRenderer.getEntity().getY() == i) {
                                    entityRenderer.render(batch);
                                    renderers.remove(ri);
                                    ri--;
                                }
                            }

                            for (int j = 0; j < floating.getWidth(); j++) {

                                int i2 = i;

                                TiledMapTileLayer.Cell cell = floating.getCell(j,i2);
                                while (cell != null) {
                                    batch.draw(cell.getTile().getTextureRegion(), j * 8, i2 * 8);
                                    i2++;
                                    if (i2 >= floating.getHeight()) break;
                                    cell = floating.getCell(j,i2);
                                }

                            }
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

    }

    private void buildEntityRendererFromMapObjectMapping(TiledMapUtils.MapObjectMapping mo) {

        Entity entity = world.getEntityByName(mo.name);

        EntityRenderer renderer = rendererByEntity.get(entity);
        if (renderer == null) {
            EntityRenderer entityRenderer = new EntityRenderer(entity);
            entityRenderer.setWorldRenderer(this);

            Animator standingAnimator = SpritesheetUtils.getAnimatorFromSpritesheet(mo.standingSpritesheet);
            if (standingAnimator instanceof DirectionSpriteSheetAnimator) {
                ((DirectionSpriteSheetAnimator) standingAnimator).setEntity(entity);
            }

            Animator walkingAnimator = SpritesheetUtils.getAnimatorFromSpritesheet(mo.walkingSpritesheet);
            if (walkingAnimator instanceof DirectionSpriteSheetAnimator) {
                ((DirectionSpriteSheetAnimator) walkingAnimator).setEntity(entity);
            }


            entityRenderer.setStandingAnimator(standingAnimator);
            entityRenderer.setWalkingAnimator(walkingAnimator);

            rendererByEntity.put(entity, entityRenderer);
        }
    }

    public SmoothCamera getCamera() {
        return camera;
    }

    public EntityRenderer getRendererByEntity(Entity entity) {
        return rendererByEntity.get(entity);
    }
}
