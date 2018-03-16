package com.sgrostad.inventory;

import com.sgrostad.Handler;
import com.sgrostad.items.Item;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private Handler handler;
    private boolean active = false;
    private List<Item> inventoryItems;

    public Inventory(Handler handler) {
        this.handler = handler;
        inventoryItems = new ArrayList<>();
    }

    public void tick(){
        /*if (handler.getKeyManager().keyJustPressed(KeyEvent.VK_E)){
            active = !active;
        }
        if (!active){
            return;
        }*/
        //TODO fix with key bindings
    }

    public void render(Graphics g){
        if (!active){
            return;
        }
    }

    //inventory methods

    public void addItem(Item item){
        for (Item i: inventoryItems){
            if (i.getId() == item.getId()){
                i.setCount(i.getCount() + item.getCount());
                return;
            }
        }
        inventoryItems.add(item);
    }
}
