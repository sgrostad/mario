package com.sgrostad.input;

import com.sgrostad.Handler;
import com.sgrostad.display.Display;
import com.sgrostad.entities.creatures.Player;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager {

    private JComponent component;
    private Player player;

    public KeyManager(JComponent component, Player player){
        this.component = component;
        this.player = player;
    }

    public void addPlayerMoves(){
        PlayerMoveAction leftMove = new PlayerMoveAction(player,"LEFT", -1, 0);
        addKeyBinding(KeyEvent.VK_LEFT, "LEFT", leftMove);

        PlayerMoveAction rightMove = new PlayerMoveAction(player,"RIGHT", 1, 0);
        addKeyBinding(KeyEvent.VK_RIGHT, "RIGHT", rightMove);

        PlayerMoveAction upMove = new PlayerMoveAction(player,"UP", 0, -1);
        addKeyBinding(KeyEvent.VK_UP, "UP", upMove);

        PlayerMoveAction downMove = new PlayerMoveAction(player,"DOWN", 0, 1);
        addKeyBinding(KeyEvent.VK_DOWN, "DOWN", downMove);
    }

    public void addKeyBinding(int keyCode, String id, AbstractAction abstractAction){
        KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(keyCode, 0, false);
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();
        inputMap.put(pressedKeyStroke, id);
        actionMap.put(id, abstractAction);
    }
}
/*

public class KeyManager implements KeyListener {
    private boolean[] keys, justPressed, cantPress;
    public boolean up, down, right, left;
    public boolean aUp, aDown, aRight, aLeft;

    public KeyManager() {
        keys = new boolean[256];
        justPressed = new boolean[keys.length];
        cantPress = new boolean[keys.length];
    }

    public void tick(){
        for (int i = 0; i < keys.length; i++){
            if (cantPress[i] && !keys[i]){
                cantPress[i] = false;
            }else if (justPressed[i]){
                cantPress[i] = true;
                justPressed[i] = false;
            }
            if (!cantPress[i] && keys[i]){
                justPressed[i] = true;
            }
        }

        up = keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_D];

        aUp = keys[KeyEvent.VK_UP];
        aDown = keys[KeyEvent.VK_DOWN];
        aLeft = keys[KeyEvent.VK_LEFT];
        aRight = keys[KeyEvent.VK_RIGHT];
    }

    public boolean keyJustPressed(int keyCode){
        if (keyCode < 0 || keyCode >= keys.length){
            return false;
        }
        return justPressed[keyCode];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < 0 || e.getKeyCode() >= keys.length){
            return;
        }
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < 0 || e.getKeyCode() >= keys.length){
            return;
        }
        keys[e.getKeyCode()] = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
*/