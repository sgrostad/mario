package com.sgrostad.entities.creatures;

import com.sgrostad.Handler;
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
    private static final int DEFAULT_JUMP_SPEED = -500;
    // Animations
    private Animation animationLeft, animationRight;
    // Attack timer
    private long lastAttackTimer, attackCoolDown = 800, attackTimer = attackCoolDown;
    // Inventory
    private Inventory inventory;
    // Actions
    private PlayerActionsHandler playerActionsHandler; // TODO do class static?
    private Map<String, Point> pressedKeys = new HashMap<>();


    public Player(Handler handler, float x, float y) {
        super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);
        bounds.x = 16;
        bounds.y = 8;
        bounds.width = 32;
        bounds.height = 54;
        animationLeft = new Animation(MILLI_SEC_PER_PLAYER_FRAME, Assets.playerLeft);
        animationRight = new Animation(MILLI_SEC_PER_PLAYER_FRAME, Assets.playerRight);
        playerActionsHandler = new PlayerActionsHandler(this, handler);
        playerActionsHandler.initPlayerKeys();
        inventory = new Inventory(handler);
    }

    @Override
    public void tick() {
        //Animation
        animationLeft.tick();
        animationRight.tick();
        getInput();
        move();
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
        xMove = 0;
        for (Map.Entry<String, Point> entry : pressedKeys.entrySet()){
            if (entry.getKey().equals("RIGHT")){
                xMove += entry.getValue().x * horizontalSpeed;
            }
            else if (entry.getKey().equals("LEFT")){
                xMove += entry.getValue().x * horizontalSpeed;
            }
            else if (entry.getKey().equals("UP")){
                makeJump(entry.getValue().y);
            }
        }
    }

    private void makeJump(int jumpScale){
        if (!airborne){
            ySpeed = jumpScale * DEFAULT_JUMP_SPEED;
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
        if (xMove < 0){
            return animationLeft.getCurrentFrame();
        }
        else if (xMove > 0){
            return animationRight.getCurrentFrame();
        }
        else {
            if (lastDirectionRight){
                return animationRight.getFirstFrame();
            }
            return animationLeft.getFirstFrame();
        }
    }

    //GETTERS SETTERS


    public void removePressedKey(String key) {
        pressedKeys.remove(key);
        if (key.equals("RIGHT")){
            lastDirectionRight = true;
        }
        else if (key.equals("LEFT")){
            lastDirectionRight = false;
        }
    }

    public void addPressedKey(String key, Point moveDelta) {
        pressedKeys.put(key, moveDelta);
    }

    public Inventory getInventory() {
        return inventory;
    }
}
