/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.platformQuest.Entities;

import br.com.platformQuest.Helper.Constants;
import br.com.platformQuest.Questions.QuestionCreator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 *
 * @author zorte
 */
public class EntitiesManager {

    private static EntitiesManager instance;
    private final List<Box2DEntity> entities;
    private final Queue<Runnable> performAfterWorldStep;
    private QuestionCreator questCreator;
    private World wrld;
    private Player player;
    private float timeUntilNextShot = 0;
    private float lastHeight = 0;

    private EntitiesManager() {
        timeUntilNextShot = 5f;
        entities = new ArrayList<>();
        performAfterWorldStep = new LinkedList<>();
        this.questCreator = new QuestionCreator();
        this.questCreator.selectRandomQuest();
    }

    public static synchronized EntitiesManager getInstance() {
        if (instance == null) {
            instance = new EntitiesManager();
        }
        return instance;
    }

    public void addEntity(Box2DEntity e) {
        entities.add(e);
    }

    public void removeEntity(Box2DEntity e) {
        entities.remove(e);
    }

    public void scheduleAfterWorldStep(Runnable action) {
        performAfterWorldStep.add(action);
    }

    public void executeScheduledActions() {
        while (!performAfterWorldStep.isEmpty()) {
            Runnable poll = performAfterWorldStep.poll();
            if (poll != null) {
                poll.run();
            }
        }
    }

    public void drawEntities(SpriteBatch batch) {
        entities.stream()
            .filter((t) -> t != null)
            .forEach((t) -> t.draw(batch));
        float QuestionHeight = lastHeight * Constants.PPM + 500;
        QuestionCreator.QUESTION_FONT.draw(batch, questCreator.getActualQuest().getQuestion(), 100, QuestionHeight);
    }

    public void updateEntities() {
        entities.stream()
            .filter((t) -> t instanceof Update)
            .forEach((t) -> ((Update) t).update());

        timeUntilNextShot -= Gdx.graphics.getDeltaTime();
        if (timeUntilNextShot <= 0f) {
            shootNewDino();  // Dispara um novo dinossauro
            resetShootingTimer();  // Reseta o timer
        }
    }

    public World getWrld() {
        return wrld;
    }

    public void setWrld(World wrld) {
        this.wrld = wrld;
    }

    public void createPlatforms(float x1, float x2) {
        scheduleAfterWorldStep(new Runnable() {
            @Override
            public void run() {
                lastHeight += 6f;
                Platform p1 = new Platform(x1, lastHeight, EntitiesManager.this.wrld, new Texture("platform.png"));
                Platform p2 = new Platform(x2, lastHeight, EntitiesManager.this.wrld, new Texture("platform.png"));
                p1.setResizable(true);
                p2.setResizable(true);

                p1.createFixture(2, 1);
                p2.createFixture(2, 1);

                Random r = new Random();
                int rInt = r.nextInt(100);
                if (rInt % 2 == 0) {
                    p1.setAns(questCreator.getActualQuest().getCorrectAnswer());
                    p2.setAns(questCreator.getActualQuest().getWrongAnswer());
                } else {
                    p2.setAns(questCreator.getActualQuest().getCorrectAnswer());
                    p1.setAns(questCreator.getActualQuest().getWrongAnswer());
                }
                entities.add(p2);
                entities.add(p1);
            }
        });
    }

    public void playerHitCorrectAnswer() {
        //deleta plataforma errada
        entities.stream()
            .filter((t) -> t instanceof Platform)
            .filter((t) -> questCreator.getActualQuest().getWrongAnswer().equals(((Platform) t).getAns()))
            .forEach((t) -> ((Platform) t).destroyObject());

        //seleciona nova pergunta
        questCreator.selectRandomQuest();

        //gera novas plataformas
        float[] newPositions = Platform.generateNewPositions();
        createPlatforms(newPositions[0], newPositions[1]);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void destroyAll() {
        entities.stream()
            .forEach((t) -> t.destroyObject());
        player = null;
        questCreator = new QuestionCreator();
        questCreator.selectRandomQuest();
        lastHeight = 0;
        resetShootingTimer();
    }

    public QuestionCreator getQuestCreator() {
        return questCreator;
    }

    private void shootNewDino() {
        Random r = new Random();
        int x = r.nextInt(22);
        int y = r.nextInt(22);
        Dino bullet = new Dino(player.getBody().getPosition().x + x, player.getBody().getPosition().y + y, wrld, new Texture("dino/velociraptor1.png"));
        bullet.createFixture(1, 1);
        bullet.shot();
        entities.add(bullet);
        System.out.println("Lancando dino! " + bullet.body.getPosition().x + "," + bullet.body.getPosition().y);
    }

    private void resetShootingTimer(){
        Random r = new Random();
        timeUntilNextShot = r.nextInt(5);
    }
}
