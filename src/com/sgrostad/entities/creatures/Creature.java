package com.sgrostad.entities.creatures;

import com.sgrostad.Game;
import com.sgrostad.Handler;
import com.sgrostad.entities.Entity;
import com.sgrostad.tiles.Tile;
public abstract class Creature extends Entity{
    public static final float SECONDS_PAST_PER_MOVE = 1.0f / Game.FPS;
    public static final float DEFAULT_CREATURE_SPEED = 5.0f * Game.FPS;
    public static final float CREATURE_NEXT_TO_WALL_SPEED = DEFAULT_CREATURE_SPEED / 10;
    public static final float DEFAULT_GRAVITATION = 9.81f * Game.FPS;
    public static final float DEFAULT_CREATURE_ACCELERATION = DEFAULT_CREATURE_SPEED;
    public static final float DEFAULT_CREATURE_DECELERATION = DEFAULT_CREATURE_ACCELERATION / 5;
    public static final int DEFAULT_CREATURE_WIDTH = 64, DEFAULT_CREATURE_HEIGHT = 64;

    protected boolean airborne = true;
    protected float horizontalMaxSpeed;
    protected Direction xDir;
    protected boolean facingRight;
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
        ySpeed += DEFAULT_GRAVITATION * SECONDS_PAST_PER_MOVE;
        float yMove = ySpeed * SECONDS_PAST_PER_MOVE;
        moveY(yMove);
        float xMove = accelerateHorizontally(yMove);
        moveX(xMove);
    }

    public void moveX(float xMove){
        boolean entityCollision = checkEntityCollision(xMove, 0f);
        if (xMove > 0){ //Moving right
            int tileX = (int) Math.floor((x + xMove + bounds.x + bounds.width) / Tile.TILE_WIDTH);
            if (!collisionWithTile(tileX, (int)Math.floor((y + bounds.y) / Tile.TILE_HEIGHT)) &&
                !collisionWithTile(tileX, (int)Math.floor((y + bounds.y + bounds.height) / Tile.TILE_HEIGHT)) &&
                    !entityCollision){
                x += xMove;
            }
            else if (entityCollision){
                xSpeed = CREATURE_NEXT_TO_WALL_SPEED;
            }
            else { //crashed in tile
                x = tileX * Tile.TILE_WIDTH - bounds.x - bounds.width - 1;
                xSpeed = CREATURE_NEXT_TO_WALL_SPEED;
            }
        }
        else if (xMove < 0) { //Moving left
            int tileX = (int) Math.floor((x + xMove + bounds.x) / Tile.TILE_WIDTH);
            if (!collisionWithTile(tileX, (int)Math.floor((y + bounds.y) / Tile.TILE_HEIGHT)) &&
                    !collisionWithTile(tileX, (int)Math.floor((y + bounds.y + bounds.height) / Tile.TILE_HEIGHT)) &&
                    !entityCollision){
                x += xMove;
            }
            else if (entityCollision){
                xSpeed = -CREATURE_NEXT_TO_WALL_SPEED;
            }
            else { //crashed in tile
                x = tileX * Tile.TILE_WIDTH + Tile.TILE_WIDTH - bounds.x;
                xSpeed = -CREATURE_NEXT_TO_WALL_SPEED;
            }
        }
    }

    public void moveY(float yMove){
        boolean entityCollision = checkEntityCollision(0, yMove);
        if (yMove > 0){ //Moving down
            int tileY = (int) Math.floor((y + yMove + bounds.y + bounds.height) / Tile.TILE_HEIGHT);
            if (!collisionWithTile((int) Math.floor((x + bounds.x) / Tile.TILE_WIDTH),tileY) &&
                    !collisionWithTile((int) Math.floor((x + bounds.x + bounds.width) / Tile.TILE_WIDTH), tileY) &&
                    !entityCollision){
                y += yMove;
            }
            else if (entityCollision){
                ySpeed = 0;
                airborne = false;
            }
            else { //Landing
                y = tileY * Tile.TILE_HEIGHT - bounds.y - bounds.height - 1;
                ySpeed = 0;
                airborne = false;
            }
        }
        else if (yMove < 0){ //Moving up
            int tileY = (int) Math.floor((y + yMove + bounds.y) / Tile.TILE_HEIGHT);
            if (!collisionWithTile((int) Math.floor((x + bounds.x) / Tile.TILE_WIDTH),tileY) &&
                    !collisionWithTile((int) Math.floor((x + bounds.x + bounds.width) / Tile.TILE_WIDTH), tileY) &&
                    !entityCollision){
                y += yMove;
                airborne = true;
            }
            else if (entityCollision){
                ySpeed = 0;
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

    private float accelerateHorizontally(float yMove){
        setFacingDirection();
        if (xDir.standingStill() && !airborne){
            xSpeed = 0;
            return 0;
        }
        if (airborne && !checkEntityCollision(0f, Math.abs(yMove)) ) { //decelerate
            performAirborneDeceleration();
        }
        else {//accelerate
            if ((xSpeed > 0 && xDir.goingLeft()) || (xSpeed < 0 && xDir.goingRight()) ||
                    (checkEntityCollision(0f, yMove) && xDir.standingStill())) {
                xSpeed = 0;
            }
            performAcceleration();
        }
        setFacingDirection();
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


    public boolean isAirborne() {
        return airborne;
    }

    private void setFacingDirection(){
        if (xSpeed > 0){
            facingRight = true;
        }else if (xSpeed < 0) {
            facingRight = false;
        }
    }

    public void setxDir(Direction xDir) {
        this.xDir = xDir;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

}
