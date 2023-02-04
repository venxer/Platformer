package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Player extends Sprite {
    public World world;
    public static Body b2body;
    private TextureRegion playerStand;

    public enum State {
        IDLE, RUNNING, JUMPING, FALLING, DEAD
    };

    public State currentState;
    public State previousState;

    private Animation<TextureRegion> playerIdle;
    private Animation<TextureRegion> playerRun;
    private Animation<TextureRegion> playerJump;
    private Animation<TextureRegion> playerDead;
    private float stateTimer;
    private static boolean facingRight;
    private static int health = 100;
    private boolean playerIsDead;

    private boolean playerEnemyCollide = false;
    CollisionRect rect;

    public Player(GameScreen screen) {
        super(screen.getAtlas().findRegion("Player"));
        this.world = screen.getWorld();
        definePlayer();
        playerStand = new TextureRegion(getTexture(), 0, 48, 48, 48);
        setBounds(0, 0, 48, 48);
        setRegion(playerStand);
        currentState = State.IDLE;
        previousState = State.IDLE;
        stateTimer = 0;
        facingRight = false;
        playerIsDead = false;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        // PlayerIdle
        for (int x = 6; x < 10; x++) {
            frames.add(new TextureRegion(getTexture(), x * 48, 11 * 48, 48, 48));
        }
        playerIdle = new Animation<TextureRegion>(0.1f, frames);

        // PlayerRun
        for (int x = 0; x < 6; x++) {
            frames.add(new TextureRegion(getTexture(), x * 48, 9 * 48, 48, 48));
            // frames.add(new TextureRegion(getTexture(), x * 48, 9 * 48, 48, 48));
            // frames.add(new TextureRegion(getTexture(), x * 48, 9 * 48, 48, 48));

        }
        playerRun = new Animation<TextureRegion>(0.1f, frames);

        // PlayerJump
        for (int x = 0; x < 5; x++) {
            frames.add(new TextureRegion(getTexture(), x * 48, 4 * 48, 48, 48));
        }
        playerJump = new Animation<TextureRegion>(0.1f, frames);

        // PlayerDead
        for (int x = 0; x < 6; x++) {
            frames.add(new TextureRegion(getTexture(), x * 48, 11 * 48, 48, 48));
        }
        playerDead = new Animation<TextureRegion>(0.1f, frames);

        rect = new CollisionRect(b2body.getPosition().x, b2body.getPosition().y, 40, 40);
    }

    public void update(float delta) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        rect.move(b2body.getPosition().x, b2body.getPosition().y);
        setRegion(getFrame(delta));
        // if(getState() == State.DEAD)
        // {
        // world.destroyBody(b2body);
        // }
    }

    public TextureRegion getFrame(float delta) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            // case JUMPING:
            // region = playerJump.getKeyFrame(stateTimer);
            // break;
            case RUNNING:
                region = playerRun.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                region = playerDead.getKeyFrame(stateTimer);
                break;
            default:
                region = playerIdle.getKeyFrame(stateTimer, true);
                break;
        }

        if ((b2body.getLinearVelocity().x < 0 || !facingRight) && !region.isFlipX()) {
            region.flip(true, false);
            facingRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || facingRight) && region.isFlipX()) {
            region.flip(true, false);
            facingRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;
        return region;
    }

    public boolean isDead() {
        return playerIsDead;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public State getState() {
        if (health <= 0 || Player.getPositionY() < 50) {
            return State.DEAD;
        } else if (b2body.getLinearVelocity().y > 0) {
            return State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (b2body.getLinearVelocity().x > 0) {
            return State.RUNNING;
        } else if (b2body.getLinearVelocity().x < 0) {
            return State.RUNNING;
        } else {
            return State.IDLE;
        }
    }

    public void definePlayer() {
        BodyDef bDef = new BodyDef();
        bDef.position.set(1600, 200);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(22);
        fDef.filter.categoryBits = Advent.PLAYER_BIT;
        fDef.filter.maskBits = Advent.DEFAULT_BIT;

        fDef.shape = shape;
        b2body.createFixture(fDef);
        b2body.createFixture(fDef).setUserData("Player");
    }

    public CollisionRect getCollisionRect() {
        return rect;
    }

    public static int getHealth() {
        return health;
    }

    public static void setHealth(int newHealth) {
        health = newHealth;
    }

    public static boolean getFacingRight() {
        return facingRight;
    }

    public static float getPositionX() {
        return b2body.getPosition().x;
    }

    public static float getPositionY() {
        return b2body.getPosition().y;
    }

    public void setPlayerEnemyCollide(boolean playerEnemyCollide) {
        this.playerEnemyCollide = playerEnemyCollide;
    }

    public boolean getPlayerEnemyCollide() {
        return playerEnemyCollide;
    }

    public void collideAsteroid() {
        health -= 10;
        UI.changeHealth(10);

    }
}
