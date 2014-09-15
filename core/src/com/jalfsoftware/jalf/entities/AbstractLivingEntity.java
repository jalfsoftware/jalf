package com.jalfsoftware.jalf.entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * Erweitert AbstractEntity um Lebenspunkte
 */
public abstract class AbstractLivingEntity extends AbstractEntity {
    protected int currentHealth;
    protected int MAXHEALTH;

    public AbstractLivingEntity(float xPos, float yPos, Texture texture, int currentHealth, int maxhealth) {
        super(xPos, yPos, texture);
        this.MAXHEALTH = maxhealth;
        this.currentHealth = currentHealth;
    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMAXHEALTH() {
        return MAXHEALTH;
    }

    public boolean isAlive() {
        return (currentHealth>0);
    }
}
