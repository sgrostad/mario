package com.sgrostad.entities.creatures;

public enum Direction {
    RIGHT, LEFT, STILL;

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

    public Direction opposite(){
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

    public static Direction keyToDirection(String key){
        switch (key){
            case "RIGHT":
                return RIGHT;
            case "LEFT":
                return LEFT;
            default:
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
            default:
                return "Wrong in enum toString";
        }
    }
}
