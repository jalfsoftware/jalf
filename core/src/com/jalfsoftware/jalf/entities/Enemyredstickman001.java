package com.jalfsoftware.jalf.entities;


import com.badlogic.gdx.graphics.g2d.Batch;
import com.jalfsoftware.jalf.screens.GameScreen;

/**
 * Created by Anton Löwen on 25.10.2014.
 */

public class Enemyredstickman001 extends AbstractLivingEntity {
    int delta;
    public Enemyredstickman001(float xPos, float yPos, GameScreen gameScreen) {
        super(xPos + 10, yPos + 250, "jalf_Stand", 10, 10, 15, 3, 5, gameScreen);
        //TODO: jonathan, Gegnertexture in Textureatlas, Zeile drüber Name ersetzen
    }

    public void render(Batch batch){
    super.render(batch);


        switch (lastXDirection) {
            case LEFT:
                if(delta == 0) {
                    move(Direction.RIGHT);
                }else{
                    move(Direction.LEFT);
                }
                break;
            case RIGHT:
                if(delta == 0) {
                    move(Direction.LEFT);
                }else{
                    move(Direction.RIGHT);
                }
                break;
            case NONE:
                move(Direction.RIGHT);
                break;
        }

    }

    @Override
    protected void itemCoinPlus() {
        //TODO: Lukas???
    }

    @Override
    protected void itemSetFireballAvalible() {
        //TODO: Lukas???
    }

    @Override
    protected void itemJumpBoost() {
        //TODO: Lukas???
    }

    @Override
    protected void itemSpeedBoost() {
        //TODO: Lukas???
    }

    @Override
    protected void itemLivePlus() {
        //TODO: Lukas???
    }

    @Override
    protected void itemHpPlus() {
        //TODO: Lukas???
    }

}

