package com.jalfsoftware.jalf.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.jalfsoftware.jalf.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Die vom Spieler steuerbare Figur
 */
public class Player extends AbstractLivingEntity implements InputProcessor {
    List<EndOfMapReachedListener> listeners = new ArrayList<EndOfMapReachedListener>();

    public Player(float xPos, float yPos, int currentHealth, int maxHealth, float acceleration, float maxSpeed, float jumpSpeed,
                  GameScreen gameScreen) {
        super(xPos, yPos, new Texture("player.png"), currentHealth, maxHealth, acceleration, maxSpeed, jumpSpeed, gameScreen);
    }

    public void addListener(EndOfMapReachedListener listener) {
        listeners.add(listener);
    }

    @Override
    public void render(Batch batch) {
        super.render(batch);
        checkIfEndReached();
    }

    private void checkIfEndReached() {
        if (gameScreen.getMap()
                      .isPositionEndPosition(gameScreen.getMap().convertToMapUnits(getX()),
                                             gameScreen.getMap().convertToMapUnits(getY()))) {
            for (EndOfMapReachedListener listener : listeners) {
                listener.mapEndReachedEventHandler();
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean keyProcessed = false;

        // Bewegung des Spielers an Tasten binden
        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                move(Direction.LEFT);
                keyProcessed = true;
                break;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                move(Direction.RIGHT);
                keyProcessed = true;
                break;
            case Input.Keys.SPACE:
                jump();
                break;
        }
        return keyProcessed;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean keyProcessed = false;

        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                // Aufhören zu bewegen, wenn Taste losgelassen
                if (!(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
                      Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.D))) move(Direction.NONE);

                if ((keycode == Input.Keys.LEFT || keycode == Input.Keys.A) &&
                    (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || keycode == Input.Keys.D)) move(Direction.RIGHT);

                if ((keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) &&
                    (Gdx.input.isKeyPressed(Input.Keys.LEFT) || keycode == Input.Keys.A)) move(Direction.LEFT);
                keyProcessed = true;
                break;
        }
        return keyProcessed;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public interface EndOfMapReachedListener {
        public void mapEndReachedEventHandler();
    }
}
