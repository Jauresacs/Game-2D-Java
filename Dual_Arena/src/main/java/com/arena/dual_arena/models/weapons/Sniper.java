package com.arena.dual_arena.models.weapons;

/**
 * Represents a sniper rifle weapon in the game. This class defines the sniper-specific characteristics such as
 * high damage, long range, specialized projectile types, and specific firing dynamics.
 * Inherits from {@link Weapon} to utilize common weapon functionalities.
 */
public class Sniper extends Weapon {
    /**
     * Constructs a {@code Sniper} object with predefined attributes that are tailored for a sniper rifle.
     * Initializes a new sniper with high damage output, long range, specific projectile types, and tailored firing mechanics.
     */
    public Sniper() {
        super(1000f, 1000f, "bullet", "reversed-bullet", 1f, 2f, 2f);
    }
}
