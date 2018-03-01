package com.sgrostad.states;

import com.sgrostad.Game;
import com.sgrostad.Handler;
import com.sgrostad.entities.creatures.Player;
import com.sgrostad.gfx.Assets;
import com.sgrostad.tiles.Tile;
import com.sgrostad.worlds.World;

import java.awt.*;

public class GameState extends State {

    private Player player;
    private World world;

    public GameState(Handler handler){
        super(handler);
        world = new World(handler,"res/worlds/world1.txt");
        handler.setWorld(world);
        player = new Player(handler,100,100);
    }

    @Override
    public void tick() {
        world.tick();
        player.tick();
    }

    @Override
    public void render(Graphics g) {
        world.render(g);
        player.render(g);
    }
}
