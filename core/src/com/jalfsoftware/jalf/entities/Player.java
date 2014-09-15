package com.jalfsoftware.jalf.entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;

/**
 * Die vom Spieler steuerbare Figur
 */
public class Player extends AbstractLivingEntity implements InputProcessor {

    public Player(float xPos, float yPos, int currentHealth, int maxHealth, float acceleration, float maxSpeed) {
        super(xPos, yPos, new Texture("player.png"), currentHealth, maxHealth, acceleration, maxSpeed);
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
                // TODO: Bug, hört auf zu bewegen wenn zwei Tasten gleichzeitig gedrückt sind und eine losgelassen wird
                move(Direction.NONE);
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
}
