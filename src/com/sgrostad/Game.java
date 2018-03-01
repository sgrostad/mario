package com.sgrostad;

import com.sgrostad.gfx.Assets;
import com.sgrostad.display.Display;
import com.sgrostad.gfx.GameCamera;
import com.sgrostad.input.KeyManager;
import com.sgrostad.states.GameState;
import com.sgrostad.states.MenuState;
import com.sgrostad.states.State;

import java.awt.*;
import java.awt.image.BufferStrategy;


public class Game implements Runnable{

    private Display display;

    private boolean running = false;
    private Thread thread;

    private String title;
    private int width, height;

    private BufferStrategy bs;
    private Graphics g;

    //Input

    private KeyManager keyManager;

    //States:
    private State gameState;
    private State menuState;

    //Camera
    private GameCamera gameCamera;

    //Handler
    private Handler handler;

    public Game(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;
        keyManager = new KeyManager();
    }

    private void init(){
        display = new Display(title, width, height);
        display.getFrame().addKeyListener(keyManager);
        Assets.init();
        handler = new Handler(this);
        gameCamera = new GameCamera(handler, 0,0);
        gameState = new GameState(handler);
        menuState = new MenuState(handler);
        State.setCurrentState(gameState);
    }

    private void tick(){
        keyManager.tick();
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
        double nanoSecPerSec = 1000000000.0;
        int fps = 60;
        double timePerTick = nanoSecPerSec / fps;
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
            if (timer >= nanoSecPerSec){
                System.out.println("Frames per second: " + ticks);
                timer -= nanoSecPerSec;
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

    public KeyManager getKeyManager() {
        return keyManager;
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
}
