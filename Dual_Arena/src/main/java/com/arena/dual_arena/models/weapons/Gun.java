package com.arena.dual_arena.models.weapons;

/**
 * Represents a gun weapon in the game. This class specifies the characteristics of a gun such as
 * damage, range, projectile types, and firing rates.
 * Inherits from {@link Weapon} to utilize the generic weapon functionalities.
 */
public class Gun extends Weapon {
    /**
     * Constructs a {@code Gun} object with predefined attributes for a gun weapon.
     * Initializes a new gun with specific damage output, range, projectile types, and firing mechanics.
     */
    public Gun() {
        super(500f, 1000f, "bullet", "reversed-bullet", 1f, 1.2f, 1.2f);
    }
}
