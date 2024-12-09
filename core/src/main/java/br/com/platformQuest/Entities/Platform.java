package br.com.platformQuest.Entities;

import br.com.platformQuest.Helper.Constants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Queue;


public class Platform extends Box2DEntity{
    private int secBeforeDrop = 10;
    private long timestamp = 0;
    private boolean isDroppable;
    private Queue<Runnable> afterWordStep;
    public Platform (float x, float y, World world, Texture tex) {
        super(x, y, world, tex);
        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.StaticBody;
        this.bodyDef.position.set(x,y);
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
        fixture.density = 0.5f;
        fixture.friction = 0.1f;
        fixture.restitution = 0.0f;

        // Criando o fixture e aplicando no corpo
        body.createFixture(fixture).setUserData(this);
        shape.dispose();
    }

    public void startDropTimer() {
        timestamp = TimeUtils.millis()/1000;
    }

    public void update() {
        if (isDroppable && timestamp != 0 && TimeUtils.millis() / 1000 - timestamp >= secBeforeDrop) {
            afterWordStep.add(new Runnable() {
                @Override
                public void run() {
                    body.setType(BodyDef.BodyType.DynamicBody);
                }
            });
            isDroppable = false;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        update();
        System.out.println("timeStamp: " + (TimeUtils.millis() / 1000 - timestamp));
        float adjustedX = (body.getPosition().x * Constants.PPM) - (texture.getWidth() / 2f);
        float adjustedY = (body.getPosition().y * Constants.PPM) - (texture.getHeight() / 2f);

        // Desenhar a textura na posição ajustada
        batch.draw(texture, adjustedX, adjustedY);
    }

    public void setIsDroppable(boolean isDroppable) {
        this.isDroppable = isDroppable;
    }

    public void setAfterWordStep(Queue<Runnable> afterWordStep) {
        this.afterWordStep = afterWordStep;
    }
    
    
}
