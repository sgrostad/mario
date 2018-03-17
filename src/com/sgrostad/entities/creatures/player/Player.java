package com.sgrostad.entities.creatures.player;

import com.sgrostad.Handler;
import com.sgrostad.entities.creatures.Creature;
import com.sgrostad.input.PlayerActionsHandler;
import com.sgrostad.gfx.Animation;
import com.sgrostad.gfx.Assets;
import com.sgrostad.inventory.Inventory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Player extends Creature {

    private static final int MILLI_SEC_PER_PLAYER_FRAME = 80;
    private static final int DEFAULT_JUMP_SPEED = -400;
    private static final int DEFAULT_STILL_JUMP_HORIZONTAL_SPEED = 150;

    // Animations
    private Animation animationLeft, animationRight;
    // Attack timer
    private long lastAttackTimer, attackCoolDown = 800, attackTimer = attackCoolDown;
    // Jump timer
    private PlayerTakeOffTimer playerTakeOffTimer;
    // Inventory
    private Inventory inventory;
    // Actions
    private PlayerActionsHandler playerActionsHandler; // TODO do class static?
    private Map<String, PlayerActionType> pressedKeys = new HashMap<>();


    public Player(Handler handler, float x, float y) {
        super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);
        bounds.x = 16;
        bounds.y = 8;
        bounds.width = 32;
        bounds.height = 54;
        animationLeft = new Animation(MILLI_SEC_PER_PLAYER_FRAME, Assets.playerLeft);
        animationRight = new Animation(MILLI_SEC_PER_PLAYER_FRAME, Assets.playerRight);
        playerActionsHandler = new PlayerActionsHandler(handler);
        inventory = new Inventory(handler);
        playerTakeOffTimer = new PlayerTakeOffTimer(this);
    }

    @Override
    public void tick() {
        //Animation
        animationLeft.tick();
        animationRight.tick();
        //Movement
        getInput();
        move();
        playerTakeOffTimer.tick();
        handler.getGameCamera().centerOnEntity(this);
        // Attacks
        checkAttacks();
        inventory.tick();
    }


    private void checkAttacks(){
        /*
        attackTimer += System.currentTimeMillis() - lastAttackTimer;
        lastAttackTimer = System.currentTimeMillis();
        if (attackTimer < attackCoolDown){
            return;
        }
        Rectangle cb = getCollisionBounds(0,0);
        Rectangle aRange = new Rectangle();
        int aRangeSize = 20;
        aRange.width = aRangeSize;
        aRange.height = aRangeSize;
        if (handler.getKeyManager().aUp){
            aRange.x = cb.x + cb.width / 2 - aRangeSize / 2;
            aRange.y = cb.y - aRangeSize;
        }else if (handler.getKeyManager().aDown){
            aRange.x = cb.x + cb.width / 2 - aRangeSize / 2;
            aRange.y = cb.y + cb.height;
        }else if (handler.getKeyManager().aLeft){
            aRange.x = cb.x - aRangeSize;
            aRange.y = cb.y + cb.height / 2 - aRangeSize / 2;
        }else if (handler.getKeyManager().aRight){
            aRange.x = cb.x + cb.width;
            aRange.y = cb.y + cb.height / 2 - aRangeSize / 2;
        }else {
            return;
        }
        for (Entity e : handler.getWorld().getEntityManager().getEntities()){
            if (e.equals(this)){
                continue;
            }
            if (e.getCollisionBounds(0,0).intersects(aRange)){
                e.hurt(1);
            }
        }
        attackTimer = 0;*/
    }

    private void getInput(){
        int tempXDir = 0;
        for (Map.Entry<String, PlayerActionType> entry : pressedKeys.entrySet()){
            if (entry.getKey().equals("RIGHT") && !airborne){
                tempXDir += 1;
            }
            else if (entry.getKey().equals("LEFT") && !airborne){
                tempXDir -= 1;
            }
        }
        if (tempXDir > 0){
            setxDir(PlayerActionType.RIGHT);
        }
        else if (tempXDir < 0){
            setxDir(PlayerActionType.LEFT);
        }
        else {
            setxDir(PlayerActionType.STILL);
        }

    }

    private void makeJump(){
        float jumpForce = playerTakeOffTimer.getJumpForce();
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

    private void checkReleaseActions(String key){
        switch (PlayerActionType.keyToPlayerActionType(key)){
            case JUMP:
                makeJump();
        }
    }

    private void checkPressedActions(String key){
        switch (PlayerActionType.keyToPlayerActionType(key)){
            case JUMP:
                playerTakeOffTimer.prepareTakeOff();
        }
    }

    //GETTERS SETTERS


    public void removePressedKey(String key) {
        checkReleaseActions(key);
        pressedKeys.remove(key);
    }

    public void addPressedKey(String key, PlayerActionType playerActionType) {
        checkPressedActions(key);
        pressedKeys.put(key, playerActionType);
    }

    public Inventory getInventory() {
        return inventory;
    }

}
