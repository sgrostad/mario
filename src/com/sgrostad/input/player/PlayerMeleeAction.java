package com.sgrostad.input.player;

import com.sgrostad.Handler;

public class PlayerMeleeAction extends PlayerActions {

    private final String MELEE_ATTACK_KEY = "F";

    private long lastAttackTimer, attackCoolDown = 800, attackTimer = attackCoolDown;

    public PlayerMeleeAction(Handler handler) {
        super(handler);

    }

    @Override
    public void tick() {
        if (attackTimer < attackCoolDown) {
            attackTimer += System.currentTimeMillis() - lastAttackTimer;
            lastAttackTimer = System.currentTimeMillis();
        }
    }

    @Override
    protected void addActions() {
        addAction(MELEE_ATTACK_KEY);
    }

    @Override
    protected void handleKeyEvent(String key, boolean pressed) {
        if (pressed && attackTimer >= attackCoolDown){
            handler.getWorld().getEntityManager().getPlayer().meleeAttack();
            attackTimer = 0;
        }
    }
}
