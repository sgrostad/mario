package com.sgrostad.entities.mobiles;

import com.sgrostad.Game;
import com.sgrostad.Handler;
import com.sgrostad.entities.Entity;
import com.sgrostad.tiles.Tile;
public abstract class MovableEntity extends Entity{
    public static final float SECONDS_PAST_PER_MOVE = 1.0f / Game.FPS;
    public static final float DEFAULT_MOVABLE_ENTITY_SPEED = 5.0f * Game.FPS;
    public static final float DEFAULT_GRAVITATION = 9.81f * Game.FPS;
    public static final float DEFAULT_MOVABLE_ENTITY_ACCELERATION = DEFAULT_MOVABLE_ENTITY_SPEED;
    public static final float DEFAULT_MOVABLE_ENTITY_DECELERATION = DEFAULT_MOVABLE_ENTITY_ACCELERATION / 5;
    public static final int DEFAULT_MOVABLE_ENTITY_WIDTH = 64, DEFAULT_MOVABLE_ENTITY_HEIGHT = 64;

    protected boolean airborne = true;
    protected float horizontalMaxSpeed;
    protected Direction xDir;
    protected boolean facingRight;
    protected float xSpeed;
    protected float ySpeed;

    public MovableEntity(Handler handler, float x, float y, int width, int height) {
        super(handler, x, y, width, height);
        horizontalMaxSpeed = DEFAULT_MOVABLE_ENTITY_SPEED;
        xDir = Direction.STILL;
        xSpeed = 0;
        ySpeed = 0;
    }

    public abstract void move();

    protected boolean collisionWithTile(int x, int y){
        return handler.getWorld().getTile(x,y).isSolid();
    }

    protected void performHorizontalAcceleration(){
        performHorizontalAcceleration(DEFAULT_MOVABLE_ENTITY_SPEED, DEFAULT_MOVABLE_ENTITY_ACCELERATION);
    }

    protected void performHorizontalAcceleration(float horizontalMaxSpeed, float horizontalAcceleration){
            if (xDir.goingRight()) {
                xSpeed += horizontalAcceleration * SECONDS_PAST_PER_MOVE;
                if (xSpeed > horizontalMaxSpeed){
                    xSpeed = horizontalMaxSpeed;
                }
            } else {
                xSpeed -= horizontalAcceleration * SECONDS_PAST_PER_MOVE;
                if (xSpeed < -horizontalMaxSpeed){
                    xSpeed = -horizontalMaxSpeed;
                }
            }
    }


    //Getter and setter:


    public boolean isAirborne() {
        return airborne;
    }

    protected void setFacingDirection(){
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
