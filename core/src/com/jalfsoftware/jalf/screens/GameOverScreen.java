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
 * Screen zur Darstellung des Einstiegmenüs
 */
public class GameOverScreen extends AbstractScreen {
    public static final String LOG = GameOverScreen.class.getSimpleName();

    private Table table; // Dient der Anordnung der Menüelemente

    public GameOverScreen(Jalf jalf) {
        super(jalf);
    }

    @Override
    public void show() {
        super.show();

        table = new Table(skin);
        table.setFillParent(true);
        uiStage.addActor(table);

        table.add().padBottom(50).row();

        // Titel
        Label titleLabel = new Label("THE ULTIMATE JALF", skin, "Title", Color.GREEN);
        table.add(titleLabel).spaceBottom(5).align(1);
        table.row();

        // Untertitel
        Label subtitleLabel = new Label("Game Over",skin, "Subtitle", Color.BLUE);
        table.add(subtitleLabel).spaceBottom(10);
        table.row();

        // Button "Start"
        TextButton startGameButton = new TextButton("Map selection", skin);
        startGameButton.setColor(1, 1, 1, 0.9f);
        startGameButton.addListener(new DefaultActorListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                jalf.setScreen(new LevelSelectionScreen(jalf));
            }
        });
        table.add(startGameButton).expand().fill().pad(0, 150, 25, 150);
        table.row();

        // Button "Options"
        TextButton optionsButton = new TextButton("Options", skin);
        optionsButton.setColor(1, 1, 1, 0.9f);
        optionsButton.addListener(new DefaultActorListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                jalf.setScreen(new OptionsScreen(jalf));
            }
        });
        table.add(optionsButton).expand().fill().pad(0, 150, 25, 150);
        table.row();

        // Button "Exit"
        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.setColor(1, 1, 1, 0.9f);
        exitButton.addListener(new DefaultActorListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.exit();
            }
        });
        table.add(exitButton).expand().fill().pad(0, 150, 25, 150);
        table.row();

        // Footer
        table.add(new Label("www.jalfsoftware.com", skin, "Subtitle", Color.DARK_GRAY)).row();
        table.add().padBottom(25).row();

        Gdx.input.setInputProcessor(uiStage);
    }

    @Override
    protected String getName() {
        return GameOverScreen.class.getSimpleName();
    }
}
