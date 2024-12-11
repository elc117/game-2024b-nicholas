package br.com.platformQuest.Entities;

import br.com.platformQuest.Helper.Collisions;
import br.com.platformQuest.Questions.Answer;
import br.com.platformQuest.Helper.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Random;

public class Platform extends Box2DEntity implements Update, Collisions {

    private int elapsedTime = 0;
    protected long timeStart = 0;
    protected boolean timerOn = false;
    private boolean resizable = false;
    private static final int TIME_TO_WAIT = 15;
    private Answer ans;
    BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/timerFont.fnt"), Gdx.files.internal("fonts/timerFont.png"), false);

    public Platform(float x, float y, World world, Texture tex) {
        super(x, y, world, tex);
        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.StaticBody;
        this.bodyDef.position.set(x, y);
        createBody();
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

    public void collisionWithPlayer() {
        if (isTheRightPlatform()) {
            if (!timerOn && isPlayerAbove()) {// start the timer to drop this platform
                this.ans = null;
                resizePlatform(Constants.WIDTH / Constants.PPM, 1, new Texture("bigPlatform.png"));
                timeStart = TimeUtils.millis() / 1000;
                timerOn = true;
                EntitiesManager.getInstance().playerHitCorrectAnswer();//player hit the correct platform
            }
        } else if (isPlayerAbove()) {
            dropPlatform();
        }
    }

    public void dropPlatform() {
        EntitiesManager.getInstance().scheduleAfterWorldStep(new Runnable() {
            @Override
            public void run() {
                body.setType(BodyDef.BodyType.DynamicBody);
            }
        });
    }

    @Override
    public void update() {
        verifyTimer();
        maybeDestroyObject();
    }

    private boolean isTheRightPlatform() {
        EntitiesManager instance = EntitiesManager.getInstance();
        return instance.getQuestCreator().getActualQuest().getCorrectAnswer().equals(this.ans);
    }

    private void verifyTimer() {
        if (timerOn) {
            elapsedTime = Math.abs((int) (TimeUtils.millis() / 1000 - (timeStart + TIME_TO_WAIT)));
            if (elapsedTime <= 0) {
                dropPlatform();
                timerOn = false;
            }
        }
    }

    protected boolean isPlayerAbove() {
        float yPlayer = EntitiesManager.getInstance().getPlayer().body.getPosition().y;
        float yPlatform = this.body.getPosition().y;
        return yPlayer + 1 > yPlatform+1;
    }

    @Override
    public void draw(SpriteBatch batch) {
        float adjustedX = (body.getPosition().x * Constants.PPM) - (texture.getWidth() / 2f);
        float adjustedY = (body.getPosition().y * Constants.PPM) - (texture.getHeight() / 2f);
        // Desenhar a textura na posição ajustada
        batch.draw(texture, adjustedX, adjustedY);
        writeTimeInScreen(batch);
        writeAnswer(batch);
    }

    private void writeTimeInScreen(SpriteBatch batch) {
        if (timerOn) {
            float centerX = body.getPosition().x * Constants.PPM;
            float centerY = body.getPosition().y * Constants.PPM;
            centerY = centerY - 18 - texture.getHeight() / 2;
            font.draw(batch, String.valueOf(elapsedTime), centerX, centerY);
        }
    }

    private void maybeDestroyObject() {
        if (body.getPosition().y < -1) {
            destroyObject();
        }
    }

    public void destroyObject() {
        Runnable destroyOnRun = new Runnable() {
            @Override
            public void run() {
                System.out.println("Destroying platform: " + this);
                texture.dispose();
                world.destroyBody(body);
                EntitiesManager.getInstance().removeEntity(Platform.this);
            }
        };
        EntitiesManager.getInstance().scheduleAfterWorldStep(destroyOnRun);
    }

    public Answer getAns() {
        return ans;
    }

    public void setAns(Answer ans) {
        this.ans = ans;
    }

    private void writeAnswer(SpriteBatch batch) {
        if (this.ans != null) {
            float centerX = body.getPosition().x * Constants.PPM;
            float centerY = body.getPosition().y * Constants.PPM;
            font.draw(batch, ans.toString(), centerX, centerY + 20);
        }
    }

    public void resizePlatform(float newWidth, float newHeight, Texture tex) {
        if (resizable) {
            EntitiesManager.getInstance().scheduleAfterWorldStep(new Runnable() {
                @Override
                public void run() {
                    // Primeiro, remova os fixtures antigos do corpo
                    System.out.println("Resizing");
                    body.destroyFixture(body.getFixtureList().get(0));

                    // Crie um novo shape com o tamanho atualizado
                    PolygonShape newShape = new PolygonShape();
                    newShape.setAsBox(newWidth / 2, newHeight / 2); // Divida por 2 porque Box2D trabalha com dimensões relativas ao centro

                    // Atualize o FixtureDef com o novo shape
                    fixture.shape = newShape;

                    // Adicione o novo fixture ao corpo
                    body.createFixture(fixture).setUserData(this);

                    // Atualize a textura (opcional)
                    texture.dispose(); // Libere a textura antiga, se necessário
                    texture = tex;// Substitua pela textura correspondente ao novo tamanho

                    body.setTransform(new Vector2((Constants.WIDTH / Constants.PPM) / 2, body.getPosition().y), 0);
                    // Libere o shape usado
                    newShape.dispose();
                }
            });
        }
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public static float[] generateNewPositions(){
        float[] points = {-1f,-1f};
        Random r = new Random();
        while(isPointOutOfView(points[0])){
            points[0] = r.nextFloat()*22.5f;
        }
        while(isPointOutOfView(points[1]) || plataformIsAboveOther(points[0], points[1])){
            points[1] = r.nextFloat()*22.5f;
        }
        return points;
    }

    private static boolean isPointOutOfView(float p){
        return p + 2 > 22.5f || p - 2 < 0;
    }

    private static boolean plataformIsAboveOther(float otherPlat, float p){
        if(p > otherPlat){
            return (p - otherPlat) < 6;
        } else if(otherPlat > p){
            return (otherPlat - p) < 6;
        } else if(otherPlat == p){
            return true;
        } else {
            System.out.println("Caso misterioso que nunca deve ocorrer!");
            return false;
        }
    }
}
