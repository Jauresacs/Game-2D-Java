package com.arena.dual_arena.factories;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.arena.dual_arena.FXGLTestBase;
import com.arena.dual_arena.components.PlayerAnimationComponent;
import com.arena.dual_arena.components.PlayerComponent;
import com.arena.dual_arena.components.ProjectileComponent;
import com.arena.dual_arena.components.WeaponComponent;
import com.arena.dual_arena.models.projectiles.Bullet;
import com.arena.dual_arena.models.projectiles.Projectile;
import com.arena.dual_arena.models.weapons.RocketLauncher;
import com.arena.dual_arena.types.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.almasb.fxgl.dsl.FXGL.getGameWorld;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerFactoryTest extends FXGLTestBase {

    private static PlayerFactory playerFactory;

    private static Entity player;
    private WeaponFactory weaponFactory;

    @BeforeAll
    static void setUp() {
        SpawnData spawnData = new SpawnData(100, 200);
        spawnData.put("posX", 100);
        spawnData.put("posY", 200);
        spawnData.put("inputUp", KeyCode.W);
        spawnData.put("inputDown", KeyCode.S);
        spawnData.put("inputLeft", KeyCode.A);
        spawnData.put("inputRight", KeyCode.D);
        spawnData.put("primaryFire", KeyCode.SPACE);
        spawnData.put("secondaryFire", KeyCode.UP);
        spawnData.put("dropWeapon", KeyCode.Q);

        playerFactory = new PlayerFactory();
        player = playerFactory.spawnPlayer(spawnData);
        playerFactory = new PlayerFactory();

    }

    @Test
    void testSpawnPlayer() {


        assertNotNull(player, "The player entity should not be null");
        assertEquals(EntityType.PLAYER, player.getType(), "The entity type should be PLAYER");
        assertEquals(new Point2D(100, 200), player.getPosition(), "The player position should match the spawn data");
        assertTrue(player.hasComponent(PlayerComponent.class), "The player entity should have a PlayerComponent");
        assertTrue(player.hasComponent(PlayerAnimationComponent.class), "The player entity should have a PlayerAnimationComponent");
    }
    @Test
    public void testSpawnGun() {
        SpawnData spawnData = new SpawnData(100, 100);

        weaponFactory = new WeaponFactory();

        Entity gun = weaponFactory.spawnGun(spawnData);

        assertNotNull(gun, "L'arme devrait être créée");
        assertEquals(EntityType.WEAPON, gun.getType(), "Le type d'entité devrait être WEAPON");
        assertTrue(gun.hasComponent(WeaponComponent.class), "L'arme devrait avoir un WeaponComponent");

        assertEquals(100, gun.getX(), "La position X devrait être correcte");
        assertEquals(100, gun.getY(), "La position Y devrait être correcte");
    }

    @Test
    public void testSpawnRifle() {
        SpawnData spawnData = new SpawnData(200, 200);
        weaponFactory = new WeaponFactory();
        Entity rifle = weaponFactory.spawnRifle(spawnData);

        assertNotNull(rifle);
        assertTrue(rifle.hasComponent(WeaponComponent.class));
        WeaponComponent weaponComponent = rifle.getComponent(WeaponComponent.class);
        assertEquals("Rifle", weaponComponent.getWeapon().getClass().getSimpleName());
    }

    @Test
    void testSpawnRocketLauncher() {
        SpawnData data = new SpawnData(100, 200); // Position arbitraire

        WeaponFactory hazardFactory = new WeaponFactory();

        Entity rocketLauncher = hazardFactory.spawnRocketLauncher(data);

        assertNotNull(rocketLauncher, "L'entité Rocket Launcher ne doit pas être nulle.");

        assertEquals(100, rocketLauncher.getX(), "La position X de l'entité est incorrecte.");
        assertEquals(200, rocketLauncher.getY(), "La position Y de l'entité est incorrecte.");
        assertTrue(rocketLauncher.hasComponent(WeaponComponent.class));
        WeaponComponent weaponComponent = rocketLauncher.getComponent(WeaponComponent.class);
        assertEquals("RocketLauncher", weaponComponent.getWeapon().getClass().getSimpleName());

    }

    @Test
    public void testInfiniteSpawner() {
        getGameWorld().getEntitiesByType(EntityType.WEAPON).forEach(getGameWorld()::removeEntity);

        weaponFactory = new WeaponFactory();
        // Démarre le spawner
        weaponFactory.InfiniteSpawner();

        assertEquals(0, getGameWorld().getEntitiesByType(EntityType.WEAPON).size(),
                "Il ne devrait y avoir aucune arme au début");


    }

    @Test
    public void testRandomWeaponSpawn() {
        getGameWorld().getEntitiesByType(EntityType.WEAPON).forEach(getGameWorld()::removeEntity);
        FXGL.getGameWorld().addEntityFactory(new WeaponFactory());
        double x = 300;
        double y = 300;

        WeaponFactory.spawnRandomWeapon(x, y);

        var weapons = getGameWorld().getEntitiesByType(EntityType.WEAPON);
        assertEquals(1, weapons.size(), "Une arme devrait être créée");

        Entity weapon = weapons.get(0);
        assertTrue(weapon.hasComponent(WeaponComponent.class), "L'arme devrait avoir un WeaponComponent");
    }

    @Test
    public void collisionWeapon(){

        weaponFactory = new WeaponFactory();
        Entity rifle = weaponFactory.spawnRifle(new SpawnData(100, 100));

        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);


        assertNull(playerComponent.getEquippedWeapon(), "Le weapon ne devrait pas être équipé");
        playerComponent.pickupWeapon(rifle);

        assertNotNull(playerComponent.getEquippedWeapon(), "Le weapon du player devrait être équipé");

        Entity gun = weaponFactory.spawnGun(new SpawnData(100, 100));

        playerComponent.pickupWeapon(gun);

        WeaponComponent weaponComponent = gun.getComponent(WeaponComponent.class);

        assertNotEquals(playerComponent.getEquippedWeapon(), rifle, "Le player devrait changer d'arme");

        assertEquals(weaponComponent.getOwner(), player, "Le player devrait être le porteur d'arme");

        playerComponent.dropWeapon();

        assertNull(playerComponent.getEquippedWeapon(), "Le weapon devrait être drop.");
    }

    @Test
    void testRotateWeaponWithController_RightStickRight() {
        weaponFactory = new WeaponFactory();


        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);

        Entity gun = weaponFactory.spawnGun(new SpawnData(100, 100));

        playerComponent.pickupWeapon(gun);

        WeaponComponent weaponComponent = gun.getComponent(WeaponComponent.class);
        playerComponent.setXvalue(1.0);
        playerComponent.setYvalue(0.0);

        weaponComponent.rotateWithController();

        assertEquals(0.0, playerComponent.getEquippedWeapon().getRotation(), "L'arme devrait être orientée à 0° lorsque le joystick pointe vers la droite.");
        assertEquals(1, playerComponent.getEquippedWeapon().getScaleX(), "L'arme devrait être mise à l'échelle correcte sur X.");
        assertEquals(1, playerComponent.getEquippedWeapon().getScaleY(), "L'arme devrait être mise à l'échelle correcte sur Y.");
    }

    @Test
    void testRotateWeaponWithController_RightStickUp() {
        weaponFactory = new WeaponFactory();


        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);

        Entity gun = weaponFactory.spawnGun(new SpawnData(100, 100));

        playerComponent.pickupWeapon(gun);

        WeaponComponent weaponComponent = gun.getComponent(WeaponComponent.class);
        playerComponent.setXvalue(0.0);
        playerComponent.setYvalue(-1.0);

        weaponComponent.rotateWithController();

        assertEquals(0.0, playerComponent.getEquippedWeapon().getRotation(), 0.1, "L'arme devrait être orientée à -90° lorsque le joystick pointe vers le haut.");
    }

    @Test
    void testRotateWeaponWithController_RightStickDiagonal() {
        weaponFactory = new WeaponFactory();

        PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);

        Entity gun = weaponFactory.spawnGun(new SpawnData(100, 100));

        playerComponent.pickupWeapon(gun);

        WeaponComponent weaponComponent = gun.getComponent(WeaponComponent.class);
        playerComponent.setXvalue(1.0);
        playerComponent.setYvalue(1.0);

        weaponComponent.rotateWithController();

        assertEquals(0.0, playerComponent.getEquippedWeapon().getRotation(), 0.1, "L'arme devrait être orientée à 45° lorsque le joystick pointe en diagonale haut-droite.");
    }

    @Test
    void testProjectileSpawn() {
        SpawnData spawnData = new SpawnData(200, 200).put("direction", new Point2D(1, 0));
        Projectile projectile = new Bullet(2, 5);

        Entity projectileEntity = FXGL.entityBuilder(spawnData)
                .type(EntityType.PROJECTILE)
                .with(new ProjectileComponent(projectile, spawnData.get("direction"), player))
                .buildAndAttach();

        assertTrue(getGameWorld().getEntitiesByType(EntityType.PROJECTILE).contains(projectileEntity),
                "Le projectile devrait être ajouté au GameWorld.");
    }
    @Test
    void testProjectileMovement() {
        Point2D direction = new Point2D(1, 0).normalize(); // Vers la droite
        Projectile projectile = new Bullet(3, 5); // Exemple de projectile avec une vitesse de 300 px/s

        Entity projectileEntity = FXGL.entityBuilder()
                .at(100, 100)
                .type(EntityType.PROJECTILE)
                .with(new ProjectileComponent(projectile, direction, player))
                .buildAndAttach();

        projectileEntity.getComponent(ProjectileComponent.class).onUpdate(1.0);

        assertEquals(1600, projectileEntity.getX(), 0.1, "Le projectile devrait se déplacer de 300 pixels en 1 seconde.");
        assertEquals(100, projectileEntity.getY(), 0.1, "Le projectile ne devrait pas changer de position en Y.");
    }

    @Test
    void testProjectileMaxRange() {
        Projectile projectile = new Bullet(3, 5);
        Entity projectileEntity = FXGL.entityBuilder()
                .at(100, 100)
                .type(EntityType.PROJECTILE)
                .with(new ProjectileComponent(projectile, new Point2D(1, 0), player))
                .buildAndAttach();

        for (int i = 0; i < 2; i++) {
            projectileEntity.getComponent(ProjectileComponent.class).onUpdate(1.0); // Chaque update = 1 seconde
        }

        assertFalse(getGameWorld().getEntities().contains(projectileEntity),
                "Le projectile devrait être supprimé après avoir parcouru sa portée maximale.");
    }

}
