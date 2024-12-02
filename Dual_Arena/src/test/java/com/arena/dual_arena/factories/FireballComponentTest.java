package com.arena.dual_arena.factories;

import com.almasb.fxgl.entity.Entity;
import com.arena.dual_arena.components.FireballComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FireballComponentTest {

    private FireballComponent fireballComponent;
    private Entity fireballEntity;

    @BeforeEach
    void setUp() {
        fireballEntity = new Entity();
        fireballComponent = new FireballComponent(100, 100); // Position cible
        fireballEntity.setPosition(50, 50); // Position initiale
        fireballEntity.addComponent(fireballComponent);
    }

    @Test
    void testMovementTowardsTarget() {
        double tpf = 0.016; // Environ 60 FPS (1/60)
        fireballComponent.onUpdate(tpf);

        double x = fireballEntity.getX();
        double y = fireballEntity.getY();
        assertTrue(x > 50 && x <= 100, "La position X devrait se rapprocher de 100.");
        assertTrue(y > 50 && y <= 100, "La position Y devrait se rapprocher de 100.");
    }

    @Test
    void testEntityRemovalWhenCloseToTarget() {
        fireballEntity.setPosition(99, 99);

        double tpf = 0.016;
        fireballComponent.onUpdate(tpf);

        assertTrue(fireballComponent.isRemoved(), "La fireball devrait être supprimée lorsqu'elle atteint la cible.");
    }

    @Test
    void testNoMovementIfAtTarget() {
        fireballEntity.setPosition(100, 100);

        double tpf = 0.016;
        fireballComponent.onUpdate(tpf);

        assertTrue(fireballComponent.isRemoved(), "La fireball devrait être supprimée si elle est déjà à la position cible.");
    }
}

