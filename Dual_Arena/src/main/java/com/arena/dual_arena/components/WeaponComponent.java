package com.arena.dual_arena.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.arena.dual_arena.models.weapons.*;
import com.arena.dual_arena.utils.DebugMode;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Component for handling weapon-specific interactions and behaviors in the game.
 * This component manages weapon positioning, firing, and interactions with the game's control inputs.
 */
public class WeaponComponent extends Component {
    private final Weapon weapon;
    private final Point2D barrelOffset;
    private long lastShotTime = 0;
    private Entity owner = null;
    private boolean isPickedUp = false;
    private Entity weaponOrigin;
    static final float STICK_DEAD_ZONE = 0.2f;

    /**
     * Constructs a WeaponComponent with a specific weapon and its barrel offset.
     * This setup is essential for managing how the weapon interacts within the game world, including where projectiles originate.
     *
     * @param weapon The weapon instance that this component will manage.
     * @param barrelOffset The offset from the center of the weapon to the point where projectiles are considered to begin.
     */
    public WeaponComponent(Weapon weapon, Point2D barrelOffset) {
        this.weapon = weapon;
        this.barrelOffset = barrelOffset;
    }

    /**
     * Called when the component is added to an entity. Initializes debugging visuals if debug mode is active.
     * Debug visuals include a red circle at the weapon's origin to track its position clearly.
     */
    @Override
    public void onAdded() {
        if (DebugMode.isDebugMode()) {
            weaponOrigin = FXGL.entityBuilder()
                    .at(entity.getCenter())
                    .view(new Circle(10, Color.RED))
                    .with(new CollidableComponent(false))
                    .buildAndAttach();
        }
    }

    /**
     * Called every frame, updating the state of the weapon component.
     * If the weapon is picked up by an owner, it updates the weapon's position to align with the owner and
     * handles rotation based on the current control input (either mouse or controller).
     *
     * @param tpf Time per frame, provided by the game engine, used for frame-rate-independent updates.
     */
    @Override
    public void onUpdate(double tpf) {
        if (isPickedUp && owner != null) {
            // Center the weapon on the player's center position
            entity.setPosition(
                    owner.getCenter().subtract(
                            entity.getWidth() / 2,
                            entity.getHeight() / 2
                    )
            );

            Point2D mousePosition = FXGL.getInput().getMousePositionWorld();
//            rotateToMouse(mousePosition);

            if (DebugMode.isDebugMode()) {
                if (weaponOrigin != null) {
                    weaponOrigin.setPosition(entity.getCenter());
                } else {
                    weaponOrigin = FXGL.entityBuilder()
                            .at(entity.getCenter())
                            .view(new Circle(10, Color.RED))
                            .with(new CollidableComponent(false))
                            .buildAndAttach();
                }
            }
            rotateWithController();
        }
    }

    /**
     * Rotates the weapon based on controller input to align with the direction pointed by the game controller's right stick.
     * This method considers a dead zone to prevent weapon rotation due to minimal stick displacements.
     */
    public void rotateWithController() {

            PlayerComponent player = owner.getComponent(PlayerComponent.class);

        float rightStickX = player.getRightStickX();
        float rightStickY = player.getRightStickY();

            if (Math.abs(rightStickX) > STICK_DEAD_ZONE || Math.abs(rightStickY) > STICK_DEAD_ZONE) {
                    //Point2D weaponPosition = entity.getCenter();
                    //Point2D stickDirection = new Point2D(rightStickX, rightStickY);

                    // Calcule l'angle en fonction de la direction du stick
                    double angle = Math.toDegrees(Math.atan2(rightStickY, rightStickX));
                    //System.out.println(angle);
                    entity.setRotation(angle);

                    // Gère le retournement de l'arme
                    if (rightStickX < 0) {
                        entity.setScaleX(-1);
                        entity.setScaleY(-1);
                    } else {
                        entity.setScaleX(-1);
                        entity.setScaleY(1);
                    }
            }
    }

    /**
     * Rotates the weapon to always point towards the mouse cursor, providing a direct aiming mechanism for the player.
     *
     * @param mousePosition The current position of the mouse in the game world.
     */
    private void rotateToMouse(Point2D mousePosition) {
        Point2D weaponPosition = entity.getCenter();
        double angle = Math.toDegrees(Math.atan2(
                mousePosition.getY() - weaponPosition.getY(),
                mousePosition.getX() - weaponPosition.getX()
        ));
        entity.setRotation(angle);

        if (mousePosition.getX() < weaponPosition.getX()) {
            entity.setScaleX(-1);
            entity.setScaleY(-1);
        } else {
            entity.setScaleX(-1);
            entity.setScaleY(1);
        }
    }

