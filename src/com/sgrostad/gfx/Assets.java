package com.sgrostad.gfx;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Assets {

    public static List<BufferedImage> numbers;
    public static List<BufferedImage> icons;

    public static List<BufferedImage> playerDown;
    public static List<BufferedImage> playerUp;
    public static List<BufferedImage> playerLeft;
    public static List<BufferedImage> playerRight;

    public static void init(){
        numbers = initSpriteSheet("/textures/16x16.png",128,128,32,32);
        icons = initSpriteSheet("/textures/16x16_icons.png",128,128,32,32);
        initPlayerAsset();
    }
    private static List<BufferedImage> initSpriteSheet(String filePath, int sheetWidth, int sheetHeight,
                                        int spriteWidth, int spriteHeight){
        if (sheetWidth % spriteWidth != 0 || sheetHeight % spriteHeight != 0){
            System.err.println("Wrong dimensions for sprite sheet " + filePath);
            System.exit(1);
        }
        List<BufferedImage> bufferedImages = new ArrayList<>();
        SpriteSheet spriteSheet = new SpriteSheet(ImageLoader.loadImage(filePath));
        for (int row = 0; row < sheetHeight/spriteHeight; row++){
            for (int col = 0; col < sheetWidth/spriteWidth; col++){
                bufferedImages.add(spriteSheet.crop(col*spriteWidth, row*spriteHeight, spriteWidth, spriteHeight));
            }
        }
        return bufferedImages;
    }

    private static void initPlayerAsset(){
        List<BufferedImage> playerAnimations = initSpriteSheet("/textures/walking_man_224x256.png", 224,
                256, 32, 64);
        playerDown = playerAnimations.subList(0, 6);
        playerLeft = playerAnimations.subList(7, 13);
        playerRight = playerAnimations.subList(14,20);
        playerUp = playerAnimations.subList(21,27);
    }
}
