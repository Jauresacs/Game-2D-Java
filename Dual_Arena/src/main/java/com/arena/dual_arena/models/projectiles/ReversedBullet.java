package com.arena.dual_arena.models.projectiles;

/**
 * Represents a reversed bullet, a unique type of projectile in the game that features a negative knockback effect.
 * Inherits from {@link Projectile} to utilize and modify standard projectile properties such as speed and reverse knockback.
 */
public class ReversedBullet extends Projectile {
    /**
     * Constructs a {@code ReversedBullet} object with enhanced attributes for speed and a special reversed (negative) knockback.
     * The reversed knockback effect pushes targets towards the shooter instead of away.
     *
     * @param speedMultiplier Multiplier applied to the base speed of 500 units to calculate the bullet's speed.
     * @param knockbackMultiplier Multiplier applied to the base knockback to create a reversed knockback effect.
     *                             The base knockback is considered as 800 units, and the multiplier should adjust this value to achieve the desired effect.
     */
    public ReversedBullet(float speedMultiplier, float knockbackMultiplier) {
        super(speedMultiplier * 500f, knockbackMultiplier * -800f);
    }
}
