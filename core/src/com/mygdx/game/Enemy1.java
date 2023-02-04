package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

public class Enemy1 extends Enemy {

    public Body b2BodyEnemy;
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> damagedAnimation;
    private Animation<TextureRegion> deadAnimation;

    private boolean setToDestory;
    private boolean destroyed;
    private int health = 20;
    private boolean collided = false;

    public State currentState;
    public State previousState;
    private Vector2 gravity = new Vector2(0, -70);

    private static int width = 48;
    private static int height = 48;
    CollisionRect rect;

    private float attackTimer;
    private float attactWaitTime = 2f;

    private float x, y;

    public enum State {
        IDLE, WALKING, ATTACKING, DAMAGED, DEAD
    };

    private boolean facingRight = false;

    public Enemy1(GameScreen screen, float x, float y) {
        super(screen, x, y);
        // this.x = x;
        // this.y = y;
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Enemy"), i * 48, 2
                    * 48, 48, 48));
        }
        walkAnimation = new Animation<TextureRegion>(0.3f, frames);

        for (int i = 2; i < 6; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Enemy"), i * 48, 3
                    * 48, 48, 48));
        }
        attackAnimation = new Animation<TextureRegion>(0.1f, frames);

        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Enemy"), i * 48, 0
                    * 48, 48, 48));
        }
        damagedAnimation = new Animation<TextureRegion>(0.3f, frames);

        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Enemy"), i * 48, 1
                    * 48, 48, 48));
        }
        deadAnimation = new Animation<TextureRegion>(0.3f, frames);

        rect = new CollisionRect(b2BodyEnemy.getPosition().x, b2BodyEnemy.getPosition().y, width - 20, height);

        stateTime = 0;
        setBounds(0, 0, 48, 48);
        setToDestory = false;
        destroyed = false;
        attackTimer = 0;
    }

    public void update(float delta) {
        stateTime += delta;
        // setRegion(getFrame(delta));
        setRegion(getFrame(delta));
        enemyMovement(delta);
        // System.out.println("enemy coord:" + "(" + x + "," + y + ")");

        if (setToDestory && !destroyed) {
            // System.out.println(1);
            world.destroyBody(b2BodyEnemy);
            destroyed = true;
            stateTime = 0;

            // setRegion(deadAnimation.getKeyFrame(stateTime));
        } else if (!destroyed) {
            // System.out.println(setToDestory + " " + !destroyed);
            attackTimer += delta;
            if (getState() == State.ATTACKING) {
                if (attackTimer >= attactWaitTime) {
                    System.out.println(1);
                    attackTimer = 0;
                    if (collided) {
                        Player.setHealth(Player.getHealth() - 5); // player health = old health - 10 damage
                        UI.changeHealth(5);
                    }
                }
            }

            rect.move(b2BodyEnemy.getPosition().x, b2BodyEnemy.getPosition().y);
            setPosition(b2BodyEnemy.getPosition().x - getWidth() / 2,
                    b2BodyEnemy.getPosition().y - getHeight() / 2);
        }
    }

    public TextureRegion getFrame(float delta) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case DEAD:
                region = deadAnimation.getKeyFrame(stateTime);
                break;
            // case WALKING:
            // region = walkAnimation.getKeyFrame(stateTime, true);
            // break;
            case DAMAGED:
                region = damagedAnimation.getKeyFrame(stateTime);
                break;
            case ATTACKING:
                region = attackAnimation.getKeyFrame(stateTime, true);
                break;
            default:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
        }

        if (collided) {
            float diff = Player.getPositionX() - b2BodyEnemy.getPosition().x;
            if ((diff < 0 || !facingRight) && !region.isFlipX()) {
                region.flip(true, false);
                facingRight = false;
            } else if ((diff > 0 || facingRight) && region.isFlipX()) {
                region.flip(true, false);
                facingRight = true;
            }
        } else {
            if ((b2BodyEnemy.getLinearVelocity().x < 0 || !facingRight) && !region.isFlipX()) {
                region.flip(true, false);
                facingRight = false;
            } else if ((b2BodyEnemy.getLinearVelocity().x > 0 || facingRight) && region.isFlipX()) {
                region.flip(true, false);
                facingRight = true;
            }
        }

        stateTime = currentState == previousState ? stateTime + delta : 0;
        previousState = currentState;
        return region;
    }

    @Override
    public void draw(Batch batch) {
        if (!destroyed || stateTime < 5) {
            super.draw(batch);
        }
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }

    protected void defineEnemy(float x, float y) {
        BodyDef bDef = new BodyDef();
        bDef.position.set(x, y);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2BodyEnemy = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(22);
        // fDef.filter.categoryBits = Advent.PLAYER_BIT;
        // fDef.filter.maskBits = Advent.DEFAULT_BIT;

        fDef.shape = shape;
        b2BodyEnemy.createFixture(fDef);
        b2BodyEnemy.createFixture(fDef).setUserData(this);
    }

    public State getState() {
        if (getHealth() <= 0) {
            return State.DEAD;
        } else if (collided) {
            return State.ATTACKING;
        } else if (b2BodyEnemy.getLinearVelocity().x != 0) {
            return State.WALKING;
        } else {
            return State.IDLE;
        }
    }

    public boolean getCollided() {
        return collided;
    }

    public boolean getDestoryed() {
        return destroyed;
    }

    public CollisionRect getCollisionRect() {
        return rect;
    }

    public void enemyMovement(float delta) {
        b2BodyEnemy.applyForceToCenter(gravity, true);
        if (!getCollided()) {
            float diff = Player.b2body.getWorldCenter().x -
                    b2BodyEnemy.getWorldCenter().x;
            if (diff < 0 && b2BodyEnemy.getLinearVelocity().x >= -40) {
                b2BodyEnemy.applyForce(-30f, 0, b2BodyEnemy.getWorldCenter().x,
                        b2BodyEnemy.getWorldCenter().y, true);
            } else if (diff > 0 && b2BodyEnemy.getLinearVelocity().x <= 40) {
                b2BodyEnemy.applyForce(30f, 0, b2BodyEnemy.getWorldCenter().x,
                        b2BodyEnemy.getWorldCenter().y, true);
            }
        }
    }

    @Override
    public void playerEnemyCollide(boolean collided, boolean takeDamage) {
        // System.out.println("dauihjsdujiads");
        this.collided = collided;
        // if (collided) {
        // Player.setHealth(Player.getHealth() - 5); // player health = old health - 10
        // damage
        // UI.changeHealth(5);
        // }

        if (takeDamage) {
            health -= 5;
            if (health <= 0) {
                setToDestory = true;
            }
            System.out.println("Enemy HP:" + health);
        }
        System.out.println("Player HP:" + Player.getHealth());

    }
}
