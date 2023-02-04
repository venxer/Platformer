package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter.EmissionMode;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Player.State;

public class GameScreen implements Screen {
    private Advent game;
    private OrthographicCamera camera;
    private Viewport viewPort;
    private UI UI;
    private TmxMapLoader map;
    private TiledMap map2;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer box2dDebugRenderer;

    private Player player;
    private ArrayList<EnergyBallRight> energyBallsRight = new ArrayList<>();
    private ArrayList<EnergyBallLeft> energyBallsLeft = new ArrayList<>();
    public static final float shootWaitTimeRight = 0.4f;
    public static final float shootWaitTimeLeft = 0.4f;
    private float shootTimerRight;
    private float shootTimerLeft;

    // private Enemy1 enemy;
    private Random random;
    private float enemySpawnTimer;
    private static float minEnemySpawnTime = 0.1f;
    private static float maxEnemySpawnTime = 10f;
    private ArrayList<Enemy1> enemies;
    private int enemiesKilled;
    private int totalKilled;

    // Asteroid
    private Random random2;
    private float asteroidSpawnTimer;
    private static float minAsteroidSpawnTime = 1f;
    private static float maxAsteroidSpawnTime = 2f;
    private ArrayList<Asteroid> asteroids;

    private Vector2 gravity = new Vector2(0, -70);

    private boolean jumping = false;
    private boolean onGround = true;

    private TextureAtlas atlas;

    private static int score;
    private BitmapFont font;

