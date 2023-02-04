package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.World;

public abstract class Enemy extends Sprite {
    protected World world;
    protected GameScreen screen;

    public Enemy(GameScreen screen, float x, float y) {
        this.world = screen.getWorld();
        this.screen = screen;
        // setPosition(x, y);
        defineEnemy(x, y);
    }

    protected abstract void defineEnemy(float x, float y);

    public abstract void playerEnemyCollide(boolean collided, boolean takeDamage);
}
