package com.jalfsoftware.jalf.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.jalfsoftware.jalf.Jalf;
import com.jalfsoftware.jalf.entities.AbstractEntity;
import com.jalfsoftware.jalf.entities.Enemyredstickman001;
import com.jalfsoftware.jalf.entities.Player;
import com.jalfsoftware.jalf.helper.Map;
import com.jalfsoftware.jalf.services.ConsoleManager;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.Intersector.overlaps;

/**
 * Screen zur Darstellung des Spiels
 */
public class GameScreen extends AbstractScreen implements Player.EndOfMapReachedListener, ConsoleManager.CommandableInstance {
    public static final float UNITSCALE            = 0.75f; // Skalierungskonstante für die Darstellung von Maps und Entitäten
    public static final float GRAVITATION_CONSTANT = 0.2f;

    private Map                  map;
    private Player               player;
    private List<AbstractEntity> entityList;
    private List<AbstractEntity> poolableEntityList;
    private long                 startTime;
    private Label                timeLabel, livesLabel;
    private Rectangle            PlayerBox;
    private Rectangle            EnemyBox;

    public GameScreen(Jalf jalf, Map map) {
        super(jalf);
        this.map = map;
        this.timeLabel = new Label("", skin, "ingame-font", Color.BLACK);
        this.livesLabel = new Label("", skin, "ingame-font", Color.BLACK);

        // Maprenderer initialisieren
        Gdx.app.log(LOG, "Loading " + (map.isDefault() ? "defaultmap " : "usermap ") + map.getName() + " from " + map.getPath());
        boolean mapIsValid = map.loadMap();
        // TODO: rausspringen, wenn Map nicht korrekt aufgebaut


        // Spieler initialisieren
        player = new Player(map.getSpawnPosition().x * UNITSCALE, map.getSpawnPosition().y * UNITSCALE, 10, 10, 3, 15, 3, 5, this);
        player.addListener(this);
        addInputProcessor(player);

        // Gegnerliste initialisieren
        entityList = new ArrayList<AbstractEntity>();
        for(Vector2 spawn:map.getMobSpawnPositions()){
            entityList.add(new Enemyredstickman001(spawn.x * UNITSCALE, spawn.y * UNITSCALE, this));
        }
        // Poolable Entity List
        poolableEntityList = new ArrayList<AbstractEntity>();

        // Startzeit festlegen
        startTime = System.currentTimeMillis();
    }

    private long getTimeSinceStart() {
        return System.currentTimeMillis() - startTime;
    }

    @Override
    public void show() {
        super.show();

        // Game-Labels initialisieren
        timeLabel.setFontScale(0.5f);
        timeLabel.setPosition(SCREEN_WIDTH - (timeLabel.getPrefWidth() * 3), SCREEN_HEIGHT - timeLabel.getPrefHeight());
        uiStage.addActor(timeLabel);

        livesLabel.setFontScale(0.5f);
        livesLabel.setPosition(SCREEN_WIDTH - (timeLabel.getPrefWidth() * 4) - livesLabel.getPrefWidth(),
                               SCREEN_HEIGHT - livesLabel.getPrefHeight());
        uiStage.addActor(livesLabel);
    }

