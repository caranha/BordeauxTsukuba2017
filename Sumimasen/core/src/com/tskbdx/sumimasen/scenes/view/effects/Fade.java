package com.tskbdx.sumimasen.scenes.view.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Sydpy on 5/17/17.
 */
public class Fade extends Effect {

    public static int IN = 0, OUT = 1;

    private float duration;
    private float clock = 0.f;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private int inOrOut;

    public Fade(float duration, int inOrOut) {
        super();
        this.duration = duration;
        this.inOrOut = inOrOut;
    }

    @Override
    public void render(Batch batch) {

        batch.end();

        clock = Math.min(duration , clock + Gdx.graphics.getDeltaTime());

        Color color = new Color(0,0,0,
                inOrOut == IN ? clock/duration : 1.f - clock/duration );

        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        batch.begin();

        if (clock == duration) {
            setFinished();
        }
    }
}
