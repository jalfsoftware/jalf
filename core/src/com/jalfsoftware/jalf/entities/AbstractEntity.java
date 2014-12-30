package com.jalfsoftware.jalf.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.jalfsoftware.jalf.screens.GameScreen;


/**
 * Abstrakte Entitätsklasse mit Sprite
 */
public abstract class AbstractEntity {
    protected final Sprite     sprite;
    protected final GameScreen gameScreen;
    static TextureAtlas entityAtlas;


    public AbstractEntity(float xPos, float yPos, String Regionname, GameScreen gameScreen) {
        entityAtlas = new TextureAtlas(Gdx.files.internal("Konzepte/entities.atlas"));
        TextureAtlas.AtlasRegion region = entityAtlas.findRegion(Regionname);
        /*
        if(region.index == -1)
        { */
            sprite = entityAtlas.createSprite(Regionname, -1);
            sprite.setPosition(xPos, yPos);
            sprite.setSize(region.getRegionWidth() * GameScreen.UNITSCALE, region.getRegionHeight() * GameScreen.UNITSCALE);
        /*
        }
        else
        {
           Animation standAnimation;
            TextureRegion[][] tmp = region.split(32,32);
            int index = 0;
            for (int i = 0; i < region.index; i++) {
                for (int j = 0; j < ; j++) {
                    walkFrames[index++] = tmp[i][j];
                }
            }
            standAnimation = new Animation(0.025f, region);
            spriteBatch = new SpriteBatch();
        } */

        this.gameScreen = gameScreen;
    }

    public void render(Batch batch) {
        sprite.draw(batch);
    }

    protected void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    protected void setRotation(float rotation) {
        sprite.setRotation(rotation);
    }

    protected void setX(float x) {
        sprite.setX(x);
    }

    protected void setY(float y) {
        sprite.setY(y);
    }

    public Vector2 getPosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }
}
