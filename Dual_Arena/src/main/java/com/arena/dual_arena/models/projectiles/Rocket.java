package com.arena.dual_arena.models.projectiles;

/**
 * Represents a rocket, a type of projectile in the game known for its high impact and explosive properties.
 * Inherits from {@link Projectile} and enhances attributes such as range, speed, and knockback force to create a powerful explosive effect.
 * Designed for high damage output and area effect, this projectile is ideal for targeting multiple enemies or structures.
 */
public class Rocket extends Projectile {

    /**
     * Constructs a {@code Rocket} with specified enhancements for range, speed, and knockback force.
     * This configuration allows the rocket to effectively reach distant targets with significant destructive power.
     *
     * @param rangeMultiplier Multiplier applied to the base range of 1000 units to calculate the rocket's effective range.
     * @param speedMultiplier Multiplier applied to the base speed of 300 units to determine the rocket's travel speed.
     * @param knockbackMultiplier Multiplier applied to the base knockback of 2000 units to enhance the impact force upon detonation.
     */
    public Rocket(float rangeMultiplier, float speedMultiplier, float knockbackMultiplier) {
        super(rangeMultiplier * 1000f, speedMultiplier * 300f, knockbackMultiplier * 2000f, 2f, 1.5f);
    }
}
