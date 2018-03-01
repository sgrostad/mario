package com.sgrostad.states;

import com.sgrostad.Game;
import com.sgrostad.Handler;

import java.awt.*;

public abstract class State {

    private static State currentState = null;
    public static void setCurrentState(State state){
        currentState = state;
    }

    public static State getCurrentState(){
        return currentState;
    }

    //Class

    protected Handler handler;

    public State(Handler handler) {
        this.handler = handler;
    }

    public abstract void tick();

    public abstract void render(Graphics g);
}
