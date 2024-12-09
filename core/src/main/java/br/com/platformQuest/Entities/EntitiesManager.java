/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.platformQuest.Entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author zorte
 */
public class EntitiesManager {
    private static EntitiesManager instance;
    private final List<Box2DEntity> entities;
    private final Queue<Runnable> performAfterWorldStep;


    private EntitiesManager() {
        entities = new ArrayList<>();
        performAfterWorldStep = new LinkedList<>();
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
            if(poll != null){
                poll.run();
            }
        }
    }

    public void drawEntities(SpriteBatch batch){
        entities.stream()
            .filter((t) -> t != null)
            .forEach((t) -> t.draw(batch));
    }

    public void updateEntities() {
        entities.stream()
            .filter((t) -> t instanceof Update)
            .forEach((t) -> ((Update)t).update());
    }
}
