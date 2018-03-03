package com.sgrostad.ui;

import com.sgrostad.Handler;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class UIManager {

    private Handler handler;
    private List<UIObject> objects;

    public UIManager(Handler handler) {
        this.handler = handler;
        objects = new ArrayList<>();
    }

    public void tick(){
        for (UIObject o : objects){
            o.tick();
        }
    }

    public void render(Graphics g){
        for (UIObject o : objects){
            o.render(g);
        }
    }

    public void onMouseMove(MouseEvent e){
        for (UIObject o : objects){
            o.onMouseMove(e);
        }
    }

    public void onMouseReleased(MouseEvent e){
        for (UIObject o : objects){
            o.onMouseRelease(e);
        }
    }

    public void addObject(UIObject o){
        objects.add(o);
    }

    public void removeObject(UIObject o){
        objects.remove(o);
    }

    // GETTERS SETTERS


    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public List<UIObject> getObjects() {
        return objects;
    }

    public void setObjects(List<UIObject> objects) {
        this.objects = objects;
    }
}
