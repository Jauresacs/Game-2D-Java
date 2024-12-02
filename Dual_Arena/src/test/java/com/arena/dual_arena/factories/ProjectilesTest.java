package com.arena.dual_arena.factories;

import com.arena.dual_arena.models.projectiles.Bullet;
import com.arena.dual_arena.models.projectiles.GuidedRocket;
import com.arena.dual_arena.models.projectiles.ReversedBullet;
import com.arena.dual_arena.models.projectiles.ReversedRocket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectilesTest {

    @Test
    void testBulletInitialization() {
        Bullet bullet = new Bullet(2.0f, 1.5f); // Multiplieurs de test

        assertEquals(500f * 2.0f, bullet.getSpeed(), "La vitesse est incorrecte.");
        assertEquals(500f * 1.5f, bullet.getKnockback(), "Le recul est incorrect.");
        assertEquals(0f, bullet.getRange(), "La portée par défaut devrait être 0f.");
        assertEquals(1f, bullet.getExplosionRadiusMultiplier(), "Le multiplicateur de rayon d'explosion par défaut devrait être 1f.");
        assertEquals(1f, bullet.getExplosionKnockbackFalloff(), "Le multiplicateur de recul d'explosion par défaut devrait être 1f.");
    }

    @Test
    void testReversedBulletInitialization() {
        ReversedBullet reversedBullet = new ReversedBullet(1.2f, 0.8f); // Multiplieurs de test

        assertEquals(500f * 1.2f, reversedBullet.getSpeed(), "La vitesse est incorrecte.");
        assertEquals(-800f * 0.8f, reversedBullet.getKnockback(), "Le recul est incorrect.");
        assertEquals(0f, reversedBullet.getRange(), "La portée par défaut devrait être 0f.");
        assertEquals(1f, reversedBullet.getExplosionRadiusMultiplier(), "Le multiplicateur de rayon d'explosion par défaut devrait être 1f.");
        assertEquals(1f, reversedBullet.getExplosionKnockbackFalloff(), "Le multiplicateur de recul d'explosion par défaut devrait être 1f.");
    }
    @Test
    void testGuidedRocketInitialization() {
        GuidedRocket guidedRocket = new GuidedRocket(1.0f, 1.5f, 2.0f); // Multiplieurs de test

        assertEquals(1000f * 1.0f, guidedRocket.getRange(), "La portée est incorrecte.");
        assertEquals(400f * 1.5f, guidedRocket.getSpeed(), "La vitesse est incorrecte.");
        assertEquals(1000f * 2.0f, guidedRocket.getKnockback(), "Le recul est incorrect.");
        assertEquals(1.5f, guidedRocket.getExplosionRadiusMultiplier(), "Le multiplicateur de rayon d'explosion est incorrect.");
        assertEquals(1.2f, guidedRocket.getExplosionKnockbackFalloff(), "Le multiplicateur de recul d'explosion est incorrect.");
    }

    @Test
    void testReversedRocketInitialization() {
        ReversedRocket reversedRocket = new ReversedRocket(1.0f, 1.2f, 1.5f); // Multiplieurs de test

        assertEquals(1000f * 1.0f, reversedRocket.getRange(), "La portée est incorrecte.");
        assertEquals(300f * 1.2f, reversedRocket.getSpeed(), "La vitesse est incorrecte.");
        assertEquals(2000f * 1.5f, reversedRocket.getKnockback(), "Le recul est incorrect.");
        assertEquals(2f, reversedRocket.getExplosionRadiusMultiplier(), "Le multiplicateur de rayon d'explosion est incorrect.");
        assertEquals(1.5f, reversedRocket.getExplosionKnockbackFalloff(), "Le multiplicateur de recul d'explosion est incorrect.");
    }
}
