package com.arena.dual_arena.models.weapons;

/**
 * Represents a rocket launcher weapon in the game. This class details the unique characteristics of a rocket launcher such as
 * high damage, long range, specialized projectile types, and consistent firing dynamics.
 * Inherits from {@link Weapon} to utilize standardized weapon functionalities.
 */
public class RocketLauncher extends Weapon {
    /**
     * Constructs a {@code RocketLauncher} object with predefined attributes specific to a rocket launcher weapon.
     * Initializes a new rocket launcher with very high damage output, extensive range, specific projectile types, and uniform firing mechanics.
     */
    public RocketLauncher() {
        super(2000f, 2000f, "rocket", "guided-rocket", 1f, 1f, 1f);
    }
}
