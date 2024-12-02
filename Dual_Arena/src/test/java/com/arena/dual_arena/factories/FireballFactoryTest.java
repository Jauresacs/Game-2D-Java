package com.arena.dual_arena.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.BoundingShape;
import com.arena.dual_arena.components.FireballComponent;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FireballFactoryTest {

    private HazardFactory factory;

    @BeforeEach
    void setUp() {
        factory = new HazardFactory();
    }

    @Test
    void testCreateFireball() {
        SpawnData spawnData = new SpawnData(100, 200)
                .put("oppositeX", 300)
                .put("oppositeY", 400);

        Entity fireball = factory.createFireball(spawnData);

        assertEquals(100, fireball.getX(), "La position X de la fireball est incorrecte.");
        assertEquals(200, fireball.getY(), "La position Y de la fireball est incorrecte.");

        assertTrue(fireball.hasComponent(CollidableComponent.class), "Fireball devrait avoir un composant collidable.");
        assertTrue(fireball.hasComponent(FireballComponent.class), "Fireball devrait avoir le composant FireballComponent.");

        HitBox hitBox = fireball.getBoundingBoxComponent().hitBoxesProperty().get(0);
        assertEquals(10 * 2, hitBox.getWidth(), "La largeur de la HitBox est incorrecte.");
        assertEquals(13 * 2, hitBox.getHeight(), "La hauteur de la HitBox est incorrecte.");

        double expectedAngle = Math.toDegrees(Math.atan2(400 - 200, 300 - 100));
        assertEquals(expectedAngle, fireball.getRotation(), "L'angle de rotation est incorrect.");
    }
}
