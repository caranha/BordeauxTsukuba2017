package com.tskbdx.sumimasen.scenes.view.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tskbdx.sumimasen.Sumimasen;
import com.tskbdx.sumimasen.scenes.model.entities.Direction;
import com.tskbdx.sumimasen.scenes.view.entities.animator.Animator;
import com.tskbdx.sumimasen.scenes.view.entities.animator.DirectionSpriteSheetAnimator;
import com.tskbdx.sumimasen.scenes.view.entities.animator.FixedAnimator;
import com.tskbdx.sumimasen.scenes.view.entities.animator.StandardAnimator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sydpy on 5/23/17.
 */
public class SpritesheetUtils {

    private static String IMAGES_DIR = "images/";

    private static Map<String, Animator> animatorsFromSpritesheets = new HashMap<>();

    public static Animator getAnimatorFromSpritesheet(String spritesheetFile) {

        if (spritesheetFile == null || spritesheetFile.equals(""))
            return getAnimatorFromSpritesheet("entity.png");

        if (animatorsFromSpritesheets.containsKey(spritesheetFile))
            return animatorsFromSpritesheets.get(spritesheetFile);

        Texture texture = Sumimasen.getAssetManager().get(IMAGES_DIR + spritesheetFile, Texture.class);

        Animator animator;

        Map<Direction, TextureRegion[]> regions = new HashMap<>();
        TextureRegion[] textureRegions;

        switch (spritesheetFile) {
            case "player_standing.png" :

                regions.put(Direction.NORTH , new TextureRegion[]{new TextureRegion(texture, 0, 0, 12, 16)});
                regions.put(Direction.SOUTH , new TextureRegion[]{new TextureRegion(texture, 0, 16, 12, 16)});
                regions.put(Direction.WEST  , new TextureRegion[]{new TextureRegion(texture, 0, 32, 12, 16)});
                regions.put(Direction.EAST  , new TextureRegion[]{new TextureRegion(texture, 0, 48, 12, 16)});
                regions.put(Direction.NONE  , new TextureRegion[]{new TextureRegion(texture, 0, 16, 12, 16)});

                animator = new DirectionSpriteSheetAnimator(regions, 0.2f);
                break;

            case "player_walking.png" :

                regions.put(Direction.NORTH , new TextureRegion[]{
                        new TextureRegion(texture, 0, 0, 12, 16),
                        new TextureRegion(texture, 12, 0, 12, 16)});
                regions.put(Direction.SOUTH , new TextureRegion[]{
                        new TextureRegion(texture, 0, 16, 12, 16),
                        new TextureRegion(texture, 12, 16, 12, 16)});
                regions.put(Direction.WEST  , new TextureRegion[]{
                        new TextureRegion(texture, 0, 32, 12, 16),
                        new TextureRegion(texture, 12, 32, 12, 16)});
                regions.put(Direction.EAST  , new TextureRegion[]{
                        new TextureRegion(texture, 0, 48, 12, 16),
                        new TextureRegion(texture, 12, 48, 12, 16)});
                regions.put(Direction.NONE  , new TextureRegion[]{
                        new TextureRegion(texture, 0, 16, 12, 16),
                        new TextureRegion(texture, 12, 16, 12, 16)});

                animator = new DirectionSpriteSheetAnimator(regions, 0.2f);
                break;

            case "noname_standing.png" :

                regions.put(Direction.NORTH , new TextureRegion[]{new TextureRegion(texture, 0, 0, 12, 16)});
                regions.put(Direction.SOUTH , new TextureRegion[]{new TextureRegion(texture, 0, 16, 12, 16)});
                regions.put(Direction.WEST  , new TextureRegion[]{new TextureRegion(texture, 0, 32, 12, 16)});
                regions.put(Direction.EAST  , new TextureRegion[]{new TextureRegion(texture, 0, 48, 12, 16)});
                regions.put(Direction.NONE  , new TextureRegion[]{new TextureRegion(texture, 0, 16, 12, 16)});

                animator = new DirectionSpriteSheetAnimator(regions, 0.2f);
                break;

            case "noname_walking.png" :

                regions.put(Direction.NORTH , new TextureRegion[]{
                        new TextureRegion(texture, 0, 0, 12, 16),
                        new TextureRegion(texture, 12, 0, 12, 16)});
                regions.put(Direction.SOUTH , new TextureRegion[]{
                        new TextureRegion(texture, 0, 16, 12, 16),
                        new TextureRegion(texture, 12, 16, 12, 16)});
                regions.put(Direction.WEST  , new TextureRegion[]{
                        new TextureRegion(texture, 0, 32, 12, 16),
                        new TextureRegion(texture, 12, 32, 12, 16)});
                regions.put(Direction.EAST  , new TextureRegion[]{
                        new TextureRegion(texture, 0, 48, 12, 16),
                        new TextureRegion(texture, 12, 48, 12, 16)});
                regions.put(Direction.NONE  , new TextureRegion[]{
                        new TextureRegion(texture, 0, 16, 12, 16),
                        new TextureRegion(texture, 12, 16, 12, 16)});

                animator = new DirectionSpriteSheetAnimator(regions, 0.2f);
                break;

            case "cat.png":

                textureRegions = new TextureRegion[] {
                        new TextureRegion(texture, 0, 0, 16, 16),
                        new TextureRegion(texture, 0,16,16,16)
                };

                animator = new StandardAnimator(textureRegions, 0.4f);
                break;

            case "tv.png":

                textureRegions = new TextureRegion[]{
                        new TextureRegion(texture, 0, 0, 24, 16),
                        new TextureRegion(texture, 0, 16, 24, 16)
                };

                animator = new StandardAnimator(textureRegions, 0.4f);
                break;

            case "machine.png":

                textureRegions = new TextureRegion[]{
                        new TextureRegion(texture, 0, 0, 16, 24),
                        new TextureRegion(texture, 16, 0, 16, 24)
                };

                animator = new StandardAnimator(textureRegions, 0.4f);
                break;

            case "machine_broken.png":

                textureRegions = new TextureRegion[]{
                        new TextureRegion(texture, 0, 0, 16, 24),
                        new TextureRegion(texture, 16, 0, 16, 24)
                };

                animator = new StandardAnimator(textureRegions, 0.4f);
                break;

            default :
                animator = new FixedAnimator(texture);
                break;
        }

        animatorsFromSpritesheets.put(spritesheetFile, animator);

        return animator;
    }

}
