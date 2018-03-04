package com.sgrostad.items;

import com.sgrostad.Handler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemManager {

    private Handler handler;
    private List<Item> items;

    public ItemManager(Handler handler) {
        this.handler = handler;
        items = new ArrayList<>();
    }

    public void tick(){
        Iterator<Item> it = items.iterator();
        while (it.hasNext()){
            Item i = it.next();
            i.tick();
            if (i.isPickedUp()){
                it.remove();
            }
        }
    }

    public void render(Graphics g){
        for (Item i : items){
            i.render(g);
        }
    }

    public void addItem(Item i){
        i.setHandler(handler);
        items.add(i);
    }

    // GETTERS SETTERS


    public Handler getHandler() {
        return handler;
    }

}
