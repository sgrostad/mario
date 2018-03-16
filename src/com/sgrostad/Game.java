package com.sgrostad;

import com.sgrostad.gfx.Assets;
import com.sgrostad.display.Display;
import com.sgrostad.gfx.GameCamera;
import com.sgrostad.input.MouseManager;
import com.sgrostad.states.GameState;
import com.sgrostad.states.MenuState;
import com.sgrostad.states.State;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;


public class Game implements Runnable{

    public static final double NANO_SEC_PER_SEC = 1000000000.0;
    public static final double MICRO_SEC_PER_SEC = 1000000.0;
    public static final double MILLI_SEC_PER_SEC = 1000.0;
    public static final int FPS = 60;

    private Display display;

    private boolean running = false;
    private Thread thread;

    private String title;
    private int width, height;

    private BufferStrategy bs;
    private Graphics g;

    //Input
    private MouseManager mouseManager;

    //States:
    public State gameState;
    public State menuState;

    //Camera
    private GameCamera gameCamera;

    //Handler
    private Handler handler;

    public Game(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;
        mouseManager = new MouseManager();
    }

    private void init(){
        display = new Display(title, width, height);
        display.getFrame().addMouseListener(mouseManager);
        display.getFrame().addMouseMotionListener(mouseManager);
        display.getCanvas().addMouseListener(mouseManager);
        display.getCanvas().addMouseMotionListener(mouseManager);
        Assets.init();
        handler = new Handler(this);
        gameCamera = new GameCamera(handler, 0,0);
        gameState = new GameState(handler);
        menuState = new MenuState(handler);
        State.setCurrentState(menuState);
    }

    private void tick(){
        if (State.getCurrentState() != null){
            State.getCurrentState().tick();
        }
    }

    private void render(){
        bs = display.getCanvas().getBufferStrategy();
        if (bs == null){
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        g.clearRect(0,0, width, height);
        // Draw:
        if (State.getCurrentState() != null){
            State.getCurrentState().render(g);
        }
        // End Drawing
        bs.show();
        g.dispose();
    }

    @Override
    public void run() {
        init();
        double timePerTick = NANO_SEC_PER_SEC / FPS;
        double delta = 0;
        long now;
        long lastTime = System.nanoTime();
        long timer = 0;
        int ticks = 0;

        while (running){
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            timer += now - lastTime;
            lastTime = now;
            if (delta >= 1) {
                tick();
                render();
                delta--;
                ticks++;
            }
            if (timer >= NANO_SEC_PER_SEC){
                System.out.println("Frames per second: " + ticks);
                timer -= NANO_SEC_PER_SEC;
                ticks = 0;
            }
        }
        stop();
    }



    public synchronized void start(){
        if (running){
            System.err.println("Start is called from wrong place");
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public MouseManager getMouseManager() {
        return mouseManager;
    }

    public synchronized void stop(){
        if (!running){
            System.err.println("Stop is called from wrong place");
            return;
        }
        running = false;
        try{
            thread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    //Getter and setters


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

    public GameCamera getGameCamera() {
        return gameCamera;
    }

    public JFrame getFrame() {
        return display.getFrame();
    }
}
