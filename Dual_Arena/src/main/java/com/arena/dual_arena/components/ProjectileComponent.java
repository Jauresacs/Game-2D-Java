package com.arena.dual_arena.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.arena.dual_arena.models.projectiles.*;
import com.arena.dual_arena.types.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import com.almasb.fxgl.audio.Sound;

import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getAssetLoader;

/**
 * Component responsible for handling the behavior of projectiles, including movement,
 * collision detection, and impact effects in the game.
 */
public class ProjectileComponent extends Component {
    private final Projectile projectile;
    private Point2D direction;
    private double distanceTraveled = 0;
    private final Entity owner;
    private static final Duration KNOCKBACK_DURATION = Duration.seconds(0.3);
    private static final Duration EXPLOSION_DURATION = Duration.seconds(0.4);
    private float rightStickX = 0.0f;
    private float rightStickY = 0.0f;

    /**
     * Constructs a ProjectileComponent with specified projectile, direction, and owner.
     *
     * @param projectile The projectile data.
     * @param direction  Initial direction of the projectile.
     * @param owner      The entity that fired the projectile.
     */
    public ProjectileComponent(Projectile projectile, Point2D direction, Entity owner) {
        this.projectile = projectile;
        this.direction = direction;
        this.owner = owner;
    }

    /**
     * Updates the projectile's behavior each frame, handling its motion based on its type,
     * checking for collisions, and managing its lifecycle.
     *
     * @param tpf Time per frame, which is used to calculate how far the projectile moves in this update cycle.
     */
    @Override
    public void onUpdate(double tpf) {
        double distanceThisFrame = projectile.getSpeed() * tpf;
        switch (projectile) {
            case GuidedRocket guidedRocket -> {
                PlayerComponent playerComponent = owner.getComponent(PlayerComponent.class);
                rightStickX = playerComponent.getRightStickX();
                rightStickY = playerComponent.getRightStickY();

                double deadZone = 0.2;

                if (Math.abs(rightStickX) > deadZone || Math.abs(rightStickY) > deadZone) {
                    double angle = Math.toDegrees(Math.atan2(rightStickY, rightStickX));
                    entity.setRotation(angle);

                    Point2D stickDirection =  new Point2D(rightStickX, rightStickY).normalize();

                    direction = direction.add(stickDirection.multiply(0.1)).normalize();
                }

                double angle1 = Math.atan2(rightStickY, rightStickY);

//                Point2D mousePosition = FXGL.getInput().getMousePositionWorld();
//                Point2D toMouse = mousePosition.subtract(entity.getPosition());
//                Point2D normalizedDirection = toMouse.normalize();
//
//                double angle = Math.toDegrees(Math.atan2(direction.getY(), direction.getX()));
//                entity.setRotation(angle);
//
//                direction = direction.add(normalizedDirection.multiply(0.1)).normalize();

                entity.translate(direction.multiply(distanceThisFrame));


            }
            default -> entity.translate(direction.multiply(distanceThisFrame));
        }

        distanceTraveled += distanceThisFrame;

        FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(player -> {
            if (player != owner && entity.isColliding(player)) {
                handleCollision(player);
            }
        });

        // Handle range-based deletion
        if (projectile.getRange() > 0 && distanceTraveled >= projectile.getRange()) {
            handleRangeReached();
            return;
        }

        double screenWidth = FXGL.getAppWidth();
        double screenHeight = FXGL.getAppHeight();
        Point2D position = entity.getPosition();

        // Add a small buffer to ensure the projectile is fully offscreen
        if (position.getX() < -100 || position.getX() > screenWidth + 100 ||
                position.getY() < -100 || position.getY() > screenHeight + 100) {
            entity.removeFromWorld();
        }
    }

    /**
     * Handles logic when a projectile reaches its maximum range. Depending on the projectile type,
     * different effects may occur, such as creating an explosion.
     */
    private void handleRangeReached() {
        switch (projectile) {
            case Rocket rocket -> createExplosion(false);
            case ReversedRocket reversedRocket -> createExplosion(true);
            case GuidedRocket guidedRocket -> createExplosion(false);
            default -> {
                break;
            }
        }

        entity.removeFromWorld();
    }

