package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Advent extends Game {
    public SpriteBatch batch;
    public static final int width = 400;
    public static final int height = 208;
    // public static final float PPM = 1;
    public static final short DEFAULT_BIT = 1;
    public static final short PLAYER_BIT = 2;
    public static final short ENEMY_BIT = 4;
    public static final short DESTOYED_BIT = 8;
    public static final short BULLET_BIT = 16;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }
}
