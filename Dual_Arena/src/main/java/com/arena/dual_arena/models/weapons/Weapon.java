package com.arena.dual_arena.models.weapons;

/**
 * Abstract class that defines the common properties and functionalities of weapons in the game.
 * This class serves as a base for specific weapon types, providing standard attributes such as
 * fire rates, projectile types, and performance multipliers.
 */
public abstract class Weapon {
    protected float primaryFireRate;
    protected float secondaryFireRate;
    protected String primaryProjectileType;
    protected String secondaryProjectileType;
    protected float rangeMultiplier;
    protected float speedMultiplier;
    protected float knockbackMultiplier;

    /**
     * Constructs a {@code Weapon} object with specified attributes defining the weapon's behavior and properties.
     *
     * @param primaryFireRate The rate at which the primary mode fires projectiles (shots per second).
     * @param secondaryFireRate The rate at which the secondary mode fires projectiles (shots per second).
     * @param primaryProjectileType The type of projectile used in the primary firing mode.
     * @param secondaryProjectileType The type of projectile used in the secondary firing mode.
     * @param rangeMultiplier Multiplier affecting the range of the weapon's projectiles.
     * @param speedMultiplier Multiplier affecting the speed of the weapon's projectiles.
     * @param knockbackMultiplier Multiplier affecting the knockback effect of the weapon's projectiles on hit.
     */
    protected Weapon(float primaryFireRate, float secondaryFireRate, String primaryProjectileType, String secondaryProjectileType, float rangeMultiplier, float speedMultiplier, float knockbackMultiplier) {
        this.primaryFireRate = primaryFireRate;
        this.secondaryFireRate = secondaryFireRate;
        this.primaryProjectileType = primaryProjectileType;
        this.secondaryProjectileType = secondaryProjectileType;
        this.rangeMultiplier = rangeMultiplier;
        this.speedMultiplier = speedMultiplier;
        this.knockbackMultiplier = knockbackMultiplier;
    }

    /**
     * Returns the fire rate of the primary weapon mode.
     * @return the primary fire rate in shots per second
     */
    public float getPrimaryFireRate() {
        return primaryFireRate;
    }

    /**
     * Returns the fire rate of the secondary weapon mode.
     * @return the secondary fire rate in shots per second
     */
    public float getSecondaryFireRate() {
        return secondaryFireRate;
    }

    /**
     * Returns the type of projectile used in the primary firing mode.
     * @return the primary projectile type
     */
    public String getPrimaryProjectileType() {
        return primaryProjectileType;
    }

    /**
     * Returns the type of projectile used in the secondary firing mode.
     * @return the secondary projectile type
     */
    public String getSecondaryProjectileType() {
        return secondaryProjectileType;
    }

    /**
     * Returns the multiplier for the range of the weapon's projectiles.
     * @return the range multiplier
     */
    public float getRangeMultiplier() {
        return rangeMultiplier;
    }

    /**
     * Returns the multiplier for the speed of the weapon's projectiles.
     * @return the speed multiplier
     */
    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    /**
     * Returns the multiplier affecting the knockback effect of the weapon's projectiles upon hitting a target.
     * @return the knockback multiplier
     */
    public float getKnockbackMultiplier() {
        return knockbackMultiplier;
    }
}
