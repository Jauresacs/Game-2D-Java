package com.arena.dual_arena.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.arena.dual_arena.components.ProjectileComponent;
import com.arena.dual_arena.types.EntityType;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectileFactoryTest {

    @BeforeAll
    static void setUp() {
            // Collecte toutes les entités à supprimer avant de les parcourir
            var entitiesToRemove = new ArrayList<>(FXGL.getGameWorld().getEntities());

            // Supprime chaque entité sans modifier directement la collection pendant l'itération
            entitiesToRemove.forEach(FXGL.getGameWorld()::removeEntity);

            // Ajoute la factory ProjectileFactory pour les tests
            FXGL.getGameWorld().addEntityFactory(new ProjectileFactory());
    }

    @Test
    void testSpawnBullet() {
        SpawnData spawnData = new SpawnData(100, 100)
                .put("direction", new Point2D(1, 0))
                .put("speedMultiplier", 1.0f)
                .put("knockbackMultiplier", 1.0f)
                .put("owner", FXGL.entityBuilder().type(EntityType.PLAYER).build());

        Entity bullet = FXGL.spawn("bullet", spawnData);

        assertNotNull(bullet, "Le projectile doit être créé.");
        assertEquals(EntityType.PROJECTILE, bullet.getType(), "Le type d'entité doit être PROJECTILE.");
        assertEquals(100, bullet.getX(), "La position X doit correspondre à la position de spawn.");
        assertEquals(100, bullet.getY(), "La position Y doit correspondre à la position de spawn.");
        assertTrue(bullet.hasComponent(ProjectileComponent.class), "Le projectile doit avoir un ProjectileComponent.");
    }

    @Test
    void testProjectileRotation() {
        SpawnData spawnData = new SpawnData(100, 100)
                .put("direction", new Point2D(0, 1)) // Direction vers le bas
                .put("speedMultiplier", 1.0f)
                .put("knockbackMultiplier", 1.0f)
                .put("rangeMultiplier", 1.0f) // Ajout de la clé manquante
                .put("owner", FXGL.entityBuilder().type(EntityType.PLAYER).build());

        Entity rocket = FXGL.spawn("rocket", spawnData);

        assertEquals(90.0, rocket.getRotation(), 0.1, "Le projectile doit être orienté à 90° pour un mouvement vers le bas.");

    }

    @Test
    void testProjectileScale() {
        SpawnData spawnData = new SpawnData(100, 100)
                .put("direction", new Point2D(1, 0))
                .put("speedMultiplier", 1.0f)
                .put("knockbackMultiplier", 1.0f)
                .put("rangeMultiplier", 1.0f)
                .put("owner", FXGL.entityBuilder().type(EntityType.PLAYER).build());

        Entity rocket = FXGL.spawn("rocket", spawnData);

        assertEquals(1.0, rocket.getScaleX(), "Le facteur d'échelle X du projectile doit être 1.0.");
        assertEquals(1.0, rocket.getScaleY(), "Le facteur d'échelle Y du projectile doit être 1.0.");
    }

    @Test
    void testProjectileCollisionWithPlayer() {
        Entity player = FXGL.entityBuilder()
                .at(150, 100)
                .type(EntityType.PLAYER)
                .with(new CollidableComponent(true))
                .buildAndAttach();

        SpawnData spawnData = new SpawnData(150, 100)
                .put("direction", new Point2D(1, 0))
                .put("speedMultiplier", 1.0f)
                .put("knockbackMultiplier", 1.0f)
                .put("owner", FXGL.entityBuilder().type(EntityType.PLAYER).build());
        Entity bullet = FXGL.spawn("bullet", spawnData);

        FXGL.getGameWorld().onUpdate(0.016);

        assertTrue(FXGL.getGameWorld().getEntities().contains(bullet), "Le projectile doit être supprimé après collision.");
    }


}
