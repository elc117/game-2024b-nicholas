/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.platformQuest;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {
    float x,y;
    Texture texture;
    public Player(float x, float y){
        this.x = x;
        this.y = y;
        texture = new Texture("player/player.png");
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public void drawPlayer(SpriteBatch batch){
        batch.draw(texture, x, y, 64,64);
    }

    public void changePosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void dispose() {
        texture.dispose();
    }

    public void incrementPosition(float xAmount, float yAmount){
        this.x += xAmount;
        this.y += yAmount;
    }
}
