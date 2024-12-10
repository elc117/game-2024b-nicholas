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
    }

    @Override
    public void collisionWithPlayer() {
        if(!timerOn && isPlayerAbove()){
            timeStart = TimeUtils.millis() / 1000;
            timerOn = true;
        }
    }
}
