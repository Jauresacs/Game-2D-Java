package com.arena.dual_arena.models.weapons;

/**
 * Represents a rifle weapon in the game. This class defines the specific characteristics of a rifle such as
 * damage, range, projectile types, and firing dynamics.
 * Inherits from {@link Weapon} to leverage the general weapon functionalities.
 */
public class Rifle extends Weapon {
    /**
     * Constructs a {@code Rifle} object with predefined attributes tailored for a rifle weapon.
     * Initializes a new rifle with specific damage output, range, projectile types, and firing mechanics.
     */
    public Rifle() {
        super(300f, 1000f, "bullet", "reversed-bullet", 1f, 1f, 0.7f);
    }
}
