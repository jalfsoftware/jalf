package com.jalfsoftware.jalf.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Erweitert AbstractEntity um Lebenspunkte, Beschleunigung, Maximalgeschwindigkeit, Bewegung
 */
public abstract class AbstractLivingEntity extends AbstractEntity {
    public static final String LOG = AbstractLivingEntity.class.getSimpleName();

    protected int currentHealth;
    protected int maxHealth;

    protected float acceleration;
    protected float maxSpeed;
    protected float currentSpeed;

    private Direction requestedDirection;

    public AbstractLivingEntity(float xPos, float yPos, Texture texture, int currentHealth, int maxHealth, float acceleration,
                                float maxSpeed) {
        super(xPos, yPos, texture);
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;

        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;

        currentSpeed = 0;
        requestedDirection = Direction.NONE;
    }

    @Override
    public void render(Batch batch) {
        super.render(batch);

        float delta = Gdx.graphics.getDeltaTime();

        // Beschleunigen
        switch (requestedDirection) {
            case LEFT:
                currentSpeed -= acceleration * delta;
                if (currentSpeed < -maxSpeed) currentSpeed = -maxSpeed;
                break;
            case RIGHT:
                currentSpeed += acceleration * delta;
                if (currentSpeed > maxSpeed) currentSpeed = maxSpeed;
                break;
        }

        // Verlangsamen
        if (requestedDirection == Direction.NONE && Math.abs(currentSpeed) > 0) {
            if (currentSpeed > 0) {
                currentSpeed -= acceleration * delta;
                if (currentSpeed < 0) currentSpeed = 0;
            } else {
                currentSpeed += acceleration * delta;
                if (currentSpeed > 0) currentSpeed = 0;
            }
        }

        // Position ändern
        setPosition(getX() + currentSpeed, getY());

    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
    }

    public void move(Direction direction) {
        requestedDirection = direction;
        //if (direction == Direction.NONE) currentSpeed = 0;
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

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public static enum Direction {
        LEFT,
        RIGHT,
        NONE
    }
}
