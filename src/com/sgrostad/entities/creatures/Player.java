package com.sgrostad.entities.creatures;

import com.sgrostad.Handler;
import com.sgrostad.gfx.Animation;
import com.sgrostad.gfx.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Creature {

    public static final int MILLI_SEC_PER_PLAYER_FRAME = 80;
    // Animations
    private Animation animationDown;
    private Animation animationUp;
    private Animation animationLeft;
    private Animation animationRight;


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
}
