package com.sgrostad.entities.statics;

import com.sgrostad.Handler;
import com.sgrostad.entities.Entity;

public abstract class StaticEntity extends Entity{

    public StaticEntity(Handler handler, float x, float y, int width, int height) {
        super(handler, x, y, width, height);
    }

}
