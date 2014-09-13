package com.jalfsoftware.jalf;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Flaiker on 13.09.2014.
 */
public class Player {
    private Sprite sprite;

    public Player(float unitScale) {
        sprite = new Sprite(new Texture("player.png"));
    }

    public void render(Batch batch) {
        sprite.draw(batch);
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }
}
