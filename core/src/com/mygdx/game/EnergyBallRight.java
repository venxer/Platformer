package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EnergyBallRight {
    private static final int speed = 500;
    private static Texture texture;
    private static int width = 25;
    private static int height = 16;

    private float x, y;
    CollisionRect rect;

    public boolean remove = false;
    private boolean used = false;

    public EnergyBallRight(float x, float y) {
        this.x = x;
        this.y = y;

        rect = new CollisionRect(x, y, width, height);

        if (texture == null) {
            texture = new Texture("Textures/EnergyBallRight.png");
        }
    }

    public void update(float delta) {
        x += speed * delta;
        // System.out.println("Right coord:" + "(" + x + "," + y + ")");
        rect.move(x, y);

        if (x > 4000) {
            remove = true;
        }
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
