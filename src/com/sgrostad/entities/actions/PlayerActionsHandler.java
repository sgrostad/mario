package com.sgrostad.entities.actions;

import com.sgrostad.Handler;
import com.sgrostad.entities.creatures.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerActionsHandler {

    private static final String PRESSED = "pressed ";
    private static final String RELEASED = "released ";

    private Player player;
    private JComponent component;
    private Handler handler;

    public PlayerActionsHandler(Player player, Handler handler) {
        this.player = player;
        this.handler = handler;
    }

    public void initPlayerKeys(){
        component = new JLabel();
        addMoveAction("LEFT", -1, 0);
        addMoveAction("RIGHT", 1, 0);
        addMoveAction("UP", 0, 1);
        handler.getGame().getFrame().add(component);
    }

    public void addMoveAction(String keyStroke, int deltaX, int deltaY)
    {
        int offset = keyStroke.lastIndexOf(" ");
        String key = offset == -1 ? keyStroke :  keyStroke.substring( offset + 1 );
        String modifiers = keyStroke.replace(key, "");

        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();

        Action pressedAction = new PlayerAction(key, new Point(deltaX, deltaY));
        String pressedKey = modifiers + PRESSED + key;
        KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(pressedKey);
        inputMap.put(pressedKeyStroke, pressedKey);
        actionMap.put(pressedKey, pressedAction);

        Action releasedAction = new PlayerAction(key, null);
        String releasedKey = modifiers + RELEASED + key;
        KeyStroke releasedKeyStroke = KeyStroke.getKeyStroke(releasedKey);
        inputMap.put(releasedKeyStroke, releasedKey);
        actionMap.put(releasedKey, releasedAction);
    }

    private class PlayerAction extends AbstractAction implements ActionListener
    {

        private Point moveDelta;

        public PlayerAction(String key, Point moveDelta)
        {
            super(key);

            this.moveDelta = moveDelta;
        }
        public void actionPerformed(ActionEvent e)
        {
            handleKeyEvent((String)getValue(NAME), moveDelta);
        }

    }

    private void handleKeyEvent(String key, Point moveDelta) {
        if (moveDelta == null) {
            player.removePressedKey(key);
        }
        else {
            player.addPressedKey(key, moveDelta);
        }
    }
}
