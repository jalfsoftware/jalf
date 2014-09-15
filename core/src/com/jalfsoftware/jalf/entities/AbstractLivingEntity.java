package com.jalfsoftware.jalf.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Erweitert AbstractEntity um Lebenspunkte, Beschleunigung, Maximalgeschwindigkeit, Bewegung
 */
public abstract class AbstractLivingEntity extends AbstractEntity {
    protected int currentHealth;
    protected int maxHealth;

    protected float acceleration;
    protected float maxSpeed;
    protected float currentSpeed;

    private Direction currentDirection;

    public AbstractLivingEntity(float xPos, float yPos, Texture texture, int currentHealth, int maxhealth, float acceleration,
                                float maxSpeed) {
        super(xPos, yPos, texture);
        this.maxHealth = maxhealth;
        this.currentHealth = currentHealth;

        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;

        currentSpeed = 0;
        currentDirection = Direction.NONE;
    }

    @Override
    public void render(Batch batch) {
        super.render(batch);

        // Bewegung der Entity
        // TODO: Deacceleration und Trennung der aktuellen Geschwindigkeit nach Richtung
        float delta = Gdx.graphics.getDeltaTime();

        currentSpeed += acceleration * delta;
        if (currentSpeed > maxSpeed) currentSpeed = maxSpeed;

        switch (currentDirection) {
            case LEFT:
                setPosition(getX() - currentSpeed, getY());
                break;
            case RIGHT:
                setPosition(getX() + currentSpeed, getY());
                break;
        }
    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
    }

    public void move(Direction direction) {
        currentDirection = direction;
        if (direction == Direction.NONE) currentSpeed = 0;
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