    /**
     * Handles the effects of a projectile's collision with another entity, typically a player.
     * Depending on the type of projectile, this can involve creating explosions, applying knockback, and triggering visual effects.
     *
     * @param player The entity that the projectile has collided with.
     */
    private void handleCollision(Entity player) {
        entity.removeFromWorld();

        switch (projectile) {
            case Rocket rocket -> createExplosion(false);
            case ReversedRocket reversedRocket -> createExplosion(true);
            case GuidedRocket guidedRocket -> createExplosion(false);
            case Bullet bullet -> {
                PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
                if (playerComponent != null) {
                    playerComponent.applyKnockback(direction, projectile.getKnockback());
                    addImpactEffects(entity.getCenter());
                }
            }
            case ReversedBullet reversedBullet -> {
                PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
                if (playerComponent != null) {
                    playerComponent.applyKnockback(direction, projectile.getKnockback());
                    addImpactEffects(entity.getCenter());
                }
            }
            default -> {}
        }
    }

    /**
     * Creates an explosion effect at the projectile's current location and applies effects based on the explosion.
     * This includes visual effects and gameplay interactions such as knockback.
     *
     * @param reversed Boolean flag that determines the direction of the knockback effect. If true, the knockback pulls entities towards the explosion center.
     */
    private void createExplosion(boolean reversed) {
        Point2D explosionCenter = entity.getCenter();
        double explosionRadius = (entity.getScaleX() + 100) * projectile.getExplosionRadiusMultiplier();

        // Create visual explosion effect
        Entity explosionEffect = FXGL.entityBuilder()
                .at(explosionCenter)
                .view(new Circle(explosionRadius, Color.ORANGE))
                //.with(new ExplosionEffectComponent(EXPLOSION_DURATION))
                .buildAndAttach();

        /*
        FXGL.animationBuilder()
                .duration(EXPLOSION_DURATION)
                .scale(entity.getViewComponent().getChildren().getFirst())
                .from(new Point2D(1.0, 1.0))
                .to(new Point2D(0.1, 0.1))
                .buildAndPlay();
         */

        FXGL.animationBuilder()
                .duration(Duration.seconds(0.15))
                .onFinished(explosionEffect::removeFromWorld)
                .fadeOut(explosionEffect)
                .buildAndPlay();

        Sound rocketExplosion = getAssetLoader().loadSound("rocket_explosion.wav");

        FXGL.getAudioPlayer().playSound(rocketExplosion);

        // Apply knockback to nearby players
        List<Entity> nearbyPlayers = FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER);
        for (Entity player : nearbyPlayers) {
            Point2D playerCenter = player.getCenter();
            double distance = explosionCenter.distance(playerCenter);

            if (distance <= explosionRadius) {
                // Calculate knockback direction from explosion center to player
                Point2D knockbackDirection;
                if (reversed) {
                    knockbackDirection = explosionCenter.subtract(playerCenter).normalize();
                } else {
                    knockbackDirection = playerCenter.subtract(explosionCenter).normalize();
                }

                // Calculate knockback force based on distance (closer = stronger)
                float knockbackForce = projectile.getKnockback() * (1 - (float) (distance / explosionRadius)) * projectile.getExplosionKnockbackFalloff();

                // Apply knockback to player
                PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
                if (playerComponent != null) {
                    playerComponent.applyKnockback(knockbackDirection, knockbackForce);
                }
            }
        }
    }

    /**
     * Creates visual effects at the specified impact position to indicate a collision.
     * This method is typically called when a projectile hits an entity or terrain.
     *
     * @param impactPosition The point at which the impact occurs, usually where the projectile collides with another entity.
     */
    private void addImpactEffects(Point2D impactPosition) {
        // Create a quick flash effect at the impact point
        Entity flash = FXGL.entityBuilder()
                .at(impactPosition)
                .viewWithBBox(new Circle(5, Color.WHITE))
                .buildAndAttach();

        // Fade out and remove the flash
        FXGL.animationBuilder()
                .duration(Duration.seconds(0.15))
                .onFinished(flash::removeFromWorld)
                .fadeOut(flash)
                .buildAndPlay();
    }
}
