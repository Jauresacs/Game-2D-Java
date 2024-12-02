package com.arena.dual_arena.models.projectiles;

/**
 * Abstract class representing the base functionality of a projectile in the game.
 * Defines common properties such as range, speed, knockback, and explosion characteristics.
 */
public abstract class Projectile {
    protected float range = 0f;
    protected float speed;
    protected float knockback;
    protected float explosionRadiusMultiplier = 1f;
    protected float explosionKnockbackFalloff = 1f;

    /**
     * Constructs a projectile with specified speed and knockback, without explosion modifiers.
     *
     * @param speed Speed at which the projectile travels.
     * @param knockback Knockback force the projectile imparts on hitting a target.
     */
    protected Projectile(float speed, float knockback) {
        this.speed = speed;
        this.knockback = knockback;
    }

    /**
     * Constructs a projectile with specified range, speed, knockback, and explosion characteristics.
     *
     * @param range Maximum distance the projectile can travel before dissipating.
     * @param speed Speed at which the projectile travels.
     * @param knockback Knockback force the projectile imparts on hitting a target.
     * @param explosionRadiusMultiplier Multiplier for the explosion radius.
     * @param explosionKnockbackFalloff Rate at which the explosion's knockback effect decreases with distance.
     */
    protected Projectile(float range, float speed, float knockback, float explosionRadiusMultiplier, float explosionKnockbackFalloff) {
        this.range = range;
        this.speed = speed;
        this.knockback = knockback;
        this.explosionRadiusMultiplier = explosionRadiusMultiplier;
        this.explosionKnockbackFalloff = explosionKnockbackFalloff;
    }

    /**
     * Returns the range of the projectile.
     * @return The effective range of the projectile in game units.
     */
    public float getRange() {
        return range;
    }

    /**
     * Returns the speed of the projectile.
     * @return The speed of the projectile in game units per second.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Returns the knockback force of the projectile.
     * @return The knockback force imparted by the projectile upon impact.
     */
    public float getKnockback() {
        return knockback;
    }

    /**
     * Returns the explosion radius multiplier.
     * @return The multiplier applied to the base radius of the projectile's explosion.
     */
    public float getExplosionRadiusMultiplier() {
        return explosionRadiusMultiplier;
    }

    /**
     * Returns the falloff rate of the explosion's knockback effect.
     * @return The rate at which knockback decreases with distance from the center of the explosion.
     */
    public float getExplosionKnockbackFalloff() {
        return explosionKnockbackFalloff;
    }
}
