package com.sgrostad.gfx;

import com.sgrostad.Game;
import com.sgrostad.Handler;
import com.sgrostad.entities.Entity;
import com.sgrostad.tiles.Tile;

public class GameCamera {

    private Handler handler;
    private float xOffset, yOffset;

    public GameCamera(Handler handler, float xOffset, float yOffset) {
        this.handler = handler;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    private void avoidWhiteSpace(){
        if (xOffset < 0){
            xOffset = 0;
        }
        else if (xOffset > handler.getWorld().getWidth() * Tile.TILE_WIDTH - handler.getWidth()){
            xOffset = handler.getWorld().getWidth() * Tile.TILE_WIDTH - handler.getWidth();
        }
        if (yOffset < 0){
            yOffset = 0;
        }
        else if (yOffset > handler.getWorld().getHeight() * Tile.TILE_HEIGHT - handler.getHeight()){
            yOffset = handler.getWorld().getHeight() * Tile.TILE_HEIGHT - handler.getHeight();
        }
    }

    public void move(float xAmount, float yAmount){
        xOffset += xAmount;
        yOffset += yAmount;
        avoidWhiteSpace();
    }

    public void centerOnEntity(Entity e){
        xOffset = (int)e.getX() - handler.getWidth()/2 + e.getWidth()/2;
        yOffset = (int)e.getY() - handler.getHeight()/2 + e.getHeight()/2;
        avoidWhiteSpace();
    }

    public float getxOffset() {
        return xOffset;
    }

    public void setxOffset(float xOffset) {
        this.xOffset = xOffset;
    }

    public float getyOffset() {
        return yOffset;
    }

    public void setyOffset(float yOffset) {
        this.yOffset = yOffset;
    }
}
