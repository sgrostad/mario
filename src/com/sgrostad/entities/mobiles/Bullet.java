package com.sgrostad.entities.mobiles;

import com.sgrostad.Game;
import com.sgrostad.Handler;
import com.sgrostad.entities.Entity;
import com.sgrostad.gfx.Animation;
import com.sgrostad.gfx.Assets;
import com.sgrostad.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Bullet extends MovableEntity {

    private static final int STANDARD_BULLET_SPEED = 400;
    private static final int STANDARD_BULLET_HURT = 3;
    public static final int STANDARD_BULLET_WIDTH = 8;
    public static final int STANDARD_BULLET_HEIGHT = 3;
    public static final int STANDARD_SMOKE_ELEVATION_SPEED = 3;

    private int bulletSmokeSize = 24;
    private float smokeX, smokeY;
    private List<BufferedImage> bulletSprite;
    private Animation bulletSmoke;

    public Bullet(Handler handler, float x, float y, Direction xDir) {
        super(handler, x, y, STANDARD_BULLET_WIDTH, STANDARD_BULLET_HEIGHT);
        smokeX = x;
        smokeY = y;
        this.xDir = xDir;
        bulletSprite = Assets.machineGunBullet;
        if (xDir.goingRight()) {
            xSpeed = STANDARD_BULLET_SPEED;
        }
        else {
            xSpeed = -STANDARD_BULLET_SPEED;
        }
        bulletSmoke = new Animation(1000/Game.FPS, Assets.blackSmoke);
    }

    @Override
    public void tick() {
        bulletSmoke.tick();
        bulletSmokeSize--;
        smokeY -= STANDARD_SMOKE_ELEVATION_SPEED;
        move();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(getBulletImage(),(int)(x - handler.getGameCamera().getxOffset()),
                (int)(y - handler.getGameCamera().getyOffset()), width, height,null);
        if (bulletSmokeSize > 0){
            g.drawImage(bulletSmoke.getCurrentFrame(),(int)(smokeX - handler.getGameCamera().getxOffset()),
                    (int)(smokeY - handler.getGameCamera().getyOffset()), bulletSmokeSize, bulletSmokeSize, null);
        }
    }

    @Override
    public void die() {
        setActive(false);
    }

    @Override
    public void move() {
        float xMove = xSpeed * SECONDS_PAST_PER_MOVE;
        if (checkEntityCollisionAndDoHarm(xMove)){
            die();
            return;
        }
        if (xMove > 0){ //Moving right
            int tileX = (int) Math.floor((x + xMove + bounds.x + bounds.width) / Tile.TILE_WIDTH);
            if (!collisionWithTile(tileX, (int)Math.floor((y + bounds.y) / Tile.TILE_HEIGHT)) &&
                    !collisionWithTile(tileX, (int)Math.floor((y + bounds.y + bounds.height) / Tile.TILE_HEIGHT))){
                x += xMove;
            }
            else { //crashed in tile
                die();
            }
        }
        else if (xMove < 0) { //Moving left
            int tileX = (int) Math.floor((x + xMove + bounds.x) / Tile.TILE_WIDTH);
            if (!collisionWithTile(tileX, (int)Math.floor((y + bounds.y) / Tile.TILE_HEIGHT)) &&
                    !collisionWithTile(tileX, (int)Math.floor((y + bounds.y + bounds.height) / Tile.TILE_HEIGHT))){
                x += xMove;
            }
            else { //crashed in tile
                die();
            }
        }
    }

    private boolean checkEntityCollisionAndDoHarm(float xOffset){
        for(Entity e : handler.getWorld().getEntityManager().getEntities()){
            if (e.equals(this))
                continue;
            if (e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, 0))){
                e.hurt(STANDARD_BULLET_HURT);
                return true;
            }
        }
        return false;
    }

    private BufferedImage getBulletImage(){
        if (xDir.goingRight()){
            return bulletSprite.get(1);
        }
        return bulletSprite.get(0);
    }
}
