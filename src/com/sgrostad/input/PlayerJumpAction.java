package com.sgrostad.input;

import com.sgrostad.Handler;

public class PlayerJumpAction extends PlayerActions {

    public PlayerJumpAction(Handler handler) {
        super(handler);
    }

    @Override
    public void tick() {
        handler.getWorld().getEntityManager().getPlayer().getPlayerTakeOffTimer().tick();
    }

    @Override
    protected void addActions() {
        addAction("UP");
    }

    @Override
    protected void handleKeyEvent(String key, boolean pressed) {
        if (pressed){
            handler.getWorld().getEntityManager().getPlayer().getPlayerTakeOffTimer().prepareTakeOff();
        }else {
            handler.getWorld().getEntityManager().getPlayer().makeJump();
        }
    }
}
