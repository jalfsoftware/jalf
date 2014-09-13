package com.jalfsoftware.jalf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.jalfsoftware.jalf.Jalf;
import com.jalfsoftware.jalf.helper.DefaultActorListener;

/**
 * Created by Flaiker on 13.09.2014.
 */
public class MenuScreen extends AbstractScreen {
    public static final String LOG = MenuScreen.class.getSimpleName();

    private Table table;

    public MenuScreen(Jalf jalf) {
        super(jalf);
    }

    @Override
    public void show() {
        super.show();

        table = new Table(skin);
        table.setFillParent(true);
        uiStage.addActor(table);

        table.add().padBottom(50).row();
        Label titleLabel = new Label("J4F", skin, "digital7-92", Color.WHITE);
        //titleLabel.setFontScale(2);
        table.add(titleLabel).spaceBottom(5).align(1);
        table.row();
        table.add("Jump 4 Fun").align(1).spaceBottom(20);
        table.row();

        // register the button "Start"
        TextButton startGameButton = new TextButton("START", skin);
        startGameButton.setColor(1,1,1,0.9f);
        startGameButton.addListener(new DefaultActorListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                jalf.setScreen(new GameScreen(jalf));
            }
        });
        table.add(startGameButton).expand().fill().pad(100,100,100,100);
        table.row();
/*
        // register the button "Highscore"
        TextButton highscoreButton = new TextButton("HIGHSCORE", skin);
        highscoreButton.setColor(1,1,1,0.9f);
        highscoreButton.addListener(new DefaultActorListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                //game.setScreen(new HighscoreScreen(game,demoGame, skin));
            }
        });
        table.add(highscoreButton).expand().fill().pad(0, 100, 100, 100);
        table.row();

        // register the button "Options"
        TextButton optionsButton = new TextButton("OPTIONS", skin);
        optionsButton.setColor(1,1,1,0.9f);
        optionsButton.addListener(new DefaultActorListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                //game.setScreen(new OptionsScreen(game));
            }
        });
        table.add(optionsButton).expand().fill().pad(0,100,100,100).row();*/

        table.add(new Label("www.jalfsoftware.com",skin));

        Gdx.input.setInputProcessor(uiStage);
    }

    @Override
    protected String getName() {
        return MenuScreen.class.getSimpleName();
    }

    @Override
    protected void preUIrender(float delta) {

    }
}
