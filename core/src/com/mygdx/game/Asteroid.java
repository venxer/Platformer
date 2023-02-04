package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Asteroid {
    private static final int speed = 400;
    private static Texture texture;
    private static int width = 14;
    private static int height = 32;

    private float x, y;
    CollisionRect rect;

    public boolean remove = false;
    private boolean used = false;

    public Asteroid(float x, float y) {
        this.x = x;
        this.y = y;

        rect = new CollisionRect(x, y, width, height);

        if (texture == null) {
            texture = new Texture("Textures/asteroid.png");
        }
    }

    public void update(float delta) {
        y -= speed * delta;
        // System.out.println("Left coord:" + "(" + x + "," + y + ")");

        rect.move(x, y);
        // if (y < 120) {
        // remove = true;
        // }
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean getUsed() {
        return used;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public CollisionRect getCollisionRect() {
        return rect;
    }
}
