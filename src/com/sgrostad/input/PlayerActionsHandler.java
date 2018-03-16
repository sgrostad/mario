package com.sgrostad.input;

import com.sgrostad.Handler;
import com.sgrostad.entities.creatures.Player;

import javax.swing.*;
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
        addMoveAction("LEFT", com.sgrostad.entities.creatures.PlayerAction.LEFT);
        addMoveAction("RIGHT", com.sgrostad.entities.creatures.PlayerAction.RIGHT);
        addMoveAction("UP", com.sgrostad.entities.creatures.PlayerAction.JUMP);
        handler.getGame().getFrame().add(component);
    }

    public void addMoveAction(String keyStroke, com.sgrostad.entities.creatures.PlayerAction playerAction)
    {
        int offset = keyStroke.lastIndexOf(" ");
        String key = offset == -1 ? keyStroke :  keyStroke.substring( offset + 1 );
        String modifiers = keyStroke.replace(key, "");

        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();

        Action pressedAction = new PlayerAction(key, playerAction);
        String pressedKey = modifiers + PRESSED + key;
        KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(pressedKey);
        inputMap.put(pressedKeyStroke, pressedKey);
        actionMap.put(pressedKey, pressedAction);

        Action releasedAction = new PlayerAction(key, com.sgrostad.entities.creatures.PlayerAction.STILL);
        String releasedKey = modifiers + RELEASED + key;
        KeyStroke releasedKeyStroke = KeyStroke.getKeyStroke(releasedKey);
        inputMap.put(releasedKeyStroke, releasedKey);
        actionMap.put(releasedKey, releasedAction);
    }

    private class PlayerAction extends AbstractAction implements ActionListener
    {

        private com.sgrostad.entities.creatures.PlayerAction playerAction;

        public PlayerAction(String key, com.sgrostad.entities.creatures.PlayerAction playerAction)
        {
            super(key);

            this.playerAction = playerAction;
        }
        public void actionPerformed(ActionEvent e)
        {
            handleKeyEvent((String)getValue(NAME), playerAction);
        }

    }

    private void handleKeyEvent(String key, com.sgrostad.entities.creatures.PlayerAction playerAction) {
        if (playerAction.standingStill()) {
            player.removePressedKey(key);
        }
        else {
            player.addPressedKey(key, playerAction);
        }
    }
}
