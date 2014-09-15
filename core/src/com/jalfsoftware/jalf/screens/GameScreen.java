package com.jalfsoftware.jalf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.jalfsoftware.jalf.Jalf;
import com.jalfsoftware.jalf.entities.Player;

/**
 * Created by Flaiker on 13.09.2014.
 */
public class GameScreen extends AbstractScreen {
    public static final float UNITSCALE = 0.8f; // Skalierungskonstante für die Darstellung von Maps und Entitäten

    private OrthogonalTiledMapRenderer mapRenderer;
    private Player                     player;

    public GameScreen(Jalf jalf) {
        super(jalf);

        // Maprenderer initialisieren
        TiledMap map = new TmxMapLoader().load("map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, UNITSCALE);

        // Spieler initialisieren
        player = new Player(0, 0, 10, 10, 2, 10);

        Gdx.input.setInputProcessor(player);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void preUIrender(float delta) {
        // Kamera auf Map setzen und Map rendern
        mapRenderer.setView(camera);
        mapRenderer.render();

        mapRenderer.getSpriteBatch().begin();

        // Spieler rendern
        player.render(mapRenderer.getSpriteBatch());

        mapRenderer.getSpriteBatch().end();
    }

    @Override
    protected String getName() {
        return GameScreen.class.getSimpleName();
    }
}
