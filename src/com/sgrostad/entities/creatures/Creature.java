package com.sgrostad.entities.creatures;

import com.sgrostad.Game;
import com.sgrostad.Handler;
import com.sgrostad.entities.Entity;
import com.sgrostad.tiles.Tile;
public abstract class Creature extends Entity{
    public static final float SECONDS_PAST_PER_MOVE = 1.0f / Game.FPS;
    public static final float DEFAULT_CREATURE_SPEED = 5.0f * Game.FPS;
    public static final float CREATURE_CLOSE_TO_TILE_SPEED = DEFAULT_CREATURE_SPEED / 10;
    public static final float DEFAULT_GRAVITATION = 9.81f * Game.FPS;
    public static final float DEFAULT_CREATURE_ACCELERATION = DEFAULT_CREATURE_SPEED;
    public static final float DEFAULT_CREATURE_DECELERATION = DEFAULT_CREATURE_ACCELERATION / 5;
    public static final int DEFAULT_CREATURE_WIDTH = 64, DEFAULT_CREATURE_HEIGHT = 64;

    protected boolean lastDirectionRight = true;
    protected boolean airborne = true;
    protected float horizontalMaxSpeed;
    protected Direction xDir;
    protected float xSpeed;
    protected float ySpeed;

    public Creature(Handler handler, float x, float y, int width, int height) {
        super(handler, x, y, width, height);
        horizontalMaxSpeed = DEFAULT_CREATURE_SPEED;
        xDir = Direction.STILL;
        xSpeed = 0;
        ySpeed = 0;
    }

    public void move(){
        float xMove = accelerateHorizontally();
        if (!checkEntityCollision(xMove,0f)) {
            moveX(xMove);
        }
        ySpeed += DEFAULT_GRAVITATION * SECONDS_PAST_PER_MOVE;
        float yMove = ySpeed * SECONDS_PAST_PER_MOVE;
        if (!checkEntityCollision(0, yMove)) {
            moveY(yMove);
        }
    }

    public void moveX(float xMove){
        if (xMove > 0){ //Moving right
            int tileX = (int) Math.floor((x + xMove + bounds.x + bounds.width) / Tile.TILE_WIDTH);
            if (!collisionWithTile(tileX, (int)Math.floor((y + bounds.y) / Tile.TILE_HEIGHT)) &&
                !collisionWithTile(tileX, (int)Math.floor((y + bounds.y + bounds.height) / Tile.TILE_HEIGHT))){
                x += xMove;
            }
            else { //crashed in tile
                x = tileX * Tile.TILE_WIDTH - bounds.x - bounds.width - 1;
                xSpeed = CREATURE_CLOSE_TO_TILE_SPEED;
            }
        }
        else if (xMove < 0) { //Moving left
            int tileX = (int) Math.floor((x + xMove + bounds.x) / Tile.TILE_WIDTH);
            if (!collisionWithTile(tileX, (int)Math.floor((y + bounds.y) / Tile.TILE_HEIGHT)) &&
                    !collisionWithTile(tileX, (int)Math.floor((y + bounds.y + bounds.height) / Tile.TILE_HEIGHT))){
                x += xMove;
            }
            else { //crashed in tile
                x = tileX * Tile.TILE_WIDTH + Tile.TILE_WIDTH - bounds.x;
                xSpeed = -CREATURE_CLOSE_TO_TILE_SPEED;
            }
        }
    }

    public void moveY(float yMove){
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

    private float accelerateHorizontally(){
        if (xDir.standingStill() && !airborne){
            xSpeed = 0;
            return 0;
        }
        if (airborne) { //decelerate
            performAirborneDeceleration();
        }
        else {//accelerate
            if ((xSpeed > 0 && xDir.goingLeft()) || (xSpeed < 0 && xDir.goingRight())) {
                xSpeed = 0;
            }
            performAcceleration();
        }
        return xSpeed * SECONDS_PAST_PER_MOVE;
    }

    private void performAcceleration(){
            if (xDir.goingRight()) {
                xSpeed += DEFAULT_CREATURE_ACCELERATION * SECONDS_PAST_PER_MOVE;
                if (xSpeed > DEFAULT_CREATURE_SPEED){
                    xSpeed = DEFAULT_CREATURE_SPEED;
                }
            } else {
                xSpeed -= DEFAULT_CREATURE_ACCELERATION * SECONDS_PAST_PER_MOVE;
                if (xSpeed < -DEFAULT_CREATURE_SPEED){
                    xSpeed = -DEFAULT_CREATURE_SPEED;
                }
            }
    }

    private void performAirborneDeceleration(){
        if (xSpeed > 0) {
            xSpeed -= DEFAULT_CREATURE_DECELERATION * SECONDS_PAST_PER_MOVE;
            if (xSpeed < 0){
                xSpeed = 0;
            }
        } else if (xSpeed < 0){
            xSpeed += DEFAULT_CREATURE_DECELERATION * SECONDS_PAST_PER_MOVE;
            if (xSpeed > 0){
                xSpeed = 0;
            }
        }
    }

    //Getter and setter:


    public void setxDir(Direction xDir) {
        // TODO Check if this is correct solution
        if (xDir.standingStill()){
            lastDirectionRight = this.xDir.goingRight();
        }
        else {
            lastDirectionRight = xDir.goingRight();
        }
        this.xDir = xDir;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
