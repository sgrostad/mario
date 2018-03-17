package com.sgrostad.input.player;

import com.sgrostad.Handler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class PlayerActions {
    protected static final String PRESSED = "pressed ";
    protected static final String RELEASED = "released ";

    protected JComponent component;
    protected Handler handler;

    public PlayerActions(Handler handler) {
        this.handler = handler;
        initActions();
    }

    protected abstract void addActions();
    protected abstract void handleKeyEvent(String key, boolean pressed);

    public void tick(){
        //Standard = do nothing
    }

    private void initActions(){
        component = new JLabel();
        addActions();
        handler.getGame().getFrame().add(component);
    }


    protected void addAction(String keyStroke)
    {
        int offset = keyStroke.lastIndexOf(" ");
        String key = offset == -1 ? keyStroke :  keyStroke.substring( offset + 1 );
        String modifiers = keyStroke.replace(key, "");

        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();

        Action pressedAction = new PlayerAction(key, true);
        String pressedKey = modifiers + PRESSED + key;
        KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(pressedKey);
        inputMap.put(pressedKeyStroke, pressedKey);
        actionMap.put(pressedKey, pressedAction);

        Action releasedAction = new PlayerAction(key, false);
        String releasedKey = modifiers + RELEASED + key;
        KeyStroke releasedKeyStroke = KeyStroke.getKeyStroke(releasedKey);
        inputMap.put(releasedKeyStroke, releasedKey);
        actionMap.put(releasedKey, releasedAction);
    }

    private class PlayerAction extends AbstractAction implements ActionListener {

        private boolean pressed;

        public PlayerAction(String key, boolean pressed) {
            super(key);
            this.pressed = pressed;
        }
        public void actionPerformed(ActionEvent e)
        {
            handleKeyEvent((String)getValue(NAME), pressed);
        }

    }

}
