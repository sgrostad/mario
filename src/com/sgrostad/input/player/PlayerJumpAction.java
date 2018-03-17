package com.sgrostad.input.player;

import com.sgrostad.Handler;

public class PlayerJumpAction extends PlayerActions {

    private final String HIGH_JUMP_PREPERATION_KEY = "D";
    private final String JUMP_KEY = "UP";

    private PlayerTakeOffTimer playerTakeOffTimer;

    public PlayerJumpAction(Handler handler, PlayerTakeOffTimer playerTakeOffTimer) {
        super(handler);
        this.playerTakeOffTimer = playerTakeOffTimer;
    }

    @Override
    public void tick() {
        playerTakeOffTimer.tick();
    }

    @Override
    protected void addActions() {
        addAction(HIGH_JUMP_PREPERATION_KEY);
        addAction(JUMP_KEY);
    }

    @Override
    protected void handleKeyEvent(String key, boolean pressed) {
        if (key.equals(JUMP_KEY) && pressed){
            if (!playerTakeOffTimer.isActive()){
                playerTakeOffTimer.prepareTakeOff();
            }
            handler.getWorld().getEntityManager().getPlayer().makeJump(playerTakeOffTimer.getJumpForce());
        }
        else {
            if (pressed) {
                playerTakeOffTimer.prepareTakeOff();
            } else {
                playerTakeOffTimer.resetTakeOff();
            }
        }
    }
}
