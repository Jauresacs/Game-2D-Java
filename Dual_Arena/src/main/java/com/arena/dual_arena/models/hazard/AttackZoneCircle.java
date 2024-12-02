package com.arena.dual_arena.models.hazard;

import com.almasb.fxgl.dsl.FXGL;
import com.arena.dual_arena.factories.HazardFactory;

/**
 * Represents an attack zone in the form of a circle. This class is responsible for
 * initiating random attacks within the game area.
 * Extends {@link RandomAttackEvent} to utilize the random attack triggering mechanism.
 */
public class AttackZoneCircle extends RandomAttackEvent {

    /**
     * Constructs an {@code AttackZoneCircle} object with predefined cooldown and initial delay settings,
     * and starts the random attack sequence.
     */
    public AttackZoneCircle() {
        super(2, 1);
        startRandomAttack();
    }

    /**
     * Triggers a random attack by creating a circular hazard zone at a random location within the game boundaries.
     * The size and position of the circle are determined randomly based on the game's width and height.
     */
    @Override
    public void triggerAttackEvent() {
        int width = FXGL.getAppWidth();
        int height = FXGL.getAppHeight();
        int x = random.nextInt(width*18/25) + width*3/25;
        int y = random.nextInt(height*7/14) + height*3/14;
        int radius = 40 + random.nextInt(50);
        HazardFactory.spawnCircleZone(x,y,radius);
    }
}