    public GameScreen(Advent game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewPort = new FitViewport(1000, 480, camera);
        UI = new UI(game.batch);
        map = new TmxMapLoader();
        map2 = map.load("Map/mapv3.tmx");
        renderer = new OrthogonalTiledMapRenderer(map2);
        camera.position.set(viewPort.getWorldWidth() / 2, viewPort.getWorldHeight() / 2, 0); // centers cam

        // sets the ground
        world = new World(new Vector2(0, 0), true); // vector2: Gravity
        world.setContactListener(new ContactListenter1());

        box2dDebugRenderer = new Box2DDebugRenderer();
        new WorldCreator(this);

        // atlas = new TextureAtlas("AllTextures/AllTextures.atlas");
        atlas = new TextureAtlas("AllTextures/AllTextures.atlas");

        player = new Player(this);
        // enemy = new Enemy1(this, 1000, 200);
        shootTimerRight = 0;
        shootTimerLeft = 0;
        enemies = new ArrayList<>();
        asteroids = new ArrayList<>();

        random = new Random();
        enemySpawnTimer = random.nextFloat() * (maxEnemySpawnTime - minEnemySpawnTime) + minEnemySpawnTime;
        asteroidSpawnTimer = random.nextFloat() * (maxAsteroidSpawnTime - minAsteroidSpawnTime) + minAsteroidSpawnTime;
        score = 0;
        font = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void render(float delta) {
        update(delta);
        // RIGHT SHOOT
        shootTimerRight += delta;
        if (Gdx.input.isKeyPressed(Keys.F) && shootTimerRight >= shootWaitTimeRight) {
            shootTimerRight = 0;
            if (Player.getFacingRight()) {
                energyBallsRight.add(new EnergyBallRight(Player.getPositionX() + 10, Player.getPositionY() - 5));
            }
        }
        ArrayList<EnergyBallRight> energyBallsRightToRemove = new ArrayList<>();
        for (EnergyBallRight energyBallRight : energyBallsRight) {
            energyBallRight.update(delta);
            if (energyBallRight.remove) {
                energyBallsRightToRemove.add(energyBallRight);
                // energyBallsRightToRemove.remove(energyBallRight);
            }
        }
        // energyBallsRight.remove(energyBallsRightToRemove);

        // LEFT SHOOT
        shootTimerLeft += delta;
        if (Gdx.input.isKeyPressed(Keys.F) && shootTimerLeft >= shootWaitTimeLeft) {
            shootTimerLeft = 0;

            if (!Player.getFacingRight()) {
                energyBallsLeft.add(new EnergyBallLeft(Player.getPositionX() - 10, Player.getPositionY() - 5));
            }
        }
        ArrayList<EnergyBallLeft> energyBallsLeftToRemove = new ArrayList<>();
        for (EnergyBallLeft energyBallLeft : energyBallsLeft) {
            energyBallLeft.update(delta);
            if (energyBallLeft.remove) {
                energyBallsLeftToRemove.add(energyBallLeft);
                // energyBallsLeft.remove(energyBallLeft);
            }
        }
        // energyBallsLeft.remove(energyBallsLeftToRemove);

        // SPAWN
        if (enemiesKilled >= 10) {
            if (maxEnemySpawnTime > 2) {
                maxEnemySpawnTime--;
                if (maxAsteroidSpawnTime > 1) {
                    maxAsteroidSpawnTime -= 0.3;
                }
                UI.addDiff(1);
                enemiesKilled = 0;
                System.out.println("Increase Spawn Rate");
            }
        }

        enemySpawnTimer -= delta;
        if (enemySpawnTimer <= 0) {
            enemySpawnTimer = random.nextFloat() * (maxEnemySpawnTime - minEnemySpawnTime) + minEnemySpawnTime;
            enemies.add(new Enemy1(this, random.nextFloat() * (3020 - 182) + 182, 200));
            System.out.println("ENEMY SPAWNED");
        }

        asteroidSpawnTimer -= delta;
        if (asteroidSpawnTimer <= 0) {
            asteroidSpawnTimer = random.nextFloat() * (maxAsteroidSpawnTime - minAsteroidSpawnTime)
                    + minAsteroidSpawnTime;
            asteroids.add(new Asteroid(random.nextFloat() * (3040 - 0) + 0, 350));
            // System.out.println(player.getPositionY());
            System.out.println("ASTERIODS!");

        }

        ArrayList<Enemy1> enemiesToRemove = new ArrayList<>();
        for (Enemy1 enemy : enemies) {
            enemy.update(delta);
            if (enemy.getDestoryed()) {
                enemiesToRemove.add(enemy);
                enemiesKilled++;
                totalKilled++;
                UI.addScore(1);
                System.out.println(enemies + "Enemies Killed");
            }
        }

        ArrayList<Asteroid> asteriodsToRemove = new ArrayList<>();
        for (Asteroid asteroid : asteroids) {
            asteroid.update(delta);
            if (asteroid.remove) {
                asteriodsToRemove.add(asteroid);
            }
        }
        // enemies.removeAll(enemiesToRemove);

        // HIT
        for (EnergyBallLeft energyBallLeft : energyBallsLeft) {
            for (Enemy1 enemy : enemies) {
                if (energyBallLeft.getCollisionRect().collidesWith(enemy.getCollisionRect())
                        && !energyBallLeft.getUsed()) {
                    System.out.println("HIT");
                    energyBallsLeftToRemove.add(energyBallLeft);
                    energyBallLeft.setUsed(true);
                    if (enemy.getDestoryed()) {
                        enemiesToRemove.add(enemy);
                    }
                    enemy.playerEnemyCollide(false, true);
                }
            }
        }
        for (EnergyBallRight energyBallRight : energyBallsRight) {
            for (Enemy1 enemy : enemies) {

                if (energyBallRight.getCollisionRect().collidesWith(enemy.getCollisionRect())
                        && !energyBallRight.getUsed()) {
                    System.out.println("HIT");
                    energyBallsRightToRemove.add(energyBallRight);
                    energyBallRight.setUsed(true);

                    if (enemy.getDestoryed()) {
                        enemiesToRemove.add(enemy);
                    }
                    enemy.playerEnemyCollide(false, true);
                }
            }
        }
        for (Asteroid asteroid : asteroids) {
            if (player.getCollisionRect().collidesWith(asteroid.getCollisionRect())) {
                System.out.println("HIT");
                asteriodsToRemove.add(asteroid);

                if (asteroid.remove) {
                    asteriodsToRemove.add(asteroid);
                }
                player.collideAsteroid();
            }
        }

        // Remove ALl
        energyBallsLeft.removeAll(energyBallsLeftToRemove);
        energyBallsRight.removeAll(energyBallsRightToRemove);
        enemies.removeAll(enemiesToRemove);
        asteroids.remove(asteriodsToRemove);

        // Clear Screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Renders Map
        renderer.render();

        // render ground
        box2dDebugRenderer.render(world, camera.combined);

        // player
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        // GlyphLayout scoreLayout = new GlyphLayout(font, "" + score);
        // font.draw(game.batch, scoreLayout, Gdx.graphics.getWidth() /2 -
        // scoreLayout.width /2, Gdx.graphics.getHeight() /2 - scoreLayout.height /2 -
        // 10);
        for (EnergyBallRight energyBallRight : energyBallsRight) {
            energyBallRight.render(game.batch);
        }
        for (EnergyBallLeft energyBallLeft : energyBallsLeft) {
            energyBallLeft.render(game.batch);
        }
        for (Enemy1 enemy : enemies) {
            enemy.draw(game.batch);
        }
        for (Asteroid asteroid : asteroids) {
            asteroid.render(game.batch);
        }
        player.draw(game.batch);
        // enemy.draw(game.batch);
        game.batch.end();

        // UI
        game.batch.setProjectionMatrix(UI.stage.getCamera().combined);
        UI.stage.draw();

        // Game Over
        if (gameOver()) {
            game.setScreen(new GameOver(game));
            dispose();
        }

    }

    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height);
    }

    public void update(float delta) {
        // System.out.println(enemySpawnTimer);
        // System.out.println("X: " + Player.getPositionX());
        // System.out.println("Y: " + player.b2body.getLinearVelocity().y);
        if (Math.abs(player.b2body.getLinearVelocity().y) <= 10) {
            onGround = true;
        } else {
            onGround = false;
        }

        // energyBallRight(delta);
        // energyBallLeft(delta);
        // spawnEnemy(delta);
        // checkBulletEnemy(delta);

        handleInput(delta);
        // enemyMovement(delta);

        world.step(1 / 60f, 6, 2);
        camera.position.x = player.b2body.getPosition().x;
        player.update(delta);
        // enemy.update(delta);
        camera.update();
        renderer.setView(camera);
    }

    // public void checkBulletEnemy(float delta) {
    // for (EnergyBallLeft energyBallLeft : energyBallsLeft) {
    // for (Enemy1 enemy : enemies) {
    // if (energyBallLeft.getCollisionRect().collidesWith(enemy.getCollisionRect()))
    // {
    // System.out.println("HIT");
    // enemy.enemyBulletCollide();
    // }
    // }
    // }
    // for (EnergyBallRight energyBallRight : energyBallsRight) {
    // for (Enemy1 enemy : enemies) {
    // if
    // (energyBallRight.getCollisionRect().collidesWith(enemy.getCollisionRect())) {
    // System.out.println("HIT");
    // enemy.enemyBulletCollide();
    // }
    // }
    // }
    // }

    public void spawnEnemy(float delta) {
        if (enemiesKilled >= 10) {
            if (maxEnemySpawnTime > 2) {
                maxEnemySpawnTime--;
                enemiesKilled = 0;
                System.out.println("Increase Spawn Rate");
            }
        }

        enemySpawnTimer -= delta;
        if (enemySpawnTimer <= 0) {
            enemySpawnTimer = random.nextFloat() * (maxEnemySpawnTime - minEnemySpawnTime) + minEnemySpawnTime;
            enemies.add(new Enemy1(this, random.nextFloat() * (3020 - 182) + 182, 200));
            System.out.println("ENEMY SPAWNED");
        }
        ArrayList<Enemy1> enemiesToRemove = new ArrayList<>();
        for (Enemy1 enemy : enemies) {
            enemy.update(delta);
            if (enemy.getDestoryed()) {
                enemiesToRemove.add(enemy);
                enemiesKilled++;
                System.out.println(enemies + "Enemies Killed");
            }
        }
        enemies.removeAll(enemiesToRemove);
    }

    public static int getScore() {
        return score;
    }

    public void energyBallRight(float delta) {
        shootTimerRight += delta;
        if (Gdx.input.isKeyPressed(Keys.F) && shootTimerRight >= shootWaitTimeRight) {
            shootTimerRight = 0;
            if (Player.getFacingRight()) {
                energyBallsRight.add(new EnergyBallRight(Player.getPositionX() + 10, Player.getPositionY() - 5));
            }
        }
        ArrayList<EnergyBallRight> energyBallsRightToRemove = new ArrayList<>();
        for (EnergyBallRight energyBallRight : energyBallsRight) {
            energyBallRight.update(delta);
            if (energyBallRight.remove) {
                energyBallsRightToRemove.remove(energyBallRight);
            }
        }
        energyBallsRight.remove(energyBallsRightToRemove);
    }

    public void energyBallLeft(float delta) {
        shootTimerLeft += delta;
        if (Gdx.input.isKeyPressed(Keys.F) && shootTimerLeft >= shootWaitTimeLeft) {
            shootTimerLeft = 0;

            if (!Player.getFacingRight()) {
                energyBallsLeft.add(new EnergyBallLeft(Player.getPositionX() - 10, Player.getPositionY() - 5));
            }
        }
        ArrayList<EnergyBallLeft> energyBallsLeftToRemove = new ArrayList<>();
        for (EnergyBallLeft energyBallLeft : energyBallsLeft) {
            energyBallLeft.update(delta);
            if (energyBallLeft.remove) {
                energyBallsLeft.remove(energyBallLeft);
            }
        }
        energyBallsLeft.remove(energyBallsLeftToRemove);
    }

    public boolean gameOver() {
        if (player.currentState == Player.State.DEAD && player.getStateTimer() > 3) {
            return true;
        }
        return false;
    }

    public void handleInput(float delta) {
        // System.out.println(player.b2body.getLinearVelocity().x);
        // System.out.println(player.b2body.getWorldCenter().y);
        // System.out.println(onGround);
        player.b2body.applyForceToCenter(gravity, true); // Gravity
        // double yPos = player.b2body.getWorldCenter().y - 28; // center - radius
        // if (yPos <= 165) {
        // onGround = true;
        // }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && onGround) // Jump
        {
            // System.out.println("Jump");
            player.b2body.applyLinearImpulse(new Vector2(0, 90f),
                    player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) && player.b2body.getLinearVelocity().x <= 70) // Right
        {
            // System.out.println("D");
            player.b2body.applyForce(100f, 0, player.b2body.getWorldCenter().x,
                    player.b2body.getWorldCenter().y, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) && player.b2body.getLinearVelocity().x >= -70) // Left
        {
            // System.out.println("A");
            player.b2body.applyForce(-100f, 0, player.b2body.getWorldCenter().x,
                    player.b2body.getWorldCenter().y,
                    true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E) && player.b2body.getLinearVelocity().x <= 70) // Right
        {
            // System.out.println("D");
            player.b2body.applyLinearImpulse(100f, 0, player.b2body.getWorldCenter().x,
                    player.b2body.getWorldCenter().y, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q) && player.b2body.getLinearVelocity().x >= -70) // Left
        {
            // System.out.println("A");
            player.b2body.applyLinearImpulse(-100f, 0, player.b2body.getWorldCenter().x,
                    player.b2body.getWorldCenter().y,
                    true);
        }
    }

    // public void enemyMovement(float delta) {
    // enemy.b2BodyEnemy.applyForceToCenter(gravity, true);
    // if (!enemy.getCollided()) {
    // float diff = player.b2body.getWorldCenter().x -
    // enemy.b2BodyEnemy.getWorldCenter().x;
    // if (diff < 0 && enemy.b2BodyEnemy.getLinearVelocity().x >= -40) {
    // enemy.b2BodyEnemy.applyForce(-50f, 0, enemy.b2BodyEnemy.getWorldCenter().x,
    // enemy.b2BodyEnemy.getWorldCenter().y, true);
    // } else if (diff > 0 && enemy.b2BodyEnemy.getLinearVelocity().x <= 40) {
    // enemy.b2BodyEnemy.applyForce(50f, 0, enemy.b2BodyEnemy.getWorldCenter().x,
    // enemy.b2BodyEnemy.getWorldCenter().y, true);
    // }
    // }
    // }

    public TiledMap getMap() {
        return map2;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }

    @Override
    public void dispose() {
        map2.dispose();
        renderer.dispose();
        world.dispose();
        box2dDebugRenderer.dispose();
        UI.dispose();
    }
}