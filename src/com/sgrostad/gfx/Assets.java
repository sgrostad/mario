package com.sgrostad.gfx;

import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Assets {

    public static Font font28;

    public static List<BufferedImage> greenAndBrownTiles;
    public static BufferedImage clearSky;
    public static List<BufferedImage> sky;
    public static List<BufferedImage> machineGunBullet;

    public static List<BufferedImage> playerDown, playerUp, playerLeft, playerRight;
    public static List<BufferedImage> ghostDown, ghostUp, ghostLeft, ghostRight;

    public static List<BufferedImage> blackSmoke;
    public static List<BufferedImage> trees;

    public static void init(){
        font28 = FontLoader.loadFont("res/fonts/slkscr.ttf",28);
        greenAndBrownTiles = initSpriteSheet("/textures/green&brown_tiles.png", 630, 140, 70,70);
        clearSky = initSpriteSheet("/textures/clear_sky.png",70,70,70,70).get(0);
        machineGunBullet = initSpriteSheet("/textures/machine_gun_bullet.png",136,22,68,22);
        sky = initSpriteSheet("/textures/sky_5x230x130.png", 5*230, 130, 230, 130);

        blackSmoke = initMultipleSprites("res/textures/black_smoke/blackSmoke", 0,24);
        trees = initMultipleSprites("res/textures/trees/foliagePack_0", 0,61);

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

    private static List<BufferedImage> initMultipleSprites(String filePath, int startValue, int endValue){
        List<BufferedImage> bufferedImages = new ArrayList<>();
        try {
            for (int i = startValue; i <= endValue; i++){
                bufferedImages.add(ImageIO.read(new File(filePath + i + ".png")));
            }
        } catch (IOException e){
            e.printStackTrace();
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
    private static void initGhostAsset(){
        List<BufferedImage> playerAnimations = initSpriteSheet("/textures/walking_ghost_man_224x256.png", 224,
                256, 32, 64);
        ghostDown = playerAnimations.subList(0, 6);
        ghostLeft = playerAnimations.subList(7, 13);
        ghostRight = playerAnimations.subList(14,20);
        ghostUp = playerAnimations.subList(21,27);
    }

}
