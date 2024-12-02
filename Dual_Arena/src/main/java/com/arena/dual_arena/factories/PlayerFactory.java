package com.arena.dual_arena.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.arena.dual_arena.components.PlayerAnimationComponent;
import com.arena.dual_arena.components.PlayerComponent;
import com.arena.dual_arena.types.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

/**
 * Factory class for creating player entities in the game.
 * Utilizes the FXGL framework to construct players with specific configurations and components based on provided spawn data.
 */
public class PlayerFactory implements EntityFactory {

    /**
     * Spawns a player entity using the provided spawn data.
     * This method extracts player configuration such as position and control keys from the spawn data.
     *
     * @param data The spawn data containing player configuration details like position and control keys.
     * @return A newly created player entity with specified properties.
     */
    @Spawns("player")
    public Entity spawnPlayer(SpawnData data) {
        System.out.println("Player data: " + data.getData());
        int posX = data.get("posX");
        int posY = data.get("posY");
        KeyCode inputUp = data.get("inputUp");
        KeyCode inputDown = data.get("inputDown");
        KeyCode inputLeft = data.get("inputLeft");
        KeyCode inputRight = data.get("inputRight");
        KeyCode primaryFire = data.get("primaryFire");
        KeyCode secondaryFire = data.get("secondaryFire");
        KeyCode dropWeapon = data.get("dropWeapon");
        return createPlayer(posX, posY, inputUp, inputDown, inputLeft, inputRight, primaryFire, secondaryFire, dropWeapon);
    }

    /**
     * Constructs the player entity with specific characteristics and components based on the provided parameters.
     * Includes positioning, collision settings, and input control setup.
     *
     * @param posX X-coordinate for the player's initial position.
     * @param posY Y-coordinate for the player's initial position.
     * @param inputUp Key code for moving the player up.
     * @param inputDown Key code for moving the player down.
     * @param inputLeft Key code for moving the player left.
     * @param inputRight Key code for moving the player right.
     * @param primaryFire Key code for the player's primary fire action.
     * @param secondaryFire Key code for the player's secondary fire action.
     * @param dropWeapon Key code for the action to drop weapons.
     * @return The fully constructed player entity.
     */
    private Entity createPlayer(int posX, int posY, KeyCode inputUp, KeyCode inputDown, KeyCode inputLeft, KeyCode inputRight, KeyCode primaryFire, KeyCode secondaryFire, KeyCode dropWeapon) {
        return FXGL.entityBuilder()
                .at(posX, posY)
                .type(EntityType.PLAYER)
                .bbox(new HitBox("BOTTOM_BOX",
                        new Point2D(5, 15), // 5, 15
                        BoundingShape.box(15*2 - 5*2, 28*2 - 25))) // 15*2 - 5*2, 28*2 - 25
                .collidable()
                .with(new PlayerComponent(inputUp, inputDown, inputLeft, inputRight, primaryFire, secondaryFire, dropWeapon))
                .with(new PlayerAnimationComponent())
                .build();
    }
}
