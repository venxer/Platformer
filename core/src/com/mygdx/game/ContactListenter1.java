package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;

public class ContactListenter1 implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        // if (a.getUserData() == "Ground" || b.getUserData() == "Ground") {
        // System.out.println("Ground Collision");
        // return;
        // }

        // if (a.getUserData() == "Enemy1" || b.getUserData() == "Enemy1") {
        if (a.getUserData() != null && b.getUserData() != null) {
            if (a.getUserData() == "Player" || b.getUserData() == "Player") {
                Fixture player = a.getUserData() == "Player" ? a : b;
                Fixture enemy = player == a ? b : a;
                if (enemy.getUserData() != null && Enemy1.class.isAssignableFrom(enemy.getUserData().getClass())) {
                    ((Enemy1) enemy.getUserData()).playerEnemyCollide(true, false);

                }
            }
        }

        // }
        // if (a.getUserData() == "Ground" || b.getUserData() == "Ground") {
        // System.out.println("Ground Collision");
        // return;
        // }
        // if (isEntityContact(a, b)) {
        // System.out.println("collidedListen");
        // }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData() != null && b.getUserData() != null) {
            if (a.getUserData() == "Player" || b.getUserData() == "Player") {
                Fixture player = a.getUserData() == "Player" ? a : b;
                Fixture enemy = player == a ? b : a;
                if (enemy.getUserData() != null && Enemy1.class.isAssignableFrom(enemy.getUserData().getClass())) {
                    ((Enemy1) enemy.getUserData()).playerEnemyCollide(false, false);
                    // System.out.println("Player Enemy Collision Off");

                }
                // return;
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    // private boolean isEntityContact(Fixture a, Fixture b) {
    // if (a.getUserData() instanceof Player || b.getUserData() instanceof Player) {
    // if (a.getUserData() instanceof Mino || b.getUserData() instanceof Mino) {
    // return true;
    // }
    // }
    // return false;
    // }
}
