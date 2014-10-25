package com.jalfsoftware.jalf.entities;


import com.badlogic.gdx.graphics.Texture;
import com.jalfsoftware.jalf.screens.GameScreen;

/**
 * Created by Anton Löwen on 25.10.2014.
 */

public class Enemyredstickman001 extends AbstractLivingEntity {
    public Enemyredstickman001(float xPos, float yPos, GameScreen gameScreen) {
        super(xPos + 10, yPos + 250, new Texture("enemyredstickman.png"), 5,5,2,5,2, gameScreen);
    }
}
