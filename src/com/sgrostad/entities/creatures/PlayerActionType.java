package com.sgrostad.entities.creatures;

public enum PlayerActionType {
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

    public PlayerActionType opposite(){
        if (this == RIGHT){
            return LEFT;
        }
        else if (this == LEFT){
            return RIGHT;
        }
        else {
            System.err.println(this.toString() + " does not have an opposite!");
            return STILL;
        }
    }

    @Override
    public String toString() {
        switch (this){
            case STILL:
                return "STILL";
            case LEFT:
                return "LEFT";
            case RIGHT:
                return "RIGHT";
            case JUMP:
                return "JUMP";
            default:
                return "Wrong in enum toString";
        }
    }
}
