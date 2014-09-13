package com.jalfsoftware.jalf.screens;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.jalfsoftware.jalf.Jalf;
import com.jalfsoftware.jalf.Player;

/**
 * Created by Flaiker on 13.09.2014.
 */
public class GameScreen extends AbstractScreen {
    private static final float UNITSCALE = 0.8f;

    private OrthogonalTiledMapRenderer mapRenderer;
    private Player           player;

    public GameScreen(Jalf jalf) {
        super(jalf);
        TiledMap map = new TmxMapLoader().load("map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map, UNITSCALE);
        player = new Player(UNITSCALE);
    }

    @Override
    public void show() {
        super.show();
    }


    @Override
    public void preUIrender(float delta) {
        mapRenderer.setView(camera);
        mapRenderer.render();

        mapRenderer.getSpriteBatch().begin();

        player.setPosition(0,0);
        player.render(mapRenderer.getSpriteBatch());

        mapRenderer.getSpriteBatch().end();
    }

    @Override
    protected String getName() {
        return GameScreen.class.getSimpleName();
    }
}
