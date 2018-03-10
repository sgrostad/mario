package com.sgrostad.input;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KeyBinderCreator {

    public static void addKeyBinding(JComponent component, int keyCode, boolean onKeyRelease, String id, ActionListener actionListener){
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0, onKeyRelease), id);
        actionMap.put(id, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionListener.actionPerformed(e);
            }
        });
    }

}
/*KeyBinderCreator.addKeyBinding(component, KeyEvent.VK_DOWN, false, "DOWN",
                (evt) -> {this.setyMove(1);});
        KeyBinderCreator.addKeyBinding(component, KeyEvent.VK_UP, false, "UP",
                (evt) -> {this.setyMove(-1);});
        KeyBinderCreator.addKeyBinding(component, KeyEvent.VK_RIGHT, false, "RIGHT",
                (evt) -> {this.setxMove(1);});
        KeyBinderCreator.addKeyBinding(component, KeyEvent.VK_LEFT,false, "LEFT",
                (evt) -> {this.setxMove(-1);});*/
