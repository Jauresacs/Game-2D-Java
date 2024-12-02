package com.arena.dual_arena.models.projectiles;

/**
 * Represents a guided rocket as a type of projectile in the game.
 * Inherits from {@link Projectile} and extends its functionality by adjusting properties like range, speed,
 * and knockback based on provided multipliers. It also has unique characteristics such as guidance and precision.
 */
public class GuidedRocket extends Projectile {

    /**
     * Constructs a {@code GuidedRocket} object with enhanced attributes for range, speed, and knockback.
     * Additionally, sets unique characteristics for guidance and precision.
     *
     * @param rangeMultiplier Multiplier applied to the base range of 1000 units to calculate the guided rocket's effective range.
     * @param speedMultiplier Multiplier applied to the base speed of 400 units to determine the guided rocket's speed.
     * @param knockbackMultiplier Multiplier applied to the base knockback of 1000 units to calculate the guided rocket's knockback force.
     */
    public GuidedRocket(float rangeMultiplier, float speedMultiplier, float knockbackMultiplier) {
        super(rangeMultiplier * 1000f, speedMultiplier * 400f, knockbackMultiplier * 1000f, 1.5f, 1.2f);
    }
}
