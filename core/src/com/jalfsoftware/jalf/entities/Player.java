package com.jalfsoftware.jalf.entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * Die vom Spieler steuerbare Figur
 */
public class Player extends AbstractLivingEntity {

    public Player(float xPos, float yPos, int currentHealth, int maxHealth) {
        super(xPos, yPos, new Texture("player.png"), currentHealth, maxHealth);
    }
}
