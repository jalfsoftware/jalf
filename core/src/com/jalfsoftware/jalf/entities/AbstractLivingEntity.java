package com.jalfsoftware.jalf.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.jalfsoftware.jalf.screens.GameScreen;

/**
 * Erweitert AbstractEntity um Lebenspunkte, Beschleunigung, Maximalgeschwindigkeit, Bewegung
 */
public abstract class AbstractLivingEntity extends AbstractEntity {
    public static final String LOG = AbstractLivingEntity.class.getSimpleName();

    protected int currentHealth;
    protected int maxHealth;

    protected float   acceleration;
    protected float   maxSpeed;
    protected float   jumpSpeed;
    protected Vector2 currentSpeed;

    private int jumpCount;

    private Direction requestedDirection;

    public AbstractLivingEntity(float xPos, float yPos, Texture texture, int currentHealth, int maxHealth, float acceleration,
                                float maxSpeed, float jumpSpeed, GameScreen gameScreen) {
        super(xPos, yPos, texture, gameScreen);
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;

        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
        this.jumpSpeed = jumpSpeed;

        jumpCount = 0;
        currentSpeed = new Vector2(0, 0);

        requestedDirection = Direction.NONE;
    }

    @Override
    public void render(Batch batch) {
        super.render(batch);

        float delta = Gdx.graphics.getDeltaTime();

        updateCurrentSpeed(delta);
        doCollisionDetectionHorizontal();
        doCollisionDetectionVertical();
        doOutOfMapDetectionBottom();
    }

    /**
     * Entity verliert Leben, wenn sie aus der Map fällt
     */
    private void doOutOfMapDetectionBottom() {
        if (getY() < -getEntityHeight()) takeDamage(maxHealth);
    }

    /**
     * Updated currentSpeed unter Verwendung von requestedDirection, acceleration, maxSpeed und Gravitation
     */
    private void updateCurrentSpeed(float delta) {
        // X-Achse
        // Beschleunigen
        switch (requestedDirection) {
            case LEFT:
                currentSpeed.x -= acceleration * delta;
                if (currentSpeed.x < -maxSpeed) currentSpeed.x = -maxSpeed;
                break;
            case RIGHT:
                currentSpeed.x += acceleration * delta;
                if (currentSpeed.x > maxSpeed) currentSpeed.x = maxSpeed;
                break;
        }

        // Verlangsamen
        if (requestedDirection == Direction.NONE && Math.abs(currentSpeed.x) > 0) {
            if (currentSpeed.x > 0) {
                currentSpeed.x -= acceleration * delta;
                if (currentSpeed.x < 0) currentSpeed.x = 0;
            } else {
                currentSpeed.x += acceleration * delta;
                if (currentSpeed.x > 0) currentSpeed.x = 0;
            }
        }

        // Y-Achse
        // Beschleunigen (Gravitation)
        currentSpeed.y -= GameScreen.GRAVITATION_CONSTANT;
    }

    /**
     * Updated Position und currentSpeed bei Kollision der Entität mit Tiles der Map in der X-Achse
     */
    private void doCollisionDetectionHorizontal() {
        boolean blockedX = false;

        float entityMapWidth = gameScreen.getMap().convertToMapUnits(getEntityWidth());

        int newMapPositionX = (int) gameScreen.getMap().convertToMapUnits(getX() + currentSpeed.x);

        // Berührende Tilehöhen an Kanten finden
        // Unten
        int yPosTileBottom = (int) gameScreen.getMap().convertToMapUnits(getY());
        // Oben
        int yPosTileTop = (int) gameScreen.getMap().convertToMapUnits(getY() + getEntityHeight() - 1);

        if (currentSpeed.x > 0) {
            for (int i = yPosTileBottom; i <= yPosTileTop; i++) {
                blockedX = gameScreen.getMap().isPositionBlocked((int) (newMapPositionX + entityMapWidth), i);
                if (blockedX) break;
            }
        } else if (currentSpeed.x < 0) {
            if ((getX() + currentSpeed.x) < 0) {
                blockedX = true;
            } else for (int i = yPosTileBottom; i <= yPosTileTop; i++) {
                blockedX = gameScreen.getMap().isPositionBlocked(newMapPositionX, i);
                if (blockedX) break;
            }
        } else {
            blockedX = false;
        }

        if (blockedX) {
            int mapX = (int) gameScreen.getMap().convertToMapUnits(getX() + ((currentSpeed.x > 0) ? 1 : 0) * (getEntityWidth() - 1));
            setX((int) gameScreen.getMap().convertToScreenUnits(mapX));
            currentSpeed.x = 0;
        }

        // Neue X-Position setzen
        setX(getX() + currentSpeed.x);
    }

    /**
     * Updated Position und currentSpeed bei Kollision der Entität mit Tiles der Map in der Y-Achse
     */
    private void doCollisionDetectionVertical() {
        boolean blockedYBottom, blockedYTop;

        int entityMapHeight = (int) gameScreen.getMap().convertToMapUnits(getEntityHeight());

        float newMapPositionY = gameScreen.getMap().convertToMapUnits(getY() + currentSpeed.y);
        float newMapPositionX = gameScreen.getMap().convertToMapUnits(getX());
        float newMapPositionXRight = gameScreen.getMap().convertToMapUnits(getX() + getEntityWidth() - 1);

        // Unten
        blockedYBottom = gameScreen.getMap().isPositionBlocked((int) newMapPositionX, (int) newMapPositionY) ||
                         gameScreen.getMap().isPositionBlocked((int) newMapPositionXRight, (int) newMapPositionY);

        // Oben
        blockedYTop = gameScreen.getMap().isPositionBlocked((int) newMapPositionX, (int) newMapPositionY + entityMapHeight) ||
                      gameScreen.getMap().isPositionBlocked((int) newMapPositionXRight, (int) newMapPositionY + entityMapHeight);

        if (blockedYBottom) {
            int mapY = (int) gameScreen.getMap().convertToMapUnits(getY());
            setY((int) gameScreen.getMap().convertToScreenUnits(mapY));

            currentSpeed.y = 0;
            jumpCount = 0;
        }
        if (blockedYTop) {
            int mapY = (int) gameScreen.getMap().convertToMapUnits(getY() + (getEntityHeight() / 3 - 1));
            setY((int) gameScreen.getMap().convertToScreenUnits(mapY));

            currentSpeed.y = 0;
        }

        // Neue Y-Position setzen
        setY(getY() + currentSpeed.y);
    }

    public void takeDamage(int damage) {
        currentHealth -= damage;
    }

    protected void move(Direction direction) {
        requestedDirection = direction;
    }

    protected void jump() {
        if (jumpCount < 2) {
            jumpCount++;
            currentSpeed.y = jumpSpeed;
        }
    }

    /**
     * Setzt die aktuelle Geschwindigkeit in beiden Achsen auf 0
     */
    protected void resetSpeed() {
        currentSpeed.x = currentSpeed.y = 0;
    }

    public float getEntityHeight() {
        return sprite.getHeight();
    }

    public float getEntityWidth() {
        return sprite.getWidth();
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public boolean isAlive() {
        return (currentHealth > 0);
    }

    public float getAcceleration() {
        return acceleration;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public Vector2 getcurrentSpeed() {
        return currentSpeed;
    }

    public static enum Direction {
        LEFT,
        RIGHT,
        NONE
    }
}
