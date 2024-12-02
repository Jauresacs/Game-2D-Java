package com.arena.dual_arena.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.arena.dual_arena.components.WeaponComponent;
import com.arena.dual_arena.models.weapons.*;
import com.arena.dual_arena.types.EntityType;
import javafx.geometry.Point2D;
import javafx.util.Duration;

import java.util.Random;

/**
 * Factory class responsible for the creation and management of weapon entities in the game.
 * This factory uses the FXGL framework to handle entity creation, ensuring each weapon is properly initialized with its specific components.
 */
public class WeaponFactory implements EntityFactory {

    private final Random random = new Random();
    private final double dropTime=15;
    private final int rocketTimer = 70;

    /**
     * Spawns a gun entity with specific attributes and visuals.
     * @param data The spawn data including the location and any other relevant information for spawning the gun.
     * @return The newly created gun entity.
     */
    @Spawns("gun")
    public Entity spawnGun(SpawnData data) {
        Point2D gunBarrelOffset = new Point2D(10, 0);
        return createWeapon(data, new Gun(), "DEagle_0.png", gunBarrelOffset);
    }

    /**
     * Spawns a rifle entity with specific attributes and visuals.
     * @param data The spawn data including the location and any other relevant information for spawning the rifle.
     * @return The newly created rifle entity.
     */
    @Spawns("rifle")
    public Entity spawnRifle(SpawnData data) {
        Point2D rifleBarrelOffset = new Point2D(40, 10);
        return createWeapon(data, new Rifle(), "FAMAS_00.png", rifleBarrelOffset);
    }

    /**
     * Spawns a sniper entity with specific attributes and visuals.
     * @param data The spawn data including the location and any other relevant information for spawning the sniper.
     * @return The newly created sniper entity.
     */
    @Spawns("sniper")
    public Entity spawnSniper(SpawnData data) {
        Point2D sniperBarrelOffset = new Point2D(40, 10);
        return createWeapon(data, new Sniper(), "AWP_00.png", sniperBarrelOffset);
    }

    /**
     * Spawns a rocket launcher entity with specific attributes and visuals.
     * @param data The spawn data including the location and any other relevant information for spawning the rocket launcher.
     * @return The newly created rocket launcher entity.
     */
    @Spawns("rocket-launcher")
    public Entity spawnRocketLauncher(SpawnData data) {
        Point2D rocketLauncherBarrelOffset = new Point2D(40, 10);
        return createWeapon(data, new RocketLauncher(), "Six12SD_0.png", rocketLauncherBarrelOffset);
    }

    /**
     * Helper method to create a weapon entity based on provided parameters.
     * @param data Spawn data including the location for the weapon.
     * @param weapon The weapon object specifying the behavior and attributes.
     * @param spriteName The image file name for the weapon's visual representation.
     * @param barrelOffset The offset for the weapon's barrel, affecting shooting mechanics.
     * @return The newly created weapon entity.
     */
    private Entity createWeapon(SpawnData data, Weapon weapon, String spriteName, Point2D barrelOffset) {
        return FXGL.entityBuilder(data)
                .type(EntityType.WEAPON)
                .viewWithBBox(spriteName)
                .with(new CollidableComponent(true))
                .with(new WeaponComponent(weapon, barrelOffset))
                .build();
    }

    /**
     * Spawns a random weapon at a given location.
     * @param x The x-coordinate for the weapon spawn location.
     * @param y The y-coordinate for the weapon spawn location.
     */
    public static void spawnRandomWeapon(double x, double y) {
        String[] weaponTypes = {"gun", "rifle", "sniper", "rocket-launcher"};
        String randomType = weaponTypes[(int)(Math.random() * weaponTypes.length)];
        FXGL.spawn(randomType, new SpawnData(x, y));
    }

    /**
     * Counts the number of weapon entities currently present in the game world.
     * @return The count of weapon entities.
     */
    private int getWeaponCount() {
        // Count the number of weapon in the world game;
        return (int) FXGL.getGameWorld().getEntitiesByType(EntityType.WEAPON).size();
    }

    /**
     * Starts an infinite loop that periodically spawns random weapons if there are fewer than three weapons present.
     * Weapons are spawned at random locations within defined bounds.
     */
    public void InfiniteSpawner() {
        FXGL.run(() -> {
            if (getWeaponCount() < 4) {
                int rx, ry;
                rx = random.nextInt(FXGL.getAppWidth() * 8 / 25) + FXGL.getAppWidth() * 8 / 25;
                ry = random.nextInt(FXGL.getAppHeight() * 7 / 14) + FXGL.getAppHeight() * 3 / 14;

                spawnRandomWeapon(rx, ry);
            }}, Duration.seconds(dropTime));

    }
    public void RocketSpawner() {
        FXGL.run(() -> {
            FXGL.spawn("rocket-launcher",FXGL.getAppWidth()/2, FXGL.getAppHeight()/2);
            }, Duration.seconds(rocketTimer+random.nextInt(20)));

    }

}
