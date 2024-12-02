package com.arena.dual_arena.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.arena.dual_arena.FXGLTestBase;
import com.arena.dual_arena.types.EntityType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static org.junit.jupiter.api.Assertions.*;

public class HazardFactoryTest extends FXGLTestBase {

    private static HazardFactory hazardFactory;

    @BeforeAll
    static void setUp() {
        hazardFactory = new HazardFactory();
        FXGL.getGameWorld().addEntityFactory(hazardFactory);
    }

    @Test
    void testCreateAttackZone() {
        SpawnData data = new SpawnData(100, 100).put("radius", 50);
        Entity attackZone = hazardFactory.createAttackZone(data);

        assertNotNull(attackZone, "La zone d'attaque ne devrait pas être null.");
        assertEquals(EntityType.HAZARD, attackZone.getType(), "Le type d'entité devrait être HAZARD.");

        Circle circle = (Circle) attackZone.getViewComponent().getChildren().get(0);
        assertEquals(50, circle.getRadius(), "Le rayon du cercle devrait être 50.");
        assertEquals(Color.rgb(255, 160, 0, 0.4), circle.getFill(), "La couleur du cercle devrait correspondre.");
    }

    @Test
    void testSpawnCircleZone() {
        Entity attackZone = HazardFactory.spawnCircleZone(200, 200, 75);

        assertNotNull(attackZone, "La zone d'attaque circulaire devrait être créée.");
        assertTrue(getGameWorld().getEntitiesByType(EntityType.HAZARD).contains(attackZone),
                "La zone d'attaque devrait être ajoutée au GameWorld.");
    }

    @Test
    void testCreateCircleExplosion() {
        HazardFactory.createCircleExplosion(150, 150, 60);

        var explosions = getGameWorld().getEntitiesByType(EntityType.EXPLOSION);
        assertEquals(1, explosions.size(), "Une seule explosion devrait être créée.");

        Entity explosion = explosions.get(0);

        assertEquals(150 , explosion.getX(), "L'explosion devrait être positionnée correctement en X.");
        assertEquals(150, explosion.getY(), "L'explosion devrait être positionnée correctement en Y.");
    }

    @Test
    void testAttackZoneRemoval() {
        SpawnData data = new SpawnData(100, 100).put("radius", 50);
        Entity attackZone = hazardFactory.createAttackZone(data);

        FXGL.getGameTimer().runOnceAfter(() -> {
            assertFalse(getGameWorld().getEntities().contains(attackZone),
                    "La zone d'attaque devrait être retirée après la durée spécifiée.");
        }, Duration.seconds(3.1)); // Ajout d'un léger décalage pour s'assurer que l'action a eu lieu
    }
}

