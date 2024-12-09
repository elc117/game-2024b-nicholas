package br.com.platformQuest.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;


public class Platform extends Box2DEntity{

    public Platform (float x, float y, World world, Texture tex) {
        super(x, y, world, tex);
        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.StaticBody;
        this.bodyDef.position.set(x,y);
        createBody();
        createFixture();
    }

    @Override
    protected void createBody() {
        this.body = this.world.createBody(bodyDef);
    }

    @Override
    protected void createFixture() {
        super.createFixture(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    protected void draw(SpriteBatch batch) {
        super.draw(batch); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    
    
}
