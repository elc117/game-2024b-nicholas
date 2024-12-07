/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.platformQuest;

import br.com.platformQuest.Helper.Constants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public final class Player {
    Texture texture;
    BodyDef bodyDef;
    Body body;
    public Player(float x, float y, World world){
        // Configuração do corpo
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        this.body = world.createBody(bodyDef);
        this.texture = new Texture("player/players.png");
        // Criando fixture do corpo
        createFixture(body);
    }

    public void createFixture(Body body) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1, 1);  // Defina o tamanho do corpo

        // Definindo as propriedades do fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f;

        // Criando o fixture e aplicando no corpo
        body.createFixture(fixtureDef);
    }

    public void drawPlayer(SpriteBatch batch) {
        batch.draw(texture, body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        System.out.println("Posição x: " + body.getPosition().x + " y: " + body.getPosition().y);
    }

    public void dispose() {
        texture.dispose();
    }

    public void jump() {
        // Adiciona um impulso vertical (pular)
        Vector2 velocity = body.getLinearVelocity();
        if (Math.abs(velocity.y) < 0.01f) { // Verifica se está no chão (sem velocidade vertical)
            body.applyLinearImpulse(new Vector2(0, 5f), body.getWorldCenter(), true);
        }
    }

    public void moveLeft() {
        // Aplica uma força para mover à esquerda
        Vector2 velocity = body.getLinearVelocity();
        if (velocity.x > -2) { // Limita a velocidade para -2 unidades/s
            body.applyLinearImpulse(new Vector2(-0.5f, 0), body.getWorldCenter(), true);
        }
    }

    public void moveRight() {
        // Aplica uma força para mover à direita
        Vector2 velocity = body.getLinearVelocity();
        if (velocity.x < 2) { // Limita a velocidade para 2 unidades/s
            body.applyLinearImpulse(new Vector2(0.5f, 0), body.getWorldCenter(), true);
        }
    }
}
