package com.sgrostad.tiles;

import com.sgrostad.gfx.Assets;

public class GrassTile extends Tile{

    public GrassTile(int id) {
        super(Assets.greenAndBrownTiles.get(2), id);
    }

    @Override
    public boolean isSolid() {
        return true;
    }
}
