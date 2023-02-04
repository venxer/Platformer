package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class UI implements Disposable {
    Stage stage;
    private Viewport viewPort;
    // private float timeCount;
    private static Integer score;
    private static Integer health;
    private static Integer diff;

    private Label scoreLabel;
    private static Label scoreNumLabel;
    private Label healthLabel;
    private static Label healthNumLabel;
    private Label diffLabel;
    private static Label diffNumLabel;

    public UI(SpriteBatch sb) {
        // timeCount = 0;
        health = 100;
        score = 0;
        diff = 0;

        viewPort = new FitViewport(Advent.width, Advent.height, new OrthographicCamera());
        stage = new Stage(viewPort, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        scoreLabel = new Label("Score", new LabelStyle(new BitmapFont(), Color.WHITE));
        scoreNumLabel = new Label(String.format("%05d", score), new LabelStyle(new BitmapFont(), Color.WHITE));
        healthLabel = new Label("Health", new LabelStyle(new BitmapFont(), Color.RED));
        healthNumLabel = new Label(String.format("%03d", health), new LabelStyle(new BitmapFont(), Color.RED));
        diffLabel = new Label("Difficulty", new LabelStyle(new BitmapFont(), Color.WHITE));
        diffNumLabel = new Label(String.format("%02d", diff), new LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(healthLabel).expandX().padTop(10);
        table.add(diffLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);
        table.row(); // creates new row
        table.add(healthNumLabel).expandX();
        table.add(diffNumLabel).expandX();
        table.add(scoreNumLabel).expandX();

        stage.addActor(table);
    }

    public static void addScore(int value) {
        score += value;
        scoreNumLabel.setText(String.format("%05d", score));
    }

    public static void changeHealth(int value) {
        health -= value;
        healthNumLabel.setText(String.format("%03d", health));
    }

    public static void addDiff(int value) {
        diff += value;
        diffNumLabel.setText(String.format("%02d", diff));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
