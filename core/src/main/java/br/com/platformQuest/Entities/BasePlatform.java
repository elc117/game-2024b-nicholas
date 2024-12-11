/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.platformQuest.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;

/**
 *
 * @author zorte
 */
public class BasePlatform extends Platform{

    public BasePlatform(float x, float y, World world, Texture tex) {
        super(x, y, world, tex);
        timerOn = false;
    }

    @Override
    public void collisionWithPlayer() {
        System.out.println("Colidiu");
        if(!timerOn && isPlayerAbove()){
            System.out.println("iniciou o timer!\n\n");
            timeStart = TimeUtils.millis() / 1000;
            timerOn = true;
        }
    }

    protected boolean isPlayerAbove() {
        float yPlayer = EntitiesManager.getInstance().getPlayer().body.getPosition().y;
        float yPlatform = this.body.getPosition().y;
        return yPlayer - 1.5625f > yPlatform + 0.5f;
    }
}
