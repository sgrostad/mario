package com.sgrostad.entities.statics;

import com.sgrostad.Handler;
import com.sgrostad.gfx.Assets;
import com.sgrostad.items.Item;
import com.sgrostad.tiles.Tile;

import java.awt.*;

public class Tree extends StaticEntity {

    public Tree(Handler handler, float x, float y) {
        super(handler, x, y, Tile.TILE_WIDTH, Tile.TILE_HEIGHT * 2);
        bounds.x = width/3;
        bounds.y = height/2;
        bounds.width = width/3;
        bounds.height = height/4;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        /*g.setColor(Color.red);
        g.fillRect((int)(x + bounds.x - handler.getGameCamera().getxOffset()),
                (int)(y + bounds.y - handler.getGameCamera().getyOffset()), bounds.width, bounds.height);
        */
        g.drawImage(Assets.icons.get(3), (int)(x - handler.getGameCamera().getxOffset()),
                (int) (y - handler.getGameCamera().getyOffset()), width, height, null);
    }

    @Override
    public void die() {
        handler.getWorld().getItemManager().addItem(Item.woodItem.createNew((int)x, (int)y));
    }
}
