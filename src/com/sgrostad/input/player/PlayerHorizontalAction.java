package com.sgrostad.input.player;

import com.sgrostad.Handler;
import com.sgrostad.entities.creatures.Direction;
import com.sgrostad.input.player.PlayerActions;

public class PlayerHorizontalAction extends PlayerActions {

    public PlayerHorizontalAction(Handler handler) {
        super(handler);
    }

    @Override
    protected void addActions(){
        addAction("LEFT");
        addAction("RIGHT");
    }
    
    @Override
    protected void handleKeyEvent(String key, boolean pressed) {
        Direction direction = Direction.keyToDirection(key);
        if (!pressed) {
            handler.getWorld().getEntityManager().getPlayer().removePressedKey(key);
        }
        else {
            handler.getWorld().getEntityManager().getPlayer().addPressedKey(key, direction);
        }
    }
}
