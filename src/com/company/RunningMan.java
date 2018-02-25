package com.company;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class RunningMan extends Sprite {
    private final String IMAGE_LOCATION = "running_man.png";
    private int dx;
    private int dy;
    private BufferedImage[] sprites;
    int rows = 2;
    int columns = 8;
    private int imageNum;
    private boolean goingRight;

    public RunningMan(){
        super(40,60);
        initRunningMan();
    }

    private void initRunningMan(){
        try {
            BufferedImage bigImage = ImageIO.read(new File(IMAGE_LOCATION));
            int width = 108;
            int height = 140;
            sprites = new BufferedImage[rows * columns];
            for (int row = 0; row<rows; row++){
                for (int col = 0; col<columns; col++){
                    sprites[(row*columns) + col] = bigImage.getSubimage(col*width, row * height, width, height);
                }
            }
            x = 40;
            y = 60;
            imageNum = 0;
            goingRight = true;
        }catch (Exception e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public void move(){
        x += dx*5;
        y += dy*5;
        if (dx != 0){
            imageNum++;
        }
        if (imageNum >= columns){
            imageNum = 0;
        }
    }

    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT){
            dx = -1;
            goingRight = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = 1;
            goingRight = true;
        }
        if (key == KeyEvent.VK_UP) {
            dy = -1;
        }
        if (key == KeyEvent.VK_DOWN) {
            dy = 1;
        }
        if (key == KeyEvent.VK_SPACE){
            imageNum++;
        }
    }

    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT){
            dx = 0;
            imageNum = 0;
        }
        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
            imageNum = 0;
        }
        if (key == KeyEvent.VK_UP) {
            dy = 0;
        }
        if (key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }


    public BufferedImage getImage() {
        if (goingRight){
            return sprites[imageNum];
        }
        else {
            return sprites[columns + imageNum];
        }
    }
}
