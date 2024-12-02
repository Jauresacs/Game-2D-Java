package com.arena.dual_arena.models.hazard;

import com.almasb.fxgl.dsl.FXGL;
import com.arena.dual_arena.factories.HazardFactory;

/**
 * Represents a random event where fireballs appear on one edge of the screen
 * and move towards a random point on the opposite edge.
 *
 * <p>Inherits from {@link RandomAttackEvent} and sets a 4-second interval
 * with a 10% probability to trigger the attack.</p>
 */
public class AttackFireball extends RandomAttackEvent {

    /**
     * Initializes a random fireball attack with predefined parameters.
     */
    public AttackFireball() {
        super(4, 0.1); // Interval: 4s, Probability: 10%
        startRandomAttack();
    }

    /**
     * Triggers a fireball that appears on a random edge of the screen
     * and moves toward a position on the opposite edge.
     *
     * <p>The start and target coordinates are randomly generated based
     * on the screen size.</p>
     */
    @Override
    public void triggerAttackEvent() {
        int width = FXGL.getAppWidth();
        int height = FXGL.getAppHeight();
        int side = random.nextInt(4); // Random edge
        int x, y, oppositeX, oppositeY;

        switch (side) {
            case 0: // Left edge
                x = 0;
                y = random.nextInt(height);
                oppositeX = width;
                oppositeY = (y < height / 2) ? random.nextInt(height - y) + y : y - random.nextInt(y);
                break;
            case 1: // Right edge
                x = width;
                y = random.nextInt(height);
                oppositeX = 0;
                oppositeY = (y < height / 2) ? random.nextInt(height - y) + y : y - random.nextInt(y);
                break;
            case 2: // Top edge
                y = 0;
                x = random.nextInt(width);
                oppositeY = height;
                oppositeX = (x < width / 2) ? random.nextInt(width - x) + x : x - random.nextInt(x);
                break;
            case 3: // Bottom edge
                y = height;
                x = random.nextInt(width);
                oppositeY = 0;
                oppositeX = (x < width / 2) ? random.nextInt(width - x) + x : x - random.nextInt(x);
                break;
            default:
                x = width / 2;
                y = height;
                oppositeX = width / 2;
                oppositeY = 0;
                break;
        }

        // Create and launch the fireball
        HazardFactory.spawnFireball(x, y, oppositeX, oppositeY);
    }
}
