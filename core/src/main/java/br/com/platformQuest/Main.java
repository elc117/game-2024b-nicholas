package br.com.platformQuest;

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

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private Viewport view;
    private OrthographicCamera camera;
    private Texture background;
    private Texture platform;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Platform platformx;
    @Override
    public void create() {
        platform = new Texture("platform.png");
        world = new World(new Vector2(0, -10), true);
        background = new Texture("background.png");
        camera =  new OrthographicCamera(720, 1280);
        view = new FitViewport(720, 1280, camera);
        view.apply();
        camera.position.set(720/2f, 1280/2f, 0);
        batch = new SpriteBatch();
        platformx = new Platform(world);
        player = new Player(15, 40, world);
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
        batch.draw(platform, 30, 30);
        player.drawPlayer(batch);
        batch.end();
        world.step(1/60f, 6, 2);
        debugRenderer.render(world, camera.combined);
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
        } else if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            player.jump();
        }
    }
}
