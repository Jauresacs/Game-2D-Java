package com.arena.dual_arena.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.arena.dual_arena.FXGLTestBase;
import com.arena.dual_arena.types.EntityType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArenaFactoryTest extends FXGLTestBase {

    @BeforeAll
    static void setUp() {
        FXGL.getGameWorld().addEntityFactory(new ArenaFactory());
    }

    @Test
    void testSpawnWater() {
        SpawnData data = new SpawnData(100, 150)
                .put("width", 300)  // Largeur de l'eau
                .put("height", 200); // Hauteur de l'eau

        Entity water = FXGL.spawn("water", data);

        assertNotNull(water, "L'entité 'water' ne devrait pas être nulle.");
        assertEquals(EntityType.WATER, water.getType(), "Le type d'entité devrait être WATER.");

        assertTrue(water.hasComponent(CollidableComponent.class), "L'entité 'water' devrait être collidable.");

        assertEquals(100, water.getX(), "La position X de l'entité 'water' devrait être correcte.");
        assertEquals(150, water.getY(), "La position Y de l'entité 'water' devrait être correcte.");
        assertNotNull(water.getBoundingBoxComponent(), "L'entité 'water' devrait avoir une hitbox.");
    }

    @Test
    void testWaterScaling() {
        int appWidth = 1024;
        int appHeight = 576;

        SpawnData data = new SpawnData(0, 0)
                .put("width", 800) // Largeur de la carte
                .put("height", 448); // Hauteur de la carte

        Entity water = FXGL.spawn("water", data);

        double expectedScaleX = (double) appWidth / 800; // Calcul de l'échelle en X
        double expectedScaleY = (double) appHeight / 448; // Calcul de l'échelle en Y

        assertEquals(800 * expectedScaleX, water.getBoundingBoxComponent().getWidth(), 0.1,
                "La largeur de l'entité 'water' devrait être correctement redimensionnée.");
        assertEquals(448 * expectedScaleY, water.getBoundingBoxComponent().getHeight(), 0.1,
                "La hauteur de l'entité 'water' devrait être correctement redimensionnée.");
    }

}