    /**
     * Calculates the actual barrel position of the weapon, considering the current rotation.
     * This position is where projectiles will originate from when the weapon fires.
     *
     * @return The calculated barrel position as a {@link Point2D}.
     */
    public Point2D getBarrelPosition() {
        double angle = entity.getRotation();
        double cos = Math.cos(Math.toRadians(angle));
        double sin = Math.sin(Math.toRadians(angle));

        double x = barrelOffset.getX() * cos - barrelOffset.getY() * sin;
        double y = barrelOffset.getX() * sin + barrelOffset.getY() * cos;

        return entity.getCenter().add(x, y);
    }

    /**
     * Assigns a new owner to the weapon, marking it as picked up.
     * This changes the weapon's state to "in use," allowing it to interact with other game mechanics like firing.
     *
     * @param newOwner The entity that is picking up the weapon.
     */
    public void pickup(Entity newOwner) {
        owner = newOwner;
        isPickedUp = true;
    }

    /**
     * Releases the weapon from its current owner, resetting its state and position.
     * This method is called when the weapon is dropped by the player, resetting its orientation and ready state.
     */
    public void drop() {
        owner = null;
        isPickedUp = false;
        // Reset rotation and scale when dropped
        entity.setRotation(0);
        entity.setScaleX(1);
        entity.setScaleY(1);
    }

    /**
     * Initiates firing the weapon's primary projectile if the weapon is currently picked up and ready to fire.
     * This method checks the weapon's cooldown and fires a projectile if enough time has elapsed since the last shot.
     *
     * @param direction The direction in which to fire the projectile, typically towards a target or cursor position.
     */
    public void primaryFire(Point2D direction) {
        if (!isPickedUp) return; // Can't fire if not picked up

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime >= weapon.getPrimaryFireRate()) {
            lastShotTime = currentTime;
            Point2D spawnPosition = entity.getCenter();

            SpawnData spawnData = new SpawnData(spawnPosition)
                    .put("direction", direction)
                    .put("rangeMultiplier", weapon.getRangeMultiplier())
                    .put("speedMultiplier", weapon.getSpeedMultiplier())
                    .put("knockbackMultiplier", weapon.getKnockbackMultiplier())
                    .put("owner", owner);

            switch (weapon) {
                case Gun gun -> FXGL.play("gun_shot.wav");
                case Rifle rifle -> FXGL.play("rifle_shot.wav");
                case Sniper sniper -> FXGL.play("sniper_shot.wav");
                case RocketLauncher rocketLauncher -> FXGL.play("rocket_blast.wav");
                default -> throw new IllegalStateException("Unexpected value: " + weapon);
            }

            FXGL.spawn(weapon.getPrimaryProjectileType(), spawnData);
        }
    }

    /**
     * Initiates firing the weapon's secondary projectile if the weapon is currently picked up and ready to fire.
     * This method checks the weapon's cooldown for secondary fire and fires a projectile if enough time has elapsed since the last secondary shot.
     *
     * @param direction The direction in which to fire the secondary projectile.
     */
    public void secondaryFire(Point2D direction) {
        if (!isPickedUp) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime >= weapon.getSecondaryFireRate()) {
            lastShotTime = currentTime;
            Point2D spawnPosition = entity.getCenter();

            SpawnData spawnData = new SpawnData(spawnPosition)
                    .put("direction", direction)
                    .put("rangeMultiplier", weapon.getRangeMultiplier())
                    .put("speedMultiplier", weapon.getSpeedMultiplier())
                    .put("knockbackMultiplier", weapon.getKnockbackMultiplier())
                    .put("owner", owner);

            switch (weapon) {
                case Gun gun -> FXGL.play("gun_shot.wav");
                case Rifle rifle -> FXGL.play("rifle_shot.wav");
                case Sniper sniper -> FXGL.play("sniper_shot.wav");
                case RocketLauncher rocketLauncher -> FXGL.play("rocket_blast.wav");
                default -> throw new IllegalStateException("Unexpected value: " + weapon);
            }

            FXGL.spawn(weapon.getSecondaryProjectileType(), spawnData);
        }
    }

    /**
     * Checks if the weapon is currently picked up by an owner.
     *
     * @return True if the weapon is picked up, false otherwise.
     */
    public boolean isPickedUp() {
        return isPickedUp;
    }

    /**
     * Gets the entity that currently owns the weapon. This could be a player or any other entity that can wield weapons.
     *
     * @return The owner entity of the weapon, or null if it is not currently owned.
     */
    public Entity getOwner() {
        return owner;
    }

    /**
     * Retrieves the weapon data associated with this component, which includes its firing mechanics, rates, and projectile types.
     *
     * @return The weapon instance being managed by this component.
     */
    public Weapon getWeapon() {
        return weapon;
    }
}