    @Override
    public void preUIrender(float delta) {
        // Game-Labels updaten
        timeLabel.setText("TIME: " + String.valueOf((System.currentTimeMillis() - startTime) / 1000));
        timeLabel.setPosition(0, uiStage.getHeight() - timeLabel.getPrefHeight() / 2);

        livesLabel.setText("LIVES: " + String.valueOf(player.getLives()));
        livesLabel.setPosition((uiStage.getWidth() / 2) - (livesLabel.getPrefWidth() / 2),
                               uiStage.getHeight() - timeLabel.getPrefHeight() / 2);

        // Kamera auf Spieler-X ausrichten, auf Map setzen und Map rendern
        float playerCenterPos = player.getX() + player.getEntityWidth() / 2;
        boolean playerOutLeft = playerCenterPos < (SCREEN_WIDTH / 2);
        boolean playerOutRight = playerCenterPos > (getMap().getMapWidthAsScreenUnits() - (SCREEN_WIDTH / 2));

        if (!playerOutLeft && !playerOutRight)
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

        // Gegner rendern
        for(AbstractEntity enemy : entityList){
            float playerWidthPosXLeft = player.getX();
            float playerWidthPosXRight = player.getX() + player.getEntityWidth();
            float playerHeightPosLeft = player.getY();
            float playerHeightPosRight = player.getY() + player.getEntityHeight();
            float enemyWidthPosXLeft = enemy.getX();
            float enemyWidthPosXRight = enemy.getX()+ enemy.getWidth();
            float enemyHeightPosYLeft = enemy.getY();
            float enemyHeightPosYRight = enemy.getY()+ enemy.getHeight();
            PlayerBox = new Rectangle(playerWidthPosXLeft, playerHeightPosLeft, player.getWidth(), player.getHeight());
            EnemyBox = new Rectangle (enemyWidthPosXLeft, enemyHeightPosYLeft, enemy.getWidth(), enemy.getHeight());
            enemy.render(renderer.getSpriteBatch());
            // Gdx.app.log(LOG, "Enemy PositionXLeft: " + enemyWidthPosXLeft);
            // Gdx.app.log(LOG, "Enemy PositionXRight: " + enemyWidthPosXRight);
            // Gdx.app.log(LOG, "Enemy PositionYLeft: " + enemyHeightPosYLeft);
            // Gdx.app.log(LOG, "Enemy PositionYRight: " + enemyHeightPosYRight);
            // Gdx.app.log(LOG, "Enemy PositionEntityWidth: " + enemy.getWidth());
            // Gdx.app.log(LOG, "Enemy PositionEntityHeight: " + enemy.getHeight());
            // Gdx.app.log(LOG, "Player Position: " + playerCenterPos);
            // Gdx.app.log(LOG, "Player PositionXLeft: " + playerWidthPosXLeft);
            // Gdx.app.log(LOG, "Player PositionXRight: " + playerWidthPosXRight);
            // Gdx.app.log(LOG, "Player PositionYLeft: " + playerHeightPosLeft);
            // Gdx.app.log(LOG, "Player PositionYRight: " + playerHeightPosRight);
            // Gdx.app.log(LOG, "Player PositionEntityWidth: " + player.getWidth());
            // Gdx.app.log(LOG, "Player PositionEntityHeight: " + player.getHeight());
            if(overlaps(PlayerBox, EnemyBox)){
                Gdx.app.log(LOG, "Box überschnitten");
                player.takeDamage(1);
            }
            // if(PlayerBox == EnemyBox){

            //    Gdx.app.log(LOG, "Box überschnitten");
            //}

        }

            // if(enemyWidthPosXLeft == playerWidthPosXLeft){
            //    Gdx.app.log(LOG, "Position X Left gleich!");
            // }
            // else if(enemyWidthPosXLeft != playerWidthPosXLeft){

//                 Gdx.app.log(LOG, "Position X ungleich!");
//             }
//            if(enemyHeightPosYLeft == playerHeightPosLeft){
//
//                 Gdx.app.log(LOG, "Position Y gleich!");
//             }
//             else if(enemyHeightPosYLeft != playerHeightPosLeft){

//                 Gdx.app.log(LOG, "Position Y ungleich!");
//             }
//         }

        //Entitys rendern
        for (AbstractEntity entity : poolableEntityList)
            entity.render(renderer.getSpriteBatch());

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
        //TODO: Evtl. auf Observer-Pattern ändern
        jalf.setScreen(new GameOverScreen(jalf));
    }

    public void addEntityToRenderLoop(AbstractEntity entity) {
        entityList.add(entity);
        System.out.println(entityList.size() + "Entities will be rendered...");
    }

    public void addPoolableEntityToRenderLoop(AbstractEntity entity) {
        poolableEntityList.add(entity);
        //System.out.println(poolableEntityList.size() + "Poolable Entities will be rendered...");
    }

    @Override
    public List<ConsoleManager.ConsoleCommand> getConsoleCommands() {
        List<ConsoleManager.ConsoleCommand> outList = new ArrayList<>();
        outList.addAll(player.getConsoleCommands());

        return outList;
    }

    public void deleteEntity(){
        entityList.remove(entityList.get(0));
    }

}