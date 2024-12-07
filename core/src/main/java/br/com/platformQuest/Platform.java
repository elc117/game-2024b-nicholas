package br.com.platformQuest;

import br.com.platformQuest.Helper.Constants;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class Platform {
    private ShapeRenderer shapeRenderer;
    private World world;
    private BodyDef bodyDef;
    Body platformBody;
    public Platform(World world) {
        this.world = world;
        this.shapeRenderer = new ShapeRenderer();

        // Definir o corpo físico
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(+Constants.WIDTH / Constants.PPM / 2, -1);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.WIDTH / 2 / Constants.PPM, 1);


        FixtureDef fix = new FixtureDef();
        fix.shape = shape;
        fix.friction = 0.5f;

        platformBody = world.createBody(bodyDef);
        platformBody.createFixture(fix);

        System.out.println("posicao plataforma: " + platformBody.getPosition().x + " y: " + platformBody.getPosition().y);

        shape.dispose(); // Liberar memória do shape
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
