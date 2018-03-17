package com.sgrostad.input.player;

import com.sgrostad.Handler;

public class PlayerJumpAction extends PlayerActions {

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
        addAction("UP");
    }

    @Override
    protected void handleKeyEvent(String key, boolean pressed) {
        if (pressed){
            playerTakeOffTimer.prepareTakeOff();
        }else {
            handler.getWorld().getEntityManager().getPlayer().makeJump(playerTakeOffTimer.getJumpForce());
        }
    }
}
