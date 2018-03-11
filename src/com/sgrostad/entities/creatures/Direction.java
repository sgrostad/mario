package com.sgrostad.entities.creatures;

public enum Direction {
    RIGHT, LEFT, STILL, JUMP;

    public boolean goingRight(){
        if (this == RIGHT){
            return true;
        }
        return false;
    }

    public boolean goingLeft(){
        if (this == LEFT){
            return true;
        }
        return false;
    }

    public boolean standingStill(){
        if (this == STILL){
            return true;
        }
        return false;
    }

    public boolean jumping(){
        if (this == JUMP){
            return true;
        }
        return false;
    }
}
