package com.sgrostad.input;

import com.sgrostad.Handler;
import com.sgrostad.entities.creatures.Player;
import com.sgrostad.entities.creatures.PlayerActionType;

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
        addMoveAction("LEFT", PlayerActionType.LEFT);
        addMoveAction("RIGHT", PlayerActionType.RIGHT);
        addMoveAction("UP", PlayerActionType.JUMP);
        handler.getGame().getFrame().add(component);
    }

    public void addMoveAction(String keyStroke, PlayerActionType playerActionType)
    {
        int offset = keyStroke.lastIndexOf(" ");
        String key = offset == -1 ? keyStroke :  keyStroke.substring( offset + 1 );
        String modifiers = keyStroke.replace(key, "");

        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();

        Action pressedAction = new PlayerAction(key, playerActionType);
        String pressedKey = modifiers + PRESSED + key;
        KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(pressedKey);
        inputMap.put(pressedKeyStroke, pressedKey);
        actionMap.put(pressedKey, pressedAction);

        Action releasedAction = new PlayerAction(key, PlayerActionType.STILL);
        String releasedKey = modifiers + RELEASED + key;
        KeyStroke releasedKeyStroke = KeyStroke.getKeyStroke(releasedKey);
        inputMap.put(releasedKeyStroke, releasedKey);
        actionMap.put(releasedKey, releasedAction);
    }

    private class PlayerAction extends AbstractAction implements ActionListener
    {

        private PlayerActionType playerActionType;

        public PlayerAction(String key, PlayerActionType playerActionType)
        {
            super(key);

            this.playerActionType = playerActionType;
        }
        public void actionPerformed(ActionEvent e)
        {
            handleKeyEvent((String)getValue(NAME), playerActionType);
        }

    }

    private void handleKeyEvent(String key, PlayerActionType playerActionType) {
        if (playerActionType.standingStill()) {
            player.removePressedKey(key);
        }
        else {
            player.addPressedKey(key, playerActionType);
        }
    }
}
