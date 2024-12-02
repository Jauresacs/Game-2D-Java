package com.arena.dual_arena.models.projectiles;

/**
 * Represents a bullet as a type of projectile in the game.
 * Inherits from {@link Projectile} to utilize and modify standard projectile properties such as speed and knockback.
 */
public class Bullet extends Projectile {
    /**
     * Constructs a {@code Bullet} object with enhanced attributes for speed and knockback.
     * The bullet's speed and knockback force are derived by multiplying base values by specified multipliers.
     *
     * @param speedMultiplier Multiplier applied to the base speed of 500 units to calculate the bullet's speed.
     * @param knockbackMultiplier Multiplier applied to the base knockback of 500 units to determine the bullet's knockback force.
     */
    public Bullet(float speedMultiplier, float knockbackMultiplier) {
        super(speedMultiplier * 500f, knockbackMultiplier * 500f);
    }
}
