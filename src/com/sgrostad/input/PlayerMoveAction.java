package com.sgrostad.input;

import com.sgrostad.entities.creatures.Player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerMoveAction extends AbstractAction{

    private Player player;
    private int deltaX, deltaY;

    public PlayerMoveAction(Player player, String name, int deltaX, int deltaY) {
        super(name);
        this.player = player;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Move: x = " + deltaX + ", y = " + deltaY);
    }
}
