package com.sgrostad.entities.creatures;

import com.sgrostad.Handler;
import com.sgrostad.entities.Entity;
import com.sgrostad.gfx.Animation;
import com.sgrostad.gfx.Assets;
import com.sgrostad.inventory.Inventory;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Creature {

    public static final int MILLI_SEC_PER_PLAYER_FRAME = 80;
    // Animations
    private Animation animationDown, animationUp, animationLeft, animationRight;
    // Attack timer
    private long lastAttackTimer, attackCoolDown = 800, attackTimer = attackCoolDown;
    // Inventory
    private Inventory inventory;


    public Player(Handler handler, float x, float y) {
        super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);
        bounds.x = 16;
        bounds.y = 8;
        bounds.width = 32;
        bounds.height = 54;
        animationDown = new Animation(MILLI_SEC_PER_PLAYER_FRAME, Assets.playerDown);
        animationUp = new Animation(MILLI_SEC_PER_PLAYER_FRAME, Assets.playerUp);
        animationLeft = new Animation(MILLI_SEC_PER_PLAYER_FRAME, Assets.playerLeft);
        animationRight = new Animation(MILLI_SEC_PER_PLAYER_FRAME, Assets.playerRight);

        inventory = new Inventory(handler);
    }

    @Override
    public void tick() {
        //Animation
        animationDown.tick();
        animationUp.tick();
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
        attackTimer = 0;
    }
    private void getInput(){
        yMove = 0;
        xMove = 0;
        if (handler.getKeyManager().up){
            yMove = -speed;
        }
        if (handler.getKeyManager().down){
            yMove = speed;
        }
        if (handler.getKeyManager().left){
            xMove = -speed;
        }
        if (handler.getKeyManager().right){
            xMove = speed;
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
        else if (yMove < 0){
            return animationUp.getCurrentFrame();
        }
        else if (yMove > 0){
            return animationDown.getCurrentFrame();
        }
        else {
            return animationDown.getFirstFrame();
        }
    }

    //GETTERS SETTERS


    public Inventory getInventory() {
        return inventory;
    }
}
