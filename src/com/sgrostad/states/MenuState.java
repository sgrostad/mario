package com.sgrostad.states;

import com.sgrostad.Game;
import com.sgrostad.Handler;
import com.sgrostad.gfx.Assets;
import com.sgrostad.ui.ClickListener;
import com.sgrostad.ui.UIImageButton;
import com.sgrostad.ui.UIManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuState extends State {

    private UIManager uiManager;

    public MenuState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUiManager(uiManager);
        BufferedImage arr[] = {Assets.playerLeft.get(0), Assets.playerLeft.get(0)}; //TODO fix
        uiManager.addObject(new UIImageButton(200, 200, 64, 64, arr, new ClickListener() {
            @Override
            public void onClick() {
                handler.getMouseManager().setUiManager(null);
                State.setCurrentState(handler.getGame().gameState);
            }
        }));
    }

    @Override
    public void tick() {
        uiManager.tick();
        /////
        handler.getMouseManager().setUiManager(null);
        State.setCurrentState(handler.getGame().gameState);
    }

    @Override
    public void render(Graphics g) {
        uiManager.render(g);
    }

}
