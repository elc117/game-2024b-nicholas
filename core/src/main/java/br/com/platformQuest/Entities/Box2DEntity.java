/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.platformQuest.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

/**
 *
 * @author zorte
 */
public class Box2DEntity {
    protected Body body;
    protected Texture texture;
    protected FixtureDef fixture;
    protected World world;
    protected BodyDef bodyDef;

    public Box2DEntity(float x, float y, World world, Texture tex){
        this.world = world;
        this.texture = tex;
    }

    public void createBody(){
        this.body = this.world.createBody(bodyDef);
    };

    public void createFixture(float hx, float hy){};

    public void draw(SpriteBatch batch){};

    public void destroyObject() {};
}
