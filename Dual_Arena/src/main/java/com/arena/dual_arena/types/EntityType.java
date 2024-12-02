package com.arena.dual_arena.types;

/**
 * Enumerates different types of entities used in the game.
 * Each entity type represents a specific category of objects or areas within the game environment,
 * helping in the management and interaction of game elements.
 */
public enum EntityType {
    /** Represents a layer of the game map composed of tiles. */
    TiledMapLayer,

    /** Represents an explosion, typically used for visual effects and damage calculations. */
    EXPLOSION,

    /** Represents a hazard in the game, such as traps or areas that can cause damage to players. */
    HAZARD,

    /** Represents a player character or enemy controlled by AI. */
    PLAYER,

    /** Represents a weapon that can be used by players or AI characters. */
    WEAPON,

    /** Represents a projectile fired by a weapon. */
    PROJECTILE,

    /** Represents water terrain in the game, possibly affecting player movement or behavior. */
    WATER,

    /** Represents grass terrain in the game, which might affect visibility or movement. */
    GRASS,

    /** Represents the top-left part of a larger entity or area. */
    TOP_LEFT,

    /** Represents the top-middle part of a larger entity or area. */
    TOP_MID,

    /** Represents the top-right part of a larger entity or area. */
    TOP_RIGHT,

    /** Represents the middle-left part of a larger entity or area. */
    MID_LEFT,

    /** Represents the middle-right part of a larger entity or area. */
    MID_RIGHT,

    /** Represents the bottom-left part of a larger entity or area. */
    BOTTOM_LEFT,

    /** Represents the bottom-middle part of a larger entity or area. */
    BOTTOM_MID,

    /** Represents the bottom-right part of a larger entity or area. */
    BOTTOM_RIGHT
}
