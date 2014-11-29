package com.jalfsoftware.jalf.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.jalfsoftware.jalf.entities.AbstractLivingEntity.Direction;
import com.jalfsoftware.jalf.helper.ItemJumpBoostThread;
import com.jalfsoftware.jalf.helper.ItemSpeedBoostThread;
import com.jalfsoftware.jalf.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Die vom Spieler steuerbare Figur
 */
public class Player extends AbstractLivingEntity implements InputProcessor {
    private List<EndOfMapReachedListener> listeners = new ArrayList<EndOfMapReachedListener>();

    private Vector2 spawnPosition;
    private int     lives;

    public Player(float xPos, float yPos, int currentHealth, int maxHealth, int lives, float acceleration, float maxSpeed, float jumpSpeed,
                  GameScreen gameScreen) {
        super(xPos, yPos, new Texture("player.png"), currentHealth, maxHealth, acceleration, maxSpeed, jumpSpeed, gameScreen);
        this.lives = lives;
        spawnPosition = new Vector2(xPos, yPos);
    }

    public void addListener(EndOfMapReachedListener listener) {
        listeners.add(listener);
    }

    @Override
    public void render(Batch batch) {
        super.render(batch);
        checkIfEndReached();

        // Bei <=0 HP neu spawnen
        if (!isAlive()) {
            currentHealth = maxHealth;
            lives--;
            if (lives > 0) respawn();
            else gameScreen.playerDead();
        }
    }

    private void checkIfEndReached() {
        if (gameScreen.getMap().isPositionEndPosition(gameScreen.getMap().convertToMapUnits(getX()),
                                                      gameScreen.getMap().convertToMapUnits(getY()))) {
            for (EndOfMapReachedListener listener : listeners) {
                listener.mapEndReachedEventHandler();
            }
        }
    }

    public void respawn() {
        resetSpeed();
        fireballAvalible = false;
        setPosition(spawnPosition.x, spawnPosition.y);
    }

    public int getLives() {
        return lives;
    }
    protected void setLives(int lives) {
         this.lives = lives;
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
            case Input.Keys.UP:
            case Input.Keys.W:
                moveLadder(LadderDirection.UP);
                break;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                moveLadder(LadderDirection.DOWN);
                break;
            case Input.Keys.SPACE:
                jump();
                break;
            case Input.Keys.R:
                respawn();
                break;
            case Input.Keys.E:
                if (fireballAvalible) {
                    gameScreen.addPoolableEntityToRenderLoop(new Fireball(getX(), getY(), gameScreen, lastXDirection, getEntityHeight()));
                }
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
            case Input.Keys.UP:
            case Input.Keys.W:
            case Input.Keys.DOWN:
            case Input.Keys.S:
                moveLadder(LadderDirection.NONE);
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

    @Override
    protected void itemLivePlus() {
        setLives(getLives() + 1);
    }

    @Override
    protected void itemHpPlus() {
        //setHealthPoints(getHealthPoints() + 1);
    }

    @Override
    protected void itemCoinPlus() {
        //setScore(getScore() + defineHowMuchPoinsGivesACoin)
    }

    @Override
    protected void itemSetFireballAvalible() {
        this.fireballAvalible = true;
    }

    @Override
    protected void itemJumpBoost() {
        Thread jumpBoost = new Thread(new ItemJumpBoostThread(this));
        jumpBoost.start();
    }

    @Override
    protected void itemSpeedBoost() {
        Thread speedBoost = new Thread(new ItemSpeedBoostThread(this));
        speedBoost.start();
    }
}
