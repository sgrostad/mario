package com.sgrostad.tiles;

import com.sgrostad.gfx.Assets;

import java.awt.image.BufferedImage;

public class RockTile extends Tile {

    public RockTile(int id) {
        super(Assets.icons.get(3), id);
    }

    @Override
    public boolean isSolid(){
        return true;
    }
}
