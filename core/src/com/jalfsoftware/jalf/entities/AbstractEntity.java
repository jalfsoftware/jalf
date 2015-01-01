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
    protected static final TextureAtlas ENTITY_ATLAS = new TextureAtlas(Gdx.files.internal("atlases/entities.atlas"));


    public AbstractEntity(float xPos, float yPos, String Regionname, GameScreen gameScreen) {
        TextureAtlas.AtlasRegion region = ENTITY_ATLAS.findRegion(Regionname);
        sprite = ENTITY_ATLAS.createSprite(Regionname);
        sprite.setPosition(xPos, yPos);
        sprite.setSize(region.getRegionWidth() * GameScreen.UNITSCALE, region.getRegionHeight() * GameScreen.UNITSCALE);
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
