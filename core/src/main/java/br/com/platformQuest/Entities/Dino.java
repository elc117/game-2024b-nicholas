/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.platformQuest.Entities;

import br.com.platformQuest.Helper.Collisions;
import br.com.platformQuest.Helper.Constants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import java.util.Random;

/**
 *
 * @author zorte
 */
public class Dino extends Box2DEntity implements Update, Collisions{
    private float speedX;
    private float speedY;

    public Dino(float x, float y, World world, Texture tex) {
        super(x, y, world, tex);
        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.bodyDef.position.set(x, y);
        createBody();
    }

    @Override
    public void createFixture(float hx, float hy) {
        CircleShape shape = new CircleShape();
        shape.setRadius(hx);

        fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 1f;
        fixture.friction = 0.1f;
        fixture.restitution = 0.3f;

        body.createFixture(fixture).setUserData(this);
    }

    public void shot() {
        float targetX = EntitiesManager.getInstance().getPlayer().getBody().getPosition().x;
        Random r = new Random();
        float directionX = r.nextInt() % 2 == 0 ? targetX - 22.5f : 0 + targetX; //get from left or right of the player
        float directionY = 0;
        float magnitude = (float) Math.sqrt(directionX * directionX + directionY * directionY);

        directionX /= magnitude;
        directionY /= magnitude;

        float speed = 5f;
        this.speedX = directionX * speed;
        this.speedY = directionY * speed;
        body.setLinearVelocity(new Vector2(speedX, speedY));
    }

    @Override
    public void update() {
        if(body.getPosition().y < 0){
            destroyObject();
        }
    }

    @Override
    public void destroyObject() {
        Runnable destroyOnRun = new Runnable() {
            @Override
            public void run() {
                System.out.println("Destroying platform: " + this);
                texture.dispose();
                world.destroyBody(body);
                EntitiesManager.getInstance().removeEntity(Dino.this);
            }
        };
        EntitiesManager.getInstance().scheduleAfterWorldStep(destroyOnRun);
    }

    @Override
    public void draw(SpriteBatch batch) {
        float adustedX = (body.getPosition().x * Constants.PPM) - (texture.getWidth() / 2f);
        float adustedY = (body.getPosition().y * Constants.PPM) - (texture.getHeight()/ 2f);
        batch.draw(texture, adustedX, adustedY);
    }

    @Override
    public void collisionWithPlayer() {
        System.out.println("Encostou no player, morte!");
        EntitiesManager.getInstance().getPlayer().kill();
    }



}
