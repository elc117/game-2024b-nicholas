package br.com.platformQuest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    
    @Override
    public void create() {
        background = new Texture("background.png");
        camera =  new OrthographicCamera(720, 1280);
        view = new FitViewport(720, 1280, camera);
        view.apply();
        camera.position.set(720/2f, 1280/2f, 0);
        batch = new SpriteBatch();

        player = new Player(10, 10);
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
        player.drawPlayer(batch);
        batch.end();
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
        float speed = 40f;
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.incrementPosition(speed * delta, 0);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.incrementPosition(-speed * delta, 0);
        }
    }
}
