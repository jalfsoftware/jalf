package com.jalfsoftware.jalf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.jalfsoftware.jalf.Jalf;
import com.jalfsoftware.jalf.entities.AbstractEntity;
import com.jalfsoftware.jalf.entities.Player;
import com.jalfsoftware.jalf.helper.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * Screen zur Darstellung des Spiels
 */
public class GameScreen extends AbstractScreen implements Player.EndOfMapReachedListener {
    public static final float UNITSCALE            = 0.75f; // Skalierungskonstante für die Darstellung von Maps und Entitäten
    public static final float GRAVITATION_CONSTANT = 0.2f;

    private Map                  map;
    private Player               player;
    private List<AbstractEntity> entityList;
    private long                 startTime;

    public GameScreen(Jalf jalf, Map map) {
        super(jalf);
        this.map = map;

        // Maprenderer initialisieren
        Gdx.app.log(LOG, "Loading " + (map.isDefault() ? "defaultmap " : "usermap ") + map.getName() + " from " + map.getPath());
        boolean mapIsValid = map.loadMap();
        // TODO: rausspringen, wenn Map nicht korrekt aufgebaut

        // Spieler initialisieren
        player = new Player(map.getSpawnPosition().x * UNITSCALE, map.getSpawnPosition().y * UNITSCALE, 10, 10, 3, 15, 3, 5, this);
        player.addListener(this);

        // Gegnerliste initialisieren
        entityList = new ArrayList<AbstractEntity>();

        // Startzeit festlegen
        startTime = System.currentTimeMillis();
        Gdx.input.setInputProcessor(player);
    }

    private long getTimeSinceStart() {
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void preUIrender(float delta) {
        // Kamera auf Spieler-X ausrichten, auf Map setzen und Map rendern
        float playerCenterPos = player.getX() + player.getEntityWidth() / 2;
        boolean playerOutLeft = playerCenterPos < (SCREEN_WIDTH / 2);
        boolean playerOutRight = playerCenterPos > (getMap().getMapWidthAsScreenUnits() - (SCREEN_WIDTH / 2));

        if (!playerOutLeft &&! playerOutRight)
            camera.position.x = player.getX() + player.getEntityWidth() / 2;
        else {
            if (playerOutLeft) camera.position.x = SCREEN_WIDTH / 2;
            else camera.position.x = getMap().getMapWidthAsScreenUnits() - (SCREEN_WIDTH / 2);
        }
        camera.update();

        OrthogonalTiledMapRenderer renderer = map.getRenderer();
        renderer.setView(camera);
        renderer.render();

        renderer.getSpriteBatch().begin();

        // Spieler rendern
        player.render(renderer.getSpriteBatch());

        renderer.getSpriteBatch().end();
    }

    public Map getMap() {
        return map;
    }

    @Override
    protected String getName() {
        return GameScreen.class.getSimpleName();
    }

    @Override
    public void mapEndReachedEventHandler() {
        // TODO: hier in das nächste Level, GameOverScreen oder ähnliches wechseln
        jalf.setScreen(new LevelSelectionScreen(jalf));
    }

    public void playerDead() {
        jalf.setScreen(new LevelSelectionScreen(jalf));
    }
}
