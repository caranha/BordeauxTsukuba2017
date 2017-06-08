package com.tskbdx.sumimasen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Sumimasen extends Game {

    private SpriteBatch batch;

	private static AssetManager assetManager = new AssetManager();

    @Override
	public void create () {
		batch = new SpriteBatch();

		setScreen(new LoadingScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

        screen.dispose();
		batch.dispose();
		assetManager.dispose();
	}

	SpriteBatch getBatch() {
		return batch;
	}


	/**
	 * @return a generated quality font at a certain size (no scaling)
	 */
	public static BitmapFont getFont(int size, String name) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + name + ".ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = size;
		BitmapFont font = generator.generateFont(parameter);
		generator.dispose();
		return font;
	}

    public static AssetManager getAssetManager() {
        return assetManager;
    }
}
