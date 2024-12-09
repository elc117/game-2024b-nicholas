/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.platformQuest.Entities;

import br.com.platformQuest.Entities.Box2DEntity;
import br.com.platformQuest.Helper.Constants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Box2DEntity{

    private boolean dead = false;

    public Player(float x, float y, World world, Texture tex) {
        super(x, y, world, tex);
        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.bodyDef.position.set(x,y);
        this.bodyDef.fixedRotation = true;
        createBody();
    }

    @Override
    public void createBody() {
        this.body = this.world.createBody(bodyDef);
    }

    @Override
    public void createFixture(float hx, float hy) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(hx, hy);  // Defina o tamanho do corpo

        // Definindo as propriedades do fixture
        this.fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 0.9f;
        fixture.friction = 0.7f;
        fixture.restitution = 0.1f;

        // Criando o fixture e aplicando no corpo
        body.createFixture(fixture).setUserData(this);
    }

    @Override
    public void draw(SpriteBatch batch) {
        float adjustedX = (body.getPosition().x * Constants.PPM) - (texture.getWidth() / 2f);
        float adjustedY = (body.getPosition().y * Constants.PPM) - (texture.getHeight() / 2f);

        // Desenhar a textura na posição ajustada
        batch.draw(texture, adjustedX, adjustedY);
    }

    public void dispose() {
        texture.dispose();
    }

    public void jump() {
        // Adiciona um impulso vertical (pular)
        Vector2 velocity = body.getLinearVelocity();
        if (Math.abs(velocity.y) == 0.0f) { // Verifica se está no chão (sem velocidade vertical)
            body.applyLinearImpulse(new Vector2(0, 25f), body.getWorldCenter(), true);
        }
    }

    public void moveLeft() {
        // Aplica uma força para mover à esquerda
        Vector2 velocity = body.getLinearVelocity();
        if (velocity.x > -3) { // Limita a velocidade para -2 unidades/s
            body.applyLinearImpulse(new Vector2(-1f, 0), body.getWorldCenter(), true);
        }
        if (body.getPosition().x <= 0.1) {
            // Reduz a velocidade (desacelera)
            body.setLinearVelocity(new Vector2(0,0));
            body.setTransform(new Vector2(0.2f, body.getPosition().y), 0);
        }
    }

    public void moveRight() {
        // Aplica uma força para mover à direita
        Vector2 velocity = body.getLinearVelocity();
        if (velocity.x < 3) { // Limita a velocidade para 2 unidades/s
            body.applyLinearImpulse(new Vector2(1f, 0), body.getWorldCenter(), true);
        }
        if (body.getPosition().x >= 21) {
            // Reduz a velocidade (desacelera)
            body.setLinearVelocity(new Vector2(0,0));
            body.setTransform(new Vector2(20.5f, body.getPosition().y), 0);
        }
    }
}
