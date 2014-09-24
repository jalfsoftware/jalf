package com.jalfsoftware.jalf.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.jalfsoftware.jalf.screens.GameScreen;

/**
 * Erweitert AbstractEntity um Lebenspunkte, Beschleunigung, Maximalgeschwindigkeit, Bewegung
 */
public abstract class AbstractLivingEntity extends AbstractEntity {
    public static final String LOG = AbstractLivingEntity.class.getSimpleName();

    protected int currentHealth;
    protected int maxHealth;

    protected float   acceleration;
    protected float   maxSpeed;
    protected float   jumpSpeed;
    protected Vector2 currentSpeed;

    private int jumpCount;

    private Direction requestedDirection;

    public AbstractLivingEntity(float xPos, float yPos, Texture texture, int currentHealth, int maxHealth, float acceleration,
                                float maxSpeed, float jumpSpeed, GameScreen gameScreen) {
        super(xPos, yPos, texture, gameScreen);
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;

        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
        this.jumpSpeed = jumpSpeed;

        jumpCount = 0;
        currentSpeed = new Vector2(0, 0);

        requestedDirection = Direction.NONE;
    }

    @Override
    public void render(Batch batch) {
        super.render(batch);

        float delta = Gdx.graphics.getDeltaTime();

        // X-Achse
        // Beschleunigen
        switch (requestedDirection) {
            case LEFT:
                currentSpeed.x -= acceleration * delta;
                if (currentSpeed.x < -maxSpeed) currentSpeed.x = -maxSpeed;
                break;
            case RIGHT:
                currentSpeed.x += acceleration * delta;
                if (currentSpeed.x > maxSpeed) currentSpeed.x = maxSpeed;
                break;
        }

        // Verlangsamen
        if (requestedDirection == Direction.NONE && Math.abs(currentSpeed.x) > 0) {
            if (currentSpeed.x > 0) {
                currentSpeed.x -= acceleration * delta;
                if (currentSpeed.x < 0) currentSpeed.x = 0;
            } else {
                currentSpeed.x += acceleration * delta;
                if (currentSpeed.x > 0) currentSpeed.x = 0;
            }
        }

        // Y-Achse
        // Beschleunigen (Gravitation)
        currentSpeed.y -= GameScreen.GRAVITATION_CONSTANT;

        // Kollision
        // Herausfinden, ob neue Position blockiert ist
        boolean blockedX;
        boolean blockedY;

        // X
        Vector2 newPositionX = new Vector2(getX() + currentSpeed.x, getY());
        Vector2 newMapPositionX = gameScreen.convertToMapPosition(newPositionX);
        if (currentSpeed.x > 0) {
            blockedX = gameScreen.isPositionBlocked((int) (newMapPositionX.x + 1), (int) newMapPositionX.y);
        } else if (currentSpeed.x < 0) {
            blockedX = gameScreen.isPositionBlocked((int) (newMapPositionX.x), (int) newMapPositionX.y);
        } else {
            blockedX = false;
        }

        // Y
        Vector2 newPositionY = new Vector2(getX(), getY() + currentSpeed.y);
        Vector2 newMapPositionY = gameScreen.convertToMapPosition(newPositionY);
        blockedY = gameScreen.isPositionBlocked((int) newMapPositionY.x, (int) newMapPositionY.y) ||
                   gameScreen.isPositionBlocked((int) newMapPositionY.x + 1, (int) newMapPositionY.y);

        if (blockedX) currentSpeed.x = 0;
        if (blockedY) {
            currentSpeed.y = 0;
            jumpCount = 0;
        }

        // Neue Position setzen
        setPosition(getX() + currentSpeed.x, getY() + currentSpeed.y);
    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
    }

    protected void move(Direction direction) {
        requestedDirection = direction;
    }

    protected void jump() {
        if (jumpCount < 2) {
            jumpCount++;
            currentSpeed.y = jumpSpeed;
        }
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isAlive() {
        return (currentHealth > 0);
    }

    public float getAcceleration() {
        return acceleration;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public Vector2 getcurrentSpeed() {
        return currentSpeed;
    }

    public static enum Direction {
        LEFT,
        RIGHT,
        NONE
    }
}
