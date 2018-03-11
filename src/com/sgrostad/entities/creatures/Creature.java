package com.sgrostad.entities.creatures;

import com.sgrostad.Game;
import com.sgrostad.Handler;
import com.sgrostad.entities.Entity;
import com.sgrostad.tiles.Tile;

public abstract class Creature extends Entity{
    public static final float SECONDS_PAST_PER_MOVE = 1.0f / Game.FPS;
    public static final float DEFAULT_CREATURE_SPEED = 5.0f, DEFAULT_GRAVITATION = 1000.0f;
    public static final int DEFAULT_CREATURE_WIDTH = 64,
                            DEFAULT_CREATURE_HEIGHT = 64;

    protected boolean lastDirectionRight = true;
    protected boolean airborne = true;
    protected float horizontalSpeed;
    protected float xMove;
    protected float ySpeed;
    protected float yMove;

    public Creature(Handler handler, float x, float y, int width, int height) {
        super(handler, x, y, width, height);
        horizontalSpeed = DEFAULT_CREATURE_SPEED;
        xMove = 0;
        ySpeed = 0;
        yMove = 0;
    }

    public void move(){
        if (!checkEntityCollision(xMove,0f)) {
            moveX();
        }
        ySpeed += DEFAULT_GRAVITATION * SECONDS_PAST_PER_MOVE;
        yMove = ySpeed * SECONDS_PAST_PER_MOVE;
        if (!checkEntityCollision(0, yMove)) {
            moveY();
        }
    }

    public void moveX(){
        if (xMove > 0){ //Moving right
            int tileX = (int) Math.floor((x + xMove + bounds.x + bounds.width) / Tile.TILE_WIDTH);
            if (!collisionWithTile(tileX, (int)Math.floor((y + bounds.y) / Tile.TILE_HEIGHT)) &&
                !collisionWithTile(tileX, (int)Math.floor((y + bounds.y + bounds.height) / Tile.TILE_HEIGHT))){
                x += xMove;
            }
            else {
                x = tileX * Tile.TILE_WIDTH - bounds.x - bounds.width - 1;
            }
        }
        else if (xMove < 0) { //Moving left
            int tileX = (int) Math.floor((x + xMove + bounds.x) / Tile.TILE_WIDTH);
            if (!collisionWithTile(tileX, (int)Math.floor((y + bounds.y) / Tile.TILE_HEIGHT)) &&
                    !collisionWithTile(tileX, (int)Math.floor((y + bounds.y + bounds.height) / Tile.TILE_HEIGHT))){
                x += xMove;
            }
            else {
                x = tileX * Tile.TILE_WIDTH + Tile.TILE_WIDTH - bounds.x;
            }
        }
    }

    public void moveY(){
        if (yMove > 0){ //Moving down
            int tileY = (int) Math.floor((y + yMove + bounds.y + bounds.height) / Tile.TILE_HEIGHT);
            if (!collisionWithTile((int) Math.floor((x + bounds.x) / Tile.TILE_WIDTH),tileY) &&
                    !collisionWithTile((int) Math.floor((x + bounds.x + bounds.width) / Tile.TILE_WIDTH), tileY)){
                y += yMove;
            }
            else {
                y = tileY * Tile.TILE_HEIGHT - bounds.y - bounds.height - 1;
                ySpeed = 0;
                airborne = false;
            }
        }
        else if (yMove < 0){ //Moving up
            int tileY = (int) Math.floor((y + yMove + bounds.y) / Tile.TILE_HEIGHT);
            if (!collisionWithTile((int) Math.floor((x + bounds.x) / Tile.TILE_WIDTH),tileY) &&
                    !collisionWithTile((int) Math.floor((x + bounds.x + bounds.width) / Tile.TILE_WIDTH), tileY)){
                y += yMove;
                airborne = true;
            }
            else {
                y = tileY * Tile.TILE_HEIGHT + Tile.TILE_HEIGHT - bounds.y;
                ySpeed = 0;
            }
        }
    }

    protected boolean collisionWithTile(int x, int y){
        return handler.getWorld().getTile(x,y).isSolid();
    }

    //Getter and setter:


    public float getxMove() {
        return xMove;
    }

    public void setxMove(float xMove) {
        this.xMove = xMove;
    }

    public float getyMove() {
        return yMove;
    }

    public void setyMove(float yMove) {
        this.yMove = yMove;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
