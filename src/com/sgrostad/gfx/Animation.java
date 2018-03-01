package com.sgrostad.gfx;

import java.awt.image.BufferedImage;
import java.util.List;

public class Animation {

    private int milliSecPerFrame, index;
    private List<BufferedImage> frames;
    private long lastTime, timer;

    public void tick() {
        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        if (timer > milliSecPerFrame) {
            index++;
            timer = 0;
            if (index >= frames.size()) {
                index = 0;
            }
        }
    }

    public Animation(int milliSecPerFrame, List<BufferedImage> frames) {
        this.milliSecPerFrame = milliSecPerFrame;
        this.index = 0;
        this.frames = frames;
        lastTime = System.currentTimeMillis();
    }

    public BufferedImage getCurrentFrame(){
        return frames.get(index);
    }

    public BufferedImage getFirstFrame(){
        return frames.get(0);
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
