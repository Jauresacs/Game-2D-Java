package com.arena.dual_arena.factories;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.arena.dual_arena.types.EntityType;

/**
 * Factory class responsible for creating environmental features within the game's arena.
 * This includes spawning entities such as water areas that affect gameplay.
 */
public class ArenaFactory implements EntityFactory {

    /**
     * Spawns a water entity using the provided spawn data.
     * The water entity is scaled to fit the game's arena dimensions, ensuring it integrates seamlessly into the game environment.
     *
     * @param data The spawn data containing parameters such as width and height for the water entity.
     * @return A newly created water entity that can interact with other game elements.
     */
    @Spawns("water")
    public Entity spawnWater(SpawnData data) {
        int mapWidth = 800;
        int mapHeight = 448;

        double scaleX = (double) FXGL.getAppWidth() / mapWidth; // Fit to window width
        double scaleY = (double) FXGL.getAppHeight() / mapHeight; // Fit to window height
        return FXGL.entityBuilder(data)
                .type(EntityType.WATER)
                .bbox(new HitBox(BoundingShape.box(data.<Integer>get("width") * scaleX, data.<Integer>get("height") * scaleY)))
                .with(new CollidableComponent(true))
                .build();
    }
}
