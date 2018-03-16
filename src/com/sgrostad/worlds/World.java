package com.sgrostad.worlds;

import com.sgrostad.Handler;
import com.sgrostad.entities.EntityManager;
import com.sgrostad.entities.creatures.Player;
import com.sgrostad.entities.statics.Tree;
import com.sgrostad.items.ItemManager;
import com.sgrostad.tiles.Tile;
import com.sgrostad.utils.Utils;

import java.awt.*;

public class World {

    private Handler handler;
    private int width, height;
    private int spawnX, spawnY;
    private int[][] worldTiles;
    //Entities
    private EntityManager entityManager;
    //Item
    private ItemManager itemManager;


    public World(Handler handler, String path) {
        this.handler = handler;
        entityManager = new EntityManager(handler, new Player(handler,100,100));
        itemManager = new ItemManager(handler);
        loadWorld(path);
        entityManager.getPlayer().setX(spawnX);
        entityManager.getPlayer().setY(spawnY);
        //entityManager.addEntity(new Tree(handler, 100, 200));
        //entityManager.addEntity(new Tree(handler, 100, 300));
        //entityManager.addEntity(new Tree(handler, 100, 400));
    }

    public void tick(){
        entityManager.tick();
        itemManager.tick();
    }

    public void render(Graphics g){
        int xStart = (int) Math.max(0, handler.getGameCamera().getxOffset()/Tile.TILE_WIDTH);
        int xEnd = (int) Math.min(width, (handler.getGameCamera().getxOffset() + handler.getWidth()) / Tile.TILE_WIDTH + 1);
        int yStart = (int) Math.max(0, handler.getGameCamera().getyOffset()/Tile.TILE_HEIGHT);
        int yEnd = (int) Math.min(height, (handler.getGameCamera().getyOffset() + handler.getHeight()) / Tile.TILE_HEIGHT + 1);

        for (int y = yStart; y < yEnd; y++){
            for (int x = xStart; x < xEnd; x++){
                getTile(x,y).render(g, (int)(x * Tile.TILE_WIDTH - handler.getGameCamera().getxOffset()),
                        (int)(y * Tile.TILE_HEIGHT - handler.getGameCamera().getyOffset()));
            }
        }
        //Items
        itemManager.render(g);
        //Entities
        entityManager.render(g);
    }

    public Tile getTile(int x, int y){
        if (x < 0 || y < 0 || x >= width || y >= height){
            return Tile.grassTile;
        }
        Tile t = Tile.tiles[worldTiles[x][y]];
        if (t == null){
            return Tile.dirtTile;
        }
        return t;
    }

    private void loadWorld(String path){
        String file = Utils.loadFileAsString(path);
        String[] tokens = file.split("\\s+");
        height = Utils.parseInt(tokens[0]);
        width = Utils.parseInt(tokens[1]);
        spawnX = Utils.parseInt(tokens[2]);
        spawnY = Utils.parseInt(tokens[3]);
        worldTiles = new int[width][height];
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                worldTiles[x][y] = Utils.parseInt(tokens[4 + (x + y * width)]);
            }
        }
    }

    // GETTERS SETTERS


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public void setItemManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
