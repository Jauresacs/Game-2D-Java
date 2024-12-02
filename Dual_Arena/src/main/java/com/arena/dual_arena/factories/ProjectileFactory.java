package com.arena.dual_arena.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.arena.dual_arena.components.ProjectileComponent;
import com.arena.dual_arena.models.projectiles.*;
import com.arena.dual_arena.types.EntityType;
import javafx.geometry.Point2D;

/**
 * Factory class for creating projectile entities in the game.
 * This class is responsible for initializing projectiles with specified characteristics and visuals based on data provided at spawn time.
 */
public class ProjectileFactory implements EntityFactory {
    /**
     * Spawns a bullet projectile.
     * @param data The spawn data which includes parameters such as speed and knockback multipliers.
     * @return A new bullet entity with specified properties.
     */
    @Spawns("bullet")
    public Entity spawnBullet(SpawnData data) {
        float speedMultiplier = data.get("speedMultiplier");
        float knockbackMultiplier = data.get("knockbackMultiplier");
        return createProjectile(data, new Bullet(speedMultiplier, knockbackMultiplier), "bullet.png", 3f);
    }

    /**
     * Spawns a reversed bullet projectile, which has a negative knockback effect.
     * @param data The spawn data which includes parameters such as speed and knockback multipliers.
     * @return A new reversed bullet entity with specified properties.
     */
    @Spawns("reversed-bullet")
    public Entity spawnReversedBullet(SpawnData data) {
        float speedMultiplier = data.get("speedMultiplier");
        float knockbackMultiplier = data.get("knockbackMultiplier");
        return createProjectile(data, new ReversedBullet(speedMultiplier, knockbackMultiplier), "reversed_bullet.png", 3f);
    }

    /**
     * Spawns a rocket projectile.
     * @param data The spawn data which includes parameters such as range, speed, and knockback multipliers.
     * @return A new rocket entity with specified properties.
     */
    @Spawns("rocket")
    public Entity spawnRocket(SpawnData data) {
        float rangeMultiplier = data.get("rangeMultiplier");
        float speedMultiplier = data.get("speedMultiplier");
        float knockbackMultiplier = data.get("knockbackMultiplier");
        return createProjectile(data, new Rocket(rangeMultiplier, speedMultiplier, knockbackMultiplier), "rocket.png", 1f);
    }

    /**
     * Spawns a reversed rocket projectile, which pulls targets towards the point of explosion.
     * @param data The spawn data which includes parameters such as range, speed, and knockback multipliers.
     * @return A new reversed rocket entity with specified properties.
     */
    @Spawns("reversed-rocket")
    public Entity spawnReversedRocket(SpawnData data) {
        float rangeMultiplier = data.get("rangeMultiplier");
        float speedMultiplier = data.get("speedMultiplier");
        float knockbackMultiplier = data.get("knockbackMultiplier");
        return createProjectile(data, new ReversedRocket(rangeMultiplier, speedMultiplier, knockbackMultiplier), "reversed_rocket.png", 1f);
    }

    /**
     * Spawns a guided rocket projectile, which has enhanced targeting capabilities.
     * @param data The spawn data which includes parameters such as range, speed, and knockback multipliers.
     * @return A new guided rocket entity with specified properties.
     */
    @Spawns("guided-rocket")
    public Entity spawnGuidedRocket(SpawnData data) {
        float rangeMultiplier = data.get("rangeMultiplier");
        float speedMultiplier = data.get("speedMultiplier");
        float knockbackMultiplier = data.get("knockbackMultiplier");
        return createProjectile(data, new GuidedRocket(rangeMultiplier, speedMultiplier, knockbackMultiplier), "guided_rocket.png", 3f);
    }

    /**
     * Generic method to create a projectile entity.
     * This method configures the projectile with a sprite, scale, and initial rotation based on its travel direction.
     * @param data The spawn data including the initial position and owner entity.
     * @param projectile The projectile object containing behavior logic.
     * @param spriteName The image file name for the projectile's visual representation.
     * @param scale The scale factor for the projectile's visual representation.
     * @return The newly created projectile entity.
     */
    private Entity createProjectile(SpawnData data, Projectile projectile, String spriteName, float scale) {
        Point2D direction = data.get("direction");
        Entity owner = data.get("owner");

        Entity projectileEntity = FXGL.entityBuilder(data)
                .type(EntityType.PROJECTILE)
                .viewWithBBox(spriteName)
                .with(new CollidableComponent(true))
                .with(new ProjectileComponent(projectile, direction, owner))
                .build();

        projectileEntity.setScaleX(scale);
        projectileEntity.setScaleY(scale);
        double angle = Math.toDegrees(Math.atan2(direction.getY(), direction.getX()));
        projectileEntity.setRotation(angle);

        return projectileEntity;
    }
}
