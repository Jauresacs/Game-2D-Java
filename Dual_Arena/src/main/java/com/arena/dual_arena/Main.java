package com.arena.dual_arena;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.physics.CollisionHandler;
import com.arena.dual_arena.components.PlayerComponent;
import com.arena.dual_arena.components.WeaponComponent;
import com.arena.dual_arena.factories.*;
import com.arena.dual_arena.models.hazard.AttackFireball;
import com.arena.dual_arena.models.hazard.AttackZoneCircle;
import com.arena.dual_arena.types.EntityType;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

import java.util.Map;
import java.util.Random;
import java.util.HashMap;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class Main extends GameApplication {
    private Map<Music, Integer> musicTracks;
    private Music currentTrack;
    private double elapsedTime;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(16 * 90);
        settings.setHeight(9 * 90);
        settings.setTitle("Island Knockout");
        settings.setVersion("1.0");
        settings.setFullScreenAllowed(true);
        settings.setMainMenuEnabled(true);
        settings.setGameMenuEnabled(true);
        settings.setDeveloperMenuEnabled(true);
    }

    @Override
    protected void onPreInit() {
        getSettings().setGlobalMusicVolume(0.25);
        musicTracks = new HashMap<>();
        musicTracks.put(getAssetLoader().loadMusic("music_1.mp3"), 125); // 125 seconds
        musicTracks.put(getAssetLoader().loadMusic("music_2.mp3"), 153); // 153 seconds

        playRandomMusic();
    }

    private void playRandomMusic() {
        if (currentTrack != null) {
            getAudioPlayer().stopMusic(currentTrack);
        }

        // Pick a random track
        var randomIndex = new Random().nextInt(musicTracks.size());
        currentTrack = (Music) musicTracks.keySet().toArray()[randomIndex];

        elapsedTime = 0;

        // Play the selected track
        getAudioPlayer().playMusic(currentTrack);
    }

    @Override
    protected void onUpdate(double tpf) {
        // Increment elapsed time (tpf = time per frame in seconds)
        elapsedTime += tpf;

        if (currentTrack != null) {
            // Get the current track's duration
            int trackDuration = musicTracks.get(currentTrack);

            // Check if the track duration has passed
            if (elapsedTime >= trackDuration) {
                playRandomMusic(); // Switch to the next track
            }
        }
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new ArenaFactory());

        Level map = FXGL.setLevelFromMap("arena.tmx");

        double scaleX = (double) FXGL.getAppWidth() / map.getWidth(); // Fit to window width
        double scaleY = (double) FXGL.getAppHeight() / map.getHeight(); // Fit to window height

        Entity mapEntity = FXGL.getGameWorld().getEntities().getFirst();
        mapEntity.setScaleX(scaleX);
        mapEntity.setScaleY(scaleY);

        FXGL.getGameWorld().getEntitiesByType(EntityType.WATER).forEach(entity -> {
            entity.setPosition(entity.getX() * scaleX, entity.getY() * scaleY);
        });

        FXGL.getGameWorld().addEntityFactory(new PlayerFactory());
        WeaponFactory weaponFactory = new WeaponFactory();
        FXGL.getGameWorld().addEntityFactory(weaponFactory);
        FXGL.getGameWorld().addEntityFactory(new ProjectileFactory());
        FXGL.getGameWorld().addEntityFactory(new HazardFactory());

        spawnPlayer(FXGL.getAppWidth() / 5 - 16*2, FXGL.getAppHeight() / 2 - 28*2, KeyCode.Z, KeyCode.S, KeyCode.Q, KeyCode.D, KeyCode.SPACE, KeyCode.C, KeyCode.G);
        spawnPlayer(FXGL.getAppWidth() - FXGL.getAppWidth() / 5 - 16 * 2, FXGL.getAppHeight() / 2 - 28*2, KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.PAGE_UP, KeyCode.PAGE_DOWN, KeyCode.M);
        FXGL.spawn("gun", (double) FXGL.getAppWidth() / 5 - 16*2, (double) FXGL.getAppHeight() / 2 - 28*2);
        FXGL.spawn("gun", FXGL.getAppWidth() - (double) FXGL.getAppWidth() / 5 - 16 * 2, (double) FXGL.getAppHeight() / 2 - 28*2);

        //FXGL.spawn("rocket-launcher", (double) FXGL.getAppWidth() / 2, (double) FXGL.getAppHeight() / 2);
        /*
        /* Rescale player in relation to window size

        FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(entity -> {
            System.out.println("Window width: " + FXGL.getAppWidth());
            System.out.println("Window height: " + FXGL.getAppHeight());

            System.out.println("Player width: " + entity.getWidth());
            System.out.println("Player height: " + entity.getHeight());
            System.out.println("Player scaleX: " + entity.getScaleX());
            System.out.println("Player scaleY: " + entity.getScaleY());

            double playerScaleX = (double) FXGL.getAppWidth() / entity.getWidth(); // Fit to window width
            double playerScaleY = (double) FXGL.getAppHeight() / entity.getHeight(); // Fit to window height

            entity.setScaleX(playerScaleX);
            entity.setScaleY(playerScaleY);

            System.out.println("new Player width: " + entity.getWidth());
            System.out.println("new Player height: " + entity.getHeight());
            System.out.println("new Player scaleX: " + entity.getScaleX());
            System.out.println("new Player scaleY: " + entity.getScaleY());
        });

         */

        FXGL.runOnce(weaponFactory::InfiniteSpawner, Duration.seconds(1));
        new AttackZoneCircle();
        FXGL.run(AttackFireball::new,Duration.seconds(40));
        FXGL.run(AttackZoneCircle::new,Duration.seconds(60));
        FXGL.runOnce(weaponFactory::RocketSpawner, Duration.seconds(1));
    }

    public void spawnPlayer(int posX, int posY, KeyCode inputUp, KeyCode inputDown, KeyCode inputLeft, KeyCode inputRight, KeyCode primaryFire, KeyCode secondaryFire, KeyCode dropWeapon) {
        SpawnData spawnData = new SpawnData()
                .put("posX", posX)
                .put("posY", posY)
                .put("inputUp", inputUp)
                .put("inputDown", inputDown)
                .put("inputLeft", inputLeft)
                .put("inputRight", inputRight)
                .put("primaryFire", primaryFire)
                .put("secondaryFire", secondaryFire)
                .put("dropWeapon", dropWeapon);

        FXGL.spawn("player", spawnData);
    }

    @Override
    protected void initPhysics() {
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.WEAPON) {
            @Override
            protected void onCollisionBegin(Entity player, Entity weapon) {
                PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
                if (playerComponent != null && weapon.getComponent(WeaponComponent.class).getOwner() == null) {
                    playerComponent.pickupWeapon(weapon);
                }
            }
        });

        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.WATER) {
            @Override
            protected void onCollisionBegin(Entity player, Entity water) {
                PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
                if (playerComponent != null){
                    FXGL.showMessage("Joueur " + playerComponent.getId() + " est mort", () -> {
                        resetGame();
                    });
                }
            }
        });

        /*
        FXGL.getPhysicsWorld().addCollisionHandler(new CollisionHandler(EntityType.PLAYER, EntityType.EXPLOSION) {
            @Override
            protected void onCollisionBegin(Entity player, Entity explosion) {
                PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
                if (playerComponent != null){
                    FXGL.showMessage("Joueur " + playerComponent.getId() + " est mort", () -> {
                        resetGame();
                    });
                }
            }
        });

         */

        onCollisionBegin(EntityType.PLAYER, EntityType.EXPLOSION, (player, obstacle) -> {
            PlayerComponent playerComponent = player.getComponent(PlayerComponent.class);
            if (playerComponent != null){
                FXGL.showMessage("Joueur " + playerComponent.getId() + " est mort", this::resetGame);
            }
            return null;
        });
    }

    public void resetGame() {
        playRandomMusic();

        //reset weapons spawns
        FXGL.getGameWorld().getEntitiesByType(EntityType.WEAPON).forEach(Entity::removeFromWorld);

        //reset players
        FXGL.getGameWorld().getEntitiesByType(EntityType.PLAYER).forEach(entity -> {
            PlayerComponent entityComponent = entity.getComponent(PlayerComponent.class);
            //reset players positions
            switch (entityComponent.getId()) {
                case 1 -> {
                    entity.setPosition((double) FXGL.getAppWidth() / 5 - 16*2, (double) FXGL.getAppHeight() / 2 - 28*2);
                    entityComponent.getEquippedWeapon().removeFromWorld();
                    FXGL.spawn("gun", (double) FXGL.getAppWidth() / 5 - 16*2, (double) FXGL.getAppHeight() / 2 - 28*2);
                }
                case 2 -> {
                    entity.setPosition(FXGL.getAppWidth() - (double) FXGL.getAppWidth() / 5 - 16 * 2, (double) FXGL.getAppHeight() / 2 - 28*2);
                    entityComponent.getEquippedWeapon().removeFromWorld();
                    FXGL.spawn("gun", FXGL.getAppWidth() - (double) FXGL.getAppWidth() / 5 - 16 * 2, (double) FXGL.getAppHeight() / 2 - 28*2);
                }
            }

            //reset players velocity
            entityComponent.resetKnockback();
        });

        //reset hazards spawns
        FXGL.getGameWorld().getEntitiesByType(EntityType.HAZARD).forEach(Entity::removeFromWorld);
        FXGL.getGameWorld().getEntitiesByType(EntityType.EXPLOSION).forEach(Entity::removeFromWorld);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

