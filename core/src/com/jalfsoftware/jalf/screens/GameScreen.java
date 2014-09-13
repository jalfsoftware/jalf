package com.jalfsoftware.jalf.screens;

import com.jalfsoftware.jalf.Jalf;

/**
 * Created by Flaiker on 13.09.2014.
 */
public class GameScreen extends AbstractScreen {

    public GameScreen(Jalf jalf) {
        super(jalf);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    protected String getName() {
        return GameScreen.class.getSimpleName();
    }

    @Override
    protected void preUIrender(float delta) {

    }
}
