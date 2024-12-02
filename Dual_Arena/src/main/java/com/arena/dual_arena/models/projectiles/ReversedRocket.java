package com.arena.dual_arena.models.projectiles;

/**
 * Represents a reversed rocket, a type of projectile in the game that features an exceptionally high reverse knockback effect.
 * Inherits from {@link Projectile} and enhances certain attributes like range, speed, and notably, a reversed knockback force.
 * This rocket is designed to impact strategic gameplay by pulling targets towards the point of explosion rather than dispersing them.
 */
public class ReversedRocket extends Projectile {

    /**
     * Constructs a {@code ReversedRocket} with enhanced attributes for range, speed, and a special reversed knockback effect.
     * The reverse knockback effect is designed to pull targets towards the explosion, contrasting traditional explosive effects.
     *
     * @param rangeMultiplier Multiplier applied to the base range of 1000 units to calculate the rocket's effective range.
     * @param speedMultiplier Multiplier applied to the base speed of 300 units to determine the rocket's travel speed.
     * @param knockbackMultiplier Multiplier applied to achieve a reversed knockback force, significantly higher than standard projectiles, using a base of 2000 units.
     */
    public ReversedRocket(float rangeMultiplier, float speedMultiplier, float knockbackMultiplier) {
        super(rangeMultiplier * 1000f, speedMultiplier * 300f, knockbackMultiplier * 2000f, 2f, 1.5f);
    }
}
