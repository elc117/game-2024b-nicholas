/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.platformQuest.Entities;

import br.com.platformQuest.Helper.Constants;
import br.com.platformQuest.Main;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Box2DEntity implements Update{

    private boolean live;

    public Player(float x, float y, World world, Texture tex) {
        super(x, y, world, tex);
        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.bodyDef.position.set(x,y);
        this.bodyDef.fixedRotation = true;
        this.live = true;
        createBody();
    }

    @Override
    public void createFixture(float hx, float hy) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(hx, hy);  // Defina o tamanho do corpo

        // Definindo as propriedades do fixture
        this.fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 1f;
        fixture.friction = 0.7f;
        fixture.restitution = 0.1f;

        // Criando o fixture e aplicando no corpo
        body.createFixture(fixture).setUserData(this);
        shape.dispose();
    }

    @Override
    public void draw(SpriteBatch batch) {
        float adjustedX = (body.getPosition().x * Constants.PPM) - (texture.getWidth() / 2f);
        float adjustedY = (body.getPosition().y * Constants.PPM) - (texture.getHeight() / 2f);

        batch.draw(texture, adjustedX, adjustedY);
    }

    public void dispose() {
        texture.dispose();
    }

    public void jump() {
        Vector2 velocity = body.getLinearVelocity();
        if (Math.abs(velocity.y) <= 0.0f) {
            body.applyLinearImpulse(new Vector2(0, 80f), body.getWorldCenter(), true);
        }
    }

    public void moveLeft() {
        Vector2 velocity = body.getLinearVelocity();
        if (velocity.x > -4) {
            body.applyLinearImpulse(new Vector2(-3f, 0), body.getWorldCenter(), true);
        }
    }

    public void moveRight() {
        // Aplica uma força para mover à direita
        Vector2 velocity = body.getLinearVelocity();
        if (velocity.x < 4) { // Limita a velocidade para 2 unidades/s
            body.applyLinearImpulse(new Vector2(3f, 0), body.getWorldCenter(), true);
        }
    }

    @Override
    public void update() {
        if(body.getPosition().y < 0) {
            System.out.println("Player died!");
            live = false;
        }
        if(!live){
            Main.STATUS = Main.status.DEFEAT;
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
                EntitiesManager.getInstance().removeEntity(Player.this);
            }
        };
        EntitiesManager.getInstance().scheduleAfterWorldStep(destroyOnRun);
    }

    public boolean isAlive() {
        return live;
    }

    public Body getBody() {
        return this.body;
    }

    public void kill() {
        this.live = false;
    }

}
