package com.sgrostad.entities.creatures;

import com.sgrostad.Handler;
import com.sgrostad.gfx.Animation;
import com.sgrostad.gfx.Assets;
import com.sgrostad.input.KeyBinderCreator;
import com.sgrostad.inventory.Inventory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Player extends Creature {

    private static final int MILLI_SEC_PER_PLAYER_FRAME = 80;
    private static final String PRESSED = "pressed ";
    private static final String RELEASED = "released ";

    // Animations
    private Animation animationDown, animationUp, animationLeft, animationRight;
    // Attack timer
    private long lastAttackTimer, attackCoolDown = 800, attackTimer = attackCoolDown;
    // Inventory
    private Inventory inventory;
    // Keys
    private JComponent component;
    private Map<String, Point> pressedKeys = new HashMap<String, Point>();


    public Player(Handler handler, float x, float y) {
        super(handler, x, y, Creature.DEFAULT_CREATURE_WIDTH, Creature.DEFAULT_CREATURE_HEIGHT);
        bounds.x = 16;
        bounds.y = 8;
        bounds.width = 32;
        bounds.height = 54;
        animationDown = new Animation(MILLI_SEC_PER_PLAYER_FRAME, Assets.playerDown);
        animationUp = new Animation(MILLI_SEC_PER_PLAYER_FRAME, Assets.playerUp);
        animationLeft = new Animation(MILLI_SEC_PER_PLAYER_FRAME, Assets.playerLeft);
        animationRight = new Animation(MILLI_SEC_PER_PLAYER_FRAME, Assets.playerRight);
        initPlayerKeys();
        inventory = new Inventory(handler);
    }

    private void initPlayerKeys(){
        component = new JPanel();
        int tempSpeed = 4;
        addAction("LEFT", -tempSpeed, 0);
        addAction("RIGHT", tempSpeed, 0);
        addAction("UP", 0, -tempSpeed);
        addAction("DOWN", 0, tempSpeed);
        handler.getGame().getFrame().add(component);
    }

    public void addAction(String keyStroke, int deltaX, int deltaY)
    {
        int offset = keyStroke.lastIndexOf(" ");
        String key = offset == -1 ? keyStroke :  keyStroke.substring( offset + 1 );
        String modifiers = keyStroke.replace(key, "");

        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();

        Action pressedAction = new PlayerAction(key, new Point(deltaX, deltaY));
        String pressedKey = modifiers + PRESSED + key;
        KeyStroke pressedKeyStroke = KeyStroke.getKeyStroke(pressedKey);
        inputMap.put(pressedKeyStroke, pressedKey);
        actionMap.put(pressedKey, pressedAction);

        Action releasedAction = new PlayerAction(key, null);
        String releasedKey = modifiers + RELEASED + key;
        KeyStroke releasedKeyStroke = KeyStroke.getKeyStroke(releasedKey);
        inputMap.put(releasedKeyStroke, releasedKey);
        actionMap.put(releasedKey, releasedAction);
    }

    private class PlayerAction extends AbstractAction implements ActionListener
    {
        private Point moveDelta;

        public PlayerAction(String key, Point moveDelta)
        {
            super(key);

            this.moveDelta = moveDelta;
        }

        public void actionPerformed(ActionEvent e)
        {
            handleKeyEvent((String)getValue(NAME), moveDelta);
        }
    }

    private void handleKeyEvent(String key, Point moveDelta) {
        if (moveDelta == null)
            pressedKeys.remove(key);
        else
            pressedKeys.put(key, moveDelta);
    }

        @Override
    public void tick() {
        //Animation
        animationDown.tick();
        animationUp.tick();
        animationLeft.tick();
        animationRight.tick();
        getInput();
        move();
        handler.getGameCamera().centerOnEntity(this);
        // Attacks
        checkAttacks();
        inventory.tick();
    }


    private void checkAttacks(){
        /*
        attackTimer += System.currentTimeMillis() - lastAttackTimer;
        lastAttackTimer = System.currentTimeMillis();
        if (attackTimer < attackCoolDown){
            return;
        }
        Rectangle cb = getCollisionBounds(0,0);
        Rectangle aRange = new Rectangle();
        int aRangeSize = 20;
        aRange.width = aRangeSize;
        aRange.height = aRangeSize;
        if (handler.getKeyManager().aUp){
            aRange.x = cb.x + cb.width / 2 - aRangeSize / 2;
            aRange.y = cb.y - aRangeSize;
        }else if (handler.getKeyManager().aDown){
            aRange.x = cb.x + cb.width / 2 - aRangeSize / 2;
            aRange.y = cb.y + cb.height;
        }else if (handler.getKeyManager().aLeft){
            aRange.x = cb.x - aRangeSize;
            aRange.y = cb.y + cb.height / 2 - aRangeSize / 2;
        }else if (handler.getKeyManager().aRight){
            aRange.x = cb.x + cb.width;
            aRange.y = cb.y + cb.height / 2 - aRangeSize / 2;
        }else {
            return;
        }
        for (Entity e : handler.getWorld().getEntityManager().getEntities()){
            if (e.equals(this)){
                continue;
            }
            if (e.getCollisionBounds(0,0).intersects(aRange)){
                e.hurt(1);
            }
        }
        attackTimer = 0;*/
    }
    private void getInput(){
        yMove = 0;
        xMove = 0;
        for (Point delta : pressedKeys.values())
        {
            xMove += delta.x;
            yMove += delta.y;
        }
    }

    @Override
    public void render(Graphics g) {
        /*g.setColor(Color.red);
        g.fillRect((int)(x + bounds.x - handler.getGameCamera().getxOffset()),
                (int)(y + bounds.y - handler.getGameCamera().getyOffset()), bounds.width, bounds.height);
        */
        g.drawImage(getCurrentAnimationFrame(),(int)(x - handler.getGameCamera().getxOffset()),
                (int)(y - handler.getGameCamera().getyOffset()), width, height,null);
        inventory.render(g);
    }

    @Override
    public void die() {
        System.out.println("You died!");
    }

    private BufferedImage getCurrentAnimationFrame(){
        if (xMove < 0){
            return animationLeft.getCurrentFrame();
        }
        else if (xMove > 0){
            return animationRight.getCurrentFrame();
        }
        else if (yMove < 0){
            return animationUp.getCurrentFrame();
        }
        else if (yMove > 0){
            return animationDown.getCurrentFrame();
        }
        else {
            return animationDown.getFirstFrame();
        }
    }

    //GETTERS SETTERS


    public Inventory getInventory() {
        return inventory;
    }
}
