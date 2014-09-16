package com.jalfsoftware.jalf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.jalfsoftware.jalf.Jalf;
import com.jalfsoftware.jalf.entities.Player;

/**
 * Screen zur Darstellung des Spiels
 */
public class GameScreen extends AbstractScreen {
    public static final float UNITSCALE = 0.75f; // Skalierungskonstante für die Darstellung von Maps und Entitäten

    private OrthogonalTiledMapRenderer mapRenderer;
    private Player                     player;
    private TiledMap                   currentMap;
    private TiledMapTileLayer          collisionLayer;

    // Map-Strings
    private static final String TILE_BLOCKED_KEY     = "blocked";
    private static final String COLLISION_LAYER_NAME = "mgLayer";

    public GameScreen(Jalf jalf) {
        super(jalf);

        // Maprenderer initialisieren
        currentMap = new TmxMapLoader().load("map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(currentMap, UNITSCALE);
        collisionLayer = (TiledMapTileLayer) currentMap.getLayers().get(COLLISION_LAYER_NAME);

        // Spieler initialisieren
        Vector2 spawnPosition = convertToScreenPosition(new Vector2(3, 7));
        player = new Player(spawnPosition.x, spawnPosition.y, 10, 10, 10, 5, this);

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

    /**
     * Prüft, ob das Tile in mgLayer an der übergebenen Position auf der Map blockiert ist (Kollisionsabfrage)
     */
    public boolean isPositionBlocked(int x, int y) {
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(TILE_BLOCKED_KEY);
    }

    /**
     * Konvertiert eine Screen-Position zu einer Map-Position (1LE=1Tile)
     */
    public Vector2 convertToMapPosition(Vector2 screenPosition) {
        return new Vector2((screenPosition.x / collisionLayer.getTileWidth()) / UNITSCALE,
                           (screenPosition.y / collisionLayer.getTileHeight()) / UNITSCALE);
    }

    /**
     * Konvertiert eine Map-Position zu einer Screen-Position (1LE=1Pixel)
     */
    public Vector2 convertToScreenPosition(Vector2 mapPosition) {
        return new Vector2((mapPosition.x * collisionLayer.getTileWidth()) * UNITSCALE,
                           (mapPosition.y * collisionLayer.getTileHeight()) * UNITSCALE);
    }

    @Override
    protected String getName() {
        return GameScreen.class.getSimpleName();
    }
}
