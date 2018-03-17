package com.sgrostad.input;

import com.sgrostad.Handler;
import com.sgrostad.entities.creatures.player.PlayerActionType;

import javax.swing.*;

public class PlayerActionsHandler extends PlayerActions {

    public PlayerActionsHandler(Handler handler) {
        super(handler);
    }

    @Override
    protected void addActions(){
        addAction("LEFT");
        addAction("RIGHT");
        addAction("UP");
    }
    
    @Override
    protected void handleKeyEvent(String key, boolean pressed) {
        PlayerActionType playerActionType = PlayerActionType.keyToPlayerActionType(key);
        if (!pressed) {
            handler.getWorld().getEntityManager().getPlayer().removePressedKey(key);
        }
        else {
            handler.getWorld().getEntityManager().getPlayer().addPressedKey(key, playerActionType);
        }
    }
}
