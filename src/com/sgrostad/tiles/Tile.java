package com.sgrostad.tiles;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {

    public static Tile[] tiles = new Tile[256];
    public static Tile skyTile = new SkyTile(0);
    public static Tile grassTile = new GrassTile(1);

    //CLASS

    public static final int TILE_WIDTH = 64, TILE_HEIGHT = 64;

    private BufferedImage texture;
    private final int id;

    public Tile(BufferedImage texture, int id) {
        this.texture = texture;
        this.id = id;
        tiles[id] = this;
    }

    public void tick(){

    }

    public void render(Graphics g, int x, int y){
        g.drawImage(texture, x, y, TILE_WIDTH, TILE_HEIGHT, null);
    }

    public boolean isSolid(){
        return false;
    }

    public int getId() {
        return id;
    }
}
