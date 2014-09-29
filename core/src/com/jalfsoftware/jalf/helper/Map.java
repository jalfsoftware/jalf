package com.jalfsoftware.jalf.helper;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.jalfsoftware.jalf.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Halterklasse für zu ladene und geladene Maps
 */
public class Map {
    public static final String LOG = Map.class.getSimpleName();

    // Allgemeine Felder vor Laden
    private String  name;
    private String  path;
    private boolean isDefault;

    // Felder für nach dem Laden
    private Vector2                    spawnPosition;
    private List<Vector2>              endPositions;
    private List<Vector2>              mobSpawnPositions;
    private TiledMap                   map;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMapTileLayer          collisionLayer;
    private MapLayer                   objectLayer;

    // Konstanten
    private static final String TILE_BLOCKED_KEY               = "blocked";
    private static final String COLLISION_LAYER_NAME           = "mgLayer";
    private static final String OBJECT_LAYER_NAME              = "ojLayer";
    private static final String MAP_OBJECT_TILE_REFERENCE_NAME = "gid";
    private static final String TILE_PLAYER_SPAWN_KEY          = "playerSpawn";
    private static final String TILE_MAP_END_KEY               = "mapEnd";

    public Map(String name, String path, boolean isDefault) {
        this.name = name;
        this.path = path;
        this.isDefault = isDefault;
    }

    public boolean loadMap() {
        // Listen initilisieren
        endPositions = new ArrayList<Vector2>();
        mobSpawnPositions = new ArrayList<Vector2>();

        // Map und Renderer laden
        map = new TmxMapLoader().load(path);
        renderer = new OrthogonalTiledMapRenderer(map, GameScreen.UNITSCALE);

        // Layer finden
        collisionLayer = (TiledMapTileLayer) map.getLayers().get(COLLISION_LAYER_NAME);
        objectLayer = map.getLayers().get(OBJECT_LAYER_NAME);

        // Spawn-Positionen + Level-Enden finden
        MapObjects objects = objectLayer.getObjects();
        for (MapObject object : objects) {
            if (object instanceof RectangleMapObject) {
                MapProperties tileProperties =
                        map.getTileSets().getTile((Integer) object.getProperties().get(MAP_OBJECT_TILE_REFERENCE_NAME)).getProperties();
                if (tileProperties.containsKey(TILE_PLAYER_SPAWN_KEY)) {
                    if (spawnPosition == null) spawnPosition = new Vector2(((RectangleMapObject) object).getRectangle().getX(),
                                                                           ((RectangleMapObject) object).getRectangle().getY());
                    else return false;
                } else if (tileProperties.containsKey(TILE_MAP_END_KEY)) {
                    endPositions.add(new Vector2(((RectangleMapObject) object).getRectangle().getX(),
                                                 ((RectangleMapObject) object).getRectangle().getY()));
                }
            }
        }

        // Prüfung, ob Map ordnungsgemäß geladen wurde
        if (spawnPosition != null && !endPositions.isEmpty()) return true;
        else return false;
    }

    /**
     * Prüft, ob das Tile im Kollisionslayer an der übergebenen Position auf der Map blockiert ist (Kollisionsabfrage)
     */
    public boolean isPositionBlocked(int x, int y) {
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
        return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(TILE_BLOCKED_KEY);
    }

    /**
     * Konvertiert eine Screen-Position zu einer Map-Position (1LE=1Tile)
     */
    public Vector2 convertToMapPosition(Vector2 screenPosition) {
        return new Vector2((screenPosition.x / collisionLayer.getTileWidth()) / GameScreen.UNITSCALE,
                           (screenPosition.y / collisionLayer.getTileHeight()) / GameScreen.UNITSCALE);
    }

    /**
     * Konvertiert eine Map-Position zu einer Screen-Position (1LE=1Pixel)
     */
    public Vector2 convertToScreenPosition(Vector2 mapPosition) {
        return new Vector2((mapPosition.x * collisionLayer.getTileWidth()) * GameScreen.UNITSCALE,
                           (mapPosition.y * collisionLayer.getTileHeight()) * GameScreen.UNITSCALE);
    }

    /**
     * Konvertiert ScreenUnits zu MapUnits
     */
    public float convertToMapUnits(float screenUnits) {
        return screenUnits / collisionLayer.getTileWidth() / GameScreen.UNITSCALE;
    }

    /**
     * Konvertiert MapUnits zu ScreenUnits
     */
    public float convertToScreenUnits(float mapUnits) {
        return mapUnits * collisionLayer.getTileWidth() * GameScreen.UNITSCALE;
    }

    public float getMapTileHeight() {
        return collisionLayer.getTileHeight();
    }

    public float getMapTileWidth() {
        return collisionLayer.getTileWidth();
    }

    public Vector2 getSpawnPosition() {
        return spawnPosition;
    }

    public List<Vector2> getEndPositions() {
        return endPositions;
    }

    public List<Vector2> getMobSpawnPositions() {
        return mobSpawnPositions;
    }

    public OrthogonalTiledMapRenderer getRenderer() {
        return renderer;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public String toString() {
        return name;
    }
}
