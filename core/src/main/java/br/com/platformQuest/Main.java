package br.com.platformQuest;

import br.com.platformQuest.Entities.BasePlatform;
import br.com.platformQuest.Entities.EntitiesManager;
import br.com.platformQuest.Entities.Dino;
import br.com.platformQuest.Entities.Player;
import br.com.platformQuest.Helper.CollisionsHandler;
import br.com.platformQuest.Helper.Constants;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
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
    public static status STATUS;
    private Texture endGame;
    private Texture win;

    public enum status{
        WIN,
        DEFEAT,
        RUNNING
    }

    @Override
    public void create() {
        win = new Texture("winGame.png");
        endGame = new Texture("endGame.png");
        Main.STATUS = status.RUNNING;
        Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setVolume(0.3f);
        music.setLooping(true);
        music.play();
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
        player.createFixture(1,1.5625f);

        BasePlatform basePlatform = new BasePlatform(Constants.WIDTH / Constants.PPM / 2, 2, world, new Texture("bigPlatform.png"));
        basePlatform.createFixture(Constants.WIDTH / 2 / Constants.PPM, 0.5f);

        EntitiesManager eManager = EntitiesManager.getInstance();
        eManager.addEntity(player);
        eManager.addEntity(basePlatform);
        eManager.setPlayer(player);

        Dino dino = new Dino(player.getBody().getPosition().x + 10, player.getBody().getPosition().y + 18, world, new Texture("dino/velociraptor1.png"));
        dino.createFixture(1, 1);
        dino.shot();

        eManager.addEntity(dino);
        eManager.createPlatforms(5, 11);
    }

    @Override
    public void render() {
        input();
        if (STATUS.equals(status.RUNNING)) {
            update();
            draw();
        } else {
            if(STATUS.equals(status.DEFEAT)){
                drawGameOverScreen();
            } else {
                drawWinningScreen();
            }

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
        background.dispose();
        endGame.dispose();
        win.dispose();
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
            if (STATUS.equals(status.DEFEAT) || STATUS.equals(status.WIN)) {
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
        ScreenUtils.clear(Color.BLACK);
        batch.begin();
        batch.draw(endGame, camera.position.x - Constants.WIDTH / 2, camera.position.y - Constants.HEIGHT / 2);
        batch.end();

    }

    private void restartGame() {
        System.out.println("Reiniciando o Jogo!");
        EntitiesManager.getInstance().destroyAll();
        camera.position.set(720 / 2f, 1280 / 2f, 0);
        createStartEntities();
        STATUS = status.RUNNING;
    }

        private void drawWinningScreen() {
        ScreenUtils.clear(Color.BLACK);
        batch.begin();
        batch.draw(win, camera.position.x - Constants.WIDTH / 2, camera.position.y - Constants.HEIGHT / 2);
        batch.end();
    }
}
