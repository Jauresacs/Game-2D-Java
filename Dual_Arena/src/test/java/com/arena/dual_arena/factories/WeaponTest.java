package com.arena.dual_arena.factories;


import com.arena.dual_arena.models.weapons.Gun;
import com.arena.dual_arena.models.weapons.Rifle;
import com.arena.dual_arena.models.weapons.RocketLauncher;
import com.arena.dual_arena.models.weapons.Sniper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {

    @Test
    void testSniperInitialization() {
        Sniper sniper = new Sniper();

        assertEquals(1000f, sniper.getPrimaryFireRate(), "Le taux de tir principal devrait être 1000f.");
        assertEquals(1000f, sniper.getSecondaryFireRate(), "Le taux de tir secondaire devrait être 1000f.");
        assertEquals("bullet", sniper.getPrimaryProjectileType(), "Le type de projectile principal devrait être 'bullet'.");
        assertEquals("reversed-bullet", sniper.getSecondaryProjectileType(), "Le type de projectile secondaire devrait être 'reversed-bullet'.");
        assertEquals(1f, sniper.getRangeMultiplier(), "Le multiplicateur de portée devrait être 1f.");
        assertEquals(2f, sniper.getSpeedMultiplier(), "Le multiplicateur de vitesse devrait être 2f.");
        assertEquals(2f, sniper.getKnockbackMultiplier(), "Le multiplicateur de recul devrait être 2f.");
    }
    @Test
    void testRocketLauncherInitialization() {
        RocketLauncher rocketLauncher = new RocketLauncher();

        assertEquals(2000f, rocketLauncher.getPrimaryFireRate(), "Le taux de tir principal devrait être 2000f.");
        assertEquals(2000f, rocketLauncher.getSecondaryFireRate(), "Le taux de tir secondaire devrait être 2000f.");
        assertEquals("rocket", rocketLauncher.getPrimaryProjectileType(), "Le type de projectile principal devrait être 'rocket'.");
        assertEquals("guided-rocket", rocketLauncher.getSecondaryProjectileType(), "Le type de projectile secondaire devrait être 'guided-rocket'.");
        assertEquals(1f, rocketLauncher.getRangeMultiplier(), "Le multiplicateur de portée devrait être 1f.");
        assertEquals(1f, rocketLauncher.getSpeedMultiplier(), "Le multiplicateur de vitesse devrait être 1f.");
        assertEquals(1f, rocketLauncher.getKnockbackMultiplier(), "Le multiplicateur de recul devrait être 1f.");
    }

    @Test
    void testRifleInitialization() {
        Rifle rifle = new Rifle();

        assertEquals(300f, rifle.getPrimaryFireRate(), "Primary fire rate should be 300f.");
        assertEquals(1000f, rifle.getSecondaryFireRate(), "Secondary fire rate should be 1000f.");
        assertEquals("bullet", rifle.getPrimaryProjectileType(), "Primary projectile type should be 'bullet'.");
        assertEquals("reversed-bullet", rifle.getSecondaryProjectileType(), "Secondary projectile type should be 'reversed-bullet'.");
        assertEquals(1f, rifle.getRangeMultiplier(), "Range multiplier should be 1f.");
        assertEquals(1f, rifle.getSpeedMultiplier(), "Speed multiplier should be 1f.");
        assertEquals(0.7f, rifle.getKnockbackMultiplier(), "Knockback multiplier should be 0.7f.");
    }
    @Test
    void testGunInitialization() {
        Gun gun = new Gun();

        assertEquals(500f, gun.getPrimaryFireRate(), "Le taux de tir principal devrait être 500f.");
        assertEquals(1000f, gun.getSecondaryFireRate(), "Le taux de tir secondaire devrait être 1000f.");
        assertEquals("bullet", gun.getPrimaryProjectileType(), "Le type de projectile principal devrait être 'bullet'.");
        assertEquals("reversed-bullet", gun.getSecondaryProjectileType(), "Le type de projectile secondaire devrait être 'reversed-bullet'.");
        assertEquals(1f, gun.getRangeMultiplier(), "Le multiplicateur de portée devrait être 1f.");
        assertEquals(1.2f, gun.getSpeedMultiplier(), "Le multiplicateur de vitesse devrait être 1.2f.");
        assertEquals(1.2f, gun.getKnockbackMultiplier(), "Le multiplicateur de recul devrait être 1.2f.");
    }
}
