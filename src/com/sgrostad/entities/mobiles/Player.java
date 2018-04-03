package com.sgrostad.entities.mobiles;

import com.sgrostad.Handler;
import com.sgrostad.entities.Entity;
import com.sgrostad.input.player.*;
import com.sgrostad.gfx.Animation;
import com.sgrostad.gfx.Assets;
import com.sgrostad.inventory.Inventory;
import com.sgrostad.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends MovableEntity {

    private static final int MILLI_SEC_PER_PLAYER_FRAME = 80;
    private static final int DEFAULT_JUMP_SPEED = -400;
    private static final int DEFAULT_STILL_JUMP_HORIZONTAL_SPEED = 150;
    private static final float MOVABLE_PLAYER_TO_WALL_SPEED = DEFAULT_MOVABLE_ENTITY_SPEED / 10;

    // Animations
    private Animation animationLeft, animationRight;
    // Inventory
    private Inventory inventory;
    // Actions
    private List<PlayerActions> playerActions = new ArrayList<>();
    private Map<String, Direction> pressedKeys = new HashMap<>();


    public Player(Handler handler, float x, float y) {
        super(handler, x, y, MovableEntity.DEFAULT_MOVABLE_ENTITY_WIDTH, MovableEntity.DEFAULT_MOVABLE_ENTITY_HEIGHT);
        bounds.x = 16;
        bounds.y = 8;
        bounds.width = 32;
        bounds.height = 54;
        animationLeft = new Animation(MILLI_SEC_PER_PLAYER_FRAME, Assets.playerLeft);
        animationRight = new Animation(MILLI_SEC_PER_PLAYER_FRAME, Assets.playerRight);
        playerActions.add(new PlayerHorizontalAction(handler));
        playerActions.add(new PlayerJumpAction(handler, new PlayerTakeOffTimer(this)));
        playerActions.add(new PlayerMeleeAction(handler));
        inventory = new Inventory(handler);
    }

    @Override
    public void tick() {
        //Animation
        animationLeft.tick();
        animationRight.tick();
        //Movement
        getInput();
        move();
        handler.getGameCamera().centerOnEntity(this);
        for (PlayerActions action : playerActions){
            action.tick();
        }
        inventory.tick();
    }


    @Override
    public void render(Graphics g) {
        /*g.setColor(Color.red);
        g.fillRect((int)(x + bounds.x - handler.getGameCamera().getxOffset()),
                (int)(y + bounds.y - handler.getGameCamera().getyOffset()), bounds.width, bounds.height);
        */
        g.drawImage(getCurrentAnimationFrame(),(int)(x - handler.getGameCamera().getxOffset()),
                (int)(y - handler.getGameCamera().getyOffset()), width, height,null);
        inventory.render(g);
    }

    @Override
    public void die() {
        System.out.println("You died!");
    }

    @Override
    public void move(){
        ySpeed += DEFAULT_GRAVITATION * SECONDS_PAST_PER_MOVE;
        float yMove = ySpeed * SECONDS_PAST_PER_MOVE;
        moveY(yMove);
        float xMove = accelerateHorizontally(yMove);
        moveX(xMove);
    }

    private BufferedImage getCurrentAnimationFrame(){
        if (xDir.goingLeft() && !airborne){
            return animationLeft.getCurrentFrame();
        }
        else if (xDir.goingRight() && !airborne){
            return animationRight.getCurrentFrame();
        }
        else {
            if (facingRight){
                return animationRight.getFirstFrame();
            }
            return animationLeft.getFirstFrame();
        }
    }

    public void meleeAttack(){
        Rectangle cb = getCollisionBounds(0,0);
        Rectangle aRange = new Rectangle();
        int aRangeSize = 20;
        aRange.width = aRangeSize;
        aRange.height = aRangeSize;
        if (facingRight){
            aRange.x = cb.x + cb.width;
            aRange.y = cb.y + cb.height / 2 - aRangeSize / 2;
        } else {
            aRange.x = cb.x - aRangeSize;
            aRange.y = cb.y + cb.height / 2 - aRangeSize / 2;
        }
        for (Entity e : handler.getWorld().getEntityManager().getEntities()){
            if (e.equals(this)){
                continue;
            }
            if (e.getCollisionBounds(0,0).intersects(aRange)){
                e.hurt(1);
            }
        }
    }

    public void fireBullet(){ //TODO adjust bullet start values
        int bulletX = (int)x;
        int bulletY = (int)y + height/2;
        Direction bulletDir;
        if (facingRight){
            bulletDir = Direction.RIGHT;
            bulletX += width + 2;
        }
        else {
            bulletDir = Direction.LEFT;
            bulletX -= Bullet.STANDARD_BULLET_WIDTH - 2;
        }
        handler.getWorld().getEntityManager().addEntity(new Bullet(handler, bulletX, bulletY, bulletDir));
    }

    private void getInput(){
        int tempXDir = 0;
        for (Map.Entry<String, Direction> entry : pressedKeys.entrySet()){
            if (entry.getKey().equals("RIGHT")){
                tempXDir += 1;
            }
            else if (entry.getKey().equals("LEFT")){
                tempXDir -= 1;
            }
        }
        if (tempXDir > 0){
            setxDir(Direction.RIGHT);
        }
        else if (tempXDir < 0){
            setxDir(Direction.LEFT);
        }
        else {
            setxDir(Direction.STILL);
        }

    }

    public void makeJump(float jumpForce){
        if (jumpForce > 0) {
            ySpeed = DEFAULT_JUMP_SPEED * jumpForce;
            if (!xDir.standingStill() && Math.abs(xSpeed) < DEFAULT_STILL_JUMP_HORIZONTAL_SPEED) {
                if (xDir.goingRight()) {
                    xSpeed = DEFAULT_STILL_JUMP_HORIZONTAL_SPEED;
                } else {
                    xSpeed = -DEFAULT_STILL_JUMP_HORIZONTAL_SPEED;
                }
            }
        }
    }

    public void moveX(float xMove){
        boolean entityCollision = checkEntityCollision(xMove, 0f);
        if (xMove > 0){ //Moving right
            int tileX = (int) (x + xMove + bounds.x + bounds.width) / Tile.TILE_WIDTH;
            if (!collisionWithTile(tileX, (int)(y + bounds.y) / Tile.TILE_HEIGHT) &&
                    !collisionWithTile(tileX, (int)(y + bounds.y + bounds.height) / Tile.TILE_HEIGHT) &&
                    !entityCollision){
                x += xMove;
            }
            else if (entityCollision){
                xSpeed = MOVABLE_PLAYER_TO_WALL_SPEED;
            }
            else { //crashed in tile
                x = tileX * Tile.TILE_WIDTH - bounds.x - bounds.width - 1;
                xSpeed = MOVABLE_PLAYER_TO_WALL_SPEED;
            }
        }
        else if (xMove < 0) { //Moving left
            int tileX = (int) (x + xMove + bounds.x) / Tile.TILE_WIDTH;
            if (!collisionWithTile(tileX, (int)(y + bounds.y) / Tile.TILE_HEIGHT) &&
                    !collisionWithTile(tileX, (int)(y + bounds.y + bounds.height) / Tile.TILE_HEIGHT) &&
                    !entityCollision){
                x += xMove;
            }
            else if (entityCollision){
                xSpeed = -MOVABLE_PLAYER_TO_WALL_SPEED;
            }
            else { //crashed in tile
                x = tileX * Tile.TILE_WIDTH + Tile.TILE_WIDTH - bounds.x;
                xSpeed = -MOVABLE_PLAYER_TO_WALL_SPEED;
            }
        }
    }

    public void moveY(float yMove){
        boolean entityCollision = checkEntityCollision(0, yMove);
        if (yMove > 0){ //Moving down
            int tileY = (int) (y + yMove + bounds.y + bounds.height) / Tile.TILE_HEIGHT;
            if (!collisionWithTile((int)(x + bounds.x) / Tile.TILE_WIDTH,tileY) &&
                    !collisionWithTile((int)(x + bounds.x + bounds.width) / Tile.TILE_WIDTH, tileY) &&
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
            int tileY = (int)(y + yMove + bounds.y) / Tile.TILE_HEIGHT;
            if (!collisionWithTile((int)(x + bounds.x) / Tile.TILE_WIDTH,tileY) &&
                    !collisionWithTile((int)(x + bounds.x + bounds.width) / Tile.TILE_WIDTH, tileY) &&
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

    protected float accelerateHorizontally(float yMove){
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
            performHorizontalAcceleration();
        }
        setFacingDirection();
        return xSpeed * SECONDS_PAST_PER_MOVE;
    }

    private void performAirborneDeceleration(){
        if (xSpeed > 0) {
            xSpeed -= DEFAULT_MOVABLE_ENTITY_DECELERATION * SECONDS_PAST_PER_MOVE;
            if (xSpeed < 0){
                xSpeed = 0;
            }
        } else if (xSpeed < 0){
            xSpeed += DEFAULT_MOVABLE_ENTITY_DECELERATION * SECONDS_PAST_PER_MOVE;
            if (xSpeed > 0){
                xSpeed = 0;
            }
        }
    }

    //GETTERS SETTERS


    public void removePressedKey(String key) {
        pressedKeys.remove(key);
    }

    public void addPressedKey(String key, Direction direction) {
        pressedKeys.put(key, direction);
    }

    public Inventory getInventory() {
        return inventory;
    }

}
