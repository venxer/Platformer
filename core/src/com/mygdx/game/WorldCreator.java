package com.mygdx.game;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;

public class WorldCreator {
    protected Fixture fixture;

    public WorldCreator(GameScreen screen) {
        // World Creator
        World world = screen.getWorld();
        TiledMap map2 = screen.getMap();
        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();
        Body body;
        for (MapObject object : map2.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set(rectangle.getX() + rectangle.getWidth() / 2,
                    rectangle.getY() + rectangle.getHeight() / 2);

            body = world.createBody(bDef);

            shape.setAsBox(rectangle.getWidth() / 2, rectangle.getHeight() / 2);
            fDef.shape = shape;
            body.createFixture(fDef);
            fixture = body.createFixture(fDef);
            fixture.setUserData("Ground");
        }
        for (MapObject object : map2.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            // new Platform(world, map2, rectangle);
            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set(rectangle.getX() + rectangle.getWidth() / 2,
                    rectangle.getY() + rectangle.getHeight() / 2);

            body = world.createBody(bDef);

            shape.setAsBox(rectangle.getWidth() / 2, rectangle.getHeight() / 2);
            fDef.shape = shape;
            body.createFixture(fDef);
            fixture.setUserData("Platform");
        }
    }
}
