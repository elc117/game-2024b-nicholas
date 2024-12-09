package br.com.platformQuest;

import br.com.platformQuest.Entities.Platform;
import br.com.platformQuest.Entities.Player;
import br.com.platformQuest.Helper.CollisionsHandler;
import br.com.platformQuest.Helper.Constants;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.LinkedList;
import java.util.Queue;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private Viewport view;
    private OrthographicCamera camera;
    private Texture background;
    private Platform basePlatform;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Platform platformx;
    private Queue<Runnable> performAfterWordStep;
    @Override
    public void create() {
        performAfterWordStep = new LinkedList<Runnable>();
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(new CollisionsHandler());
        background = new Texture("background.png");
        camera =  new OrthographicCamera(720, 1280);
        view = new FitViewport(720, 1280, camera);
        view.apply();
        camera.position.set(720/2f, 1280/2f, 0);
        batch = new SpriteBatch();
        basePlatform = new Platform(Constants.WIDTH / Constants.PPM / 2, 0, world, new Texture("platform.png"));
        basePlatform.createBody();
        basePlatform.createFixture(Constants.WIDTH / 2 / Constants.PPM, 1);
        basePlatform.setIsDroppable(false);
        platformx = new Platform(10, 7, world, new Texture("platform.png"));
        platformx.createBody();
        platformx.setAfterWordStep(performAfterWordStep);
        platformx.createFixture(2, 1);
        platformx.setIsDroppable(true);
        player = new Player(15, 10, world, new Texture("player/players.png"));
        player.createFixture(0.5f, 1);
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void render() {
        input();
        draw();
    }

    public void draw(){
        ScreenUtils.clear(Color.BLACK);
        camera.update();

        view.apply();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0, 0);
        basePlatform.draw(batch);
        platformx.draw(batch);
        player.draw(batch);
        batch.end();
        world.step(1/60f, 6, 2);
        if(performAfterWordStep.peek()!=null) performAfterWordStep.poll().run();
        debugRenderer.render(world, camera.combined.scl(32));
    }

    @Override
    public void resize(int width, int height) {
        view.update(width, height); // Atualiza o viewport para o novo tamanho da janela
    }

    
    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
    }

    public void input(){
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight();
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            player.jump();
        }
    }
}
