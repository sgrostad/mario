package com.sgrostad.input.player;

import com.sgrostad.entities.mobiles.Player;

public class PlayerTakeOffTimer {
    private final float STANDARD_MINIMUM_JUMP_PERCENTAGE = 0.7f;
    private final int STANDARD_MILLI_SECONDS_FOR_MAX_JUMP = 600;

    private Player player;
    private long takeOffTimer, lastTime;
    private boolean wantToJump;
    private boolean readyToJump;

    public PlayerTakeOffTimer(Player player) {
        this.player = player;
        takeOffTimer = 0;
        wantToJump = false;
        readyToJump = false;
    }

    public void tick(){
        if (wantToJump && !readyToJump && !player.isAirborne()){
            prepareTakeOff();
        }
        else if (readyToJump){
            takeOffTimer += System.currentTimeMillis() - lastTime;
            lastTime = System.currentTimeMillis();
        }
    }

    public void prepareTakeOff(){
        wantToJump = true;
        if (!player.isAirborne()){
            lastTime = System.currentTimeMillis();
            readyToJump = true;
        }
    }

    public void resetTakeOff(){
        takeOffTimer = 0;
        wantToJump = false;
        readyToJump = false;
    }

    public boolean isActive(){
        if (takeOffTimer == 0){
            return false;
        }
        return true;
    }

    public float getJumpForce(){
        float jumpForce = 0;
        if (readyToJump){
            jumpForce = Math.min(1.0f,
                    STANDARD_MINIMUM_JUMP_PERCENTAGE + ((float)takeOffTimer / STANDARD_MILLI_SECONDS_FOR_MAX_JUMP) *
                            (1.0f - STANDARD_MINIMUM_JUMP_PERCENTAGE)); // between minimum jump and 1
        }
        resetTakeOff();
        return jumpForce;
    }
}
