package com.tskbdx.sumimasen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Sumimasen extends Game {

    private SpriteBatch batch;
	private BitmapFont font;
	private AssetManager assetManager;


    @Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		assetManager = new AssetManager();

		//TODO : Start with a menu screen
		setScreen(new LoadingScreen(this, assetManager));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		assetManager.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}


	/**
	 * @param size
	 * @param name
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

    final AssetManager getAssetManager() {
        return assetManager;
    }
}
