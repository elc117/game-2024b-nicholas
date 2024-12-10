/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.platformQuest.Helper;

import br.com.platformQuest.Entities.Platform;
import br.com.platformQuest.Entities.Player;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 *
 * @author zorte
 */
public class CollisionsHandler implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        Object objA = contact.getFixtureA().getUserData();
        Object objB = contact.getFixtureB().getUserData();
        if(objA instanceof Platform && objB instanceof Player){
            ((Platform)objA).maybeDropPlatform();
        } else if (objA instanceof Player && objB instanceof Platform){
            ((Platform)objB).maybeDropPlatform();
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
