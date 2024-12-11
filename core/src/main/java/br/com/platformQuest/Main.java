package br.com.platformQuest;

import br.com.platformQuest.Entities.BasePlatform;
import br.com.platformQuest.Entities.EntitiesManager;
import br.com.platformQuest.Entities.EyeBullet;
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
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Viewport view;
    private OrthographicCamera camera;
    private Texture background;
    private World world;
    private Player player;

    @Override
    public void create() {
        initGame();
    }

    private void initGame() {
        world = new World(new Vector2(0, -12), true);
        world.setContactListener(new CollisionsHandler());
        background = new Texture("background.png");
        camera = new OrthographicCamera(720, 1280);
        view = new FitViewport(720, 1280, camera);
        view.apply();
        camera.position.set(720 / 2f, 1280 / 2f, 0);
        batch = new SpriteBatch();
        EntitiesManager.getInstance().setWrld(world);
        createStartEntities();
    }

    private void createStartEntities() {
        player = new Player(15, 10, world, new Texture("player/players.png"));
        player.createFixture(0.5f, 1);

        BasePlatform basePlatform = new BasePlatform(Constants.WIDTH / Constants.PPM / 2, 0, world, new Texture("bigPlatform.png"));
        basePlatform.createFixture(Constants.WIDTH / 2 / Constants.PPM, 0.5f);

        EntitiesManager eManager = EntitiesManager.getInstance();
        eManager.addEntity(player);
        eManager.addEntity(basePlatform);
        eManager.setPlayer(player);

        EyeBullet eye = new EyeBullet(player.getBody().getPosition().x + 10, player.getBody().getPosition().x + 18, world, new Texture("deathEye.png"));
        eye.createFixture(1, 1);
        eye.shot();

        eManager.addEntity(eye);
        eManager.createPlatforms(5, 11);
    }

    @Override
    public void render() {
        input();
        if (player.isAlive()) {
            System.out.println("atualizando e desenhando");
            update();
            draw();
        } else {
            drawGameOverScreen();
        }
    }

    public void draw() {
        ScreenUtils.clear(Color.BLACK);
        camera.update();
        view.apply();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0, 0);
        EntitiesManager.getInstance().drawEntities(batch);
        batch.end();

        world.step(1 / 60f, 6, 2);
        EntitiesManager.getInstance().executeScheduledActions();
    }

    @Override
    public void resize(int width, int height) {
        view.update(width, height); // Atualiza o viewport para o novo tamanho da janela
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight();
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.jump();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            System.out.println("R pressionado");
            if (!player.isAlive()) {
                restartGame();
            }
        }
    }

    private void update() {
        EntitiesManager.getInstance().updateEntities();

        float playerY = player.getBody().getPosition().y * Constants.PPM;
        if (playerY > camera.position.y) { // Mover a câmera somente se o jogador estiver subindo
            camera.position.y = playerY;
        }
    }

    private void drawGameOverScreen() {
        System.out.println("Desenhando tela de fim");
        ScreenUtils.clear(Color.BLACK);
        batch.begin();
        Texture tex = new Texture("endGame.png");
        batch.draw(tex, camera.position.x - Constants.WIDTH / 2, camera.position.y - Constants.HEIGHT / 2);
        batch.end();

    }

    private void restartGame() {
        System.out.println("Reiniciando o Jogo!");
        EntitiesManager.getInstance().destroyAll();
        camera.position.set(720 / 2f, 1280 / 2f, 0);
        createStartEntities();
    }
}
