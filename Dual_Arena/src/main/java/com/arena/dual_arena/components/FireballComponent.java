package com.arena.dual_arena.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

public class FireballComponent extends Component {

    private final double targetX;
    private final double targetY;
    private final int speed=300;
    private boolean removed = false;

    public FireballComponent(double targetX, double targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }

    @Override
    public void onUpdate(double tpf) {
        double dx = targetX - entity.getX();
        double dy = targetY - entity.getY();

        // Normalize the vector
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length > 20) {
            dx /= length;
            dy /= length;
        }
        else{
            FXGL.getGameWorld().removeEntity(entity);
            removed = true;
        }
        entity.translate(dx * speed * tpf, dy * speed * tpf);
    }

    public boolean isRemoved() {
        return removed;
    }
}
