package com.sgrostad.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UIImageButton extends UIObject {

    private BufferedImage[] bufferedImages;
    private ClickListener clicker;

    public UIImageButton(float x, float y, int width, int height, BufferedImage[] bufferedImages, ClickListener clicker) {
        super(x, y, width, height);
        this.bufferedImages = bufferedImages;
        this.clicker = clicker;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        if (hovering){
            g.drawImage(bufferedImages[1],(int)x, (int)y, width, height,null);
        }else {
            g.drawImage(bufferedImages[0],(int)x, (int)y, width, height,null);
        }
    }

    @Override
    public void onClick() {
        clicker.onClick();
    }
}
