//package com.arena.dual_arena.factories;
//
//import com.almasb.fxgl.dsl.FXGL;
//import com.almasb.fxgl.entity.Entity;
//import com.almasb.fxgl.texture.AnimatedTexture;
//import com.arena.dual_arena.FXGLTestBase;
//import com.arena.dual_arena.components.PlayerAnimationComponent;
//import com.arena.dual_arena.components.PlayerComponent;
//import javafx.application.Platform;
//import javafx.geometry.Point2D;
//import javafx.scene.input.KeyCode;
//import javafx.util.Duration;
//import net.bytebuddy.build.Plugin;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.concurrent.atomic.AtomicReference;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class PlayerAnimationComponentTest extends FXGLTestBase {
//
//    private PlayerAnimationComponent animationComponent;
//    private Entity entity;
//    private PlayerComponent playerComponent;
//    // Définition des contrôles pour le test
//    private static final KeyCode TEST_UP = KeyCode.W;
//    private static final KeyCode TEST_DOWN = KeyCode.S;
//    private static final KeyCode TEST_LEFT = KeyCode.A;
//    private static final KeyCode TEST_RIGHT = KeyCode.D;
//    private static final KeyCode TEST_PRIMARY_FIRE = KeyCode.J;
//    private static final KeyCode TEST_SECONDARY_FIRE = KeyCode.K;
//    private static final KeyCode TEST_DROP_WEAPON = KeyCode.L;
//
//
//
//    @BeforeEach
//    public void setUp() {
//        playerComponent = new PlayerComponent(TEST_UP, TEST_DOWN, TEST_LEFT, TEST_RIGHT, TEST_PRIMARY_FIRE, TEST_SECONDARY_FIRE, TEST_DROP_WEAPON);
//        // Ajoute les composants à l'entité
////        entity.addComponent(playerComponent);
////        entity.addComponent(animationComponent);
//    }
//
//    @Test
//    public void testPlayerControlsAssignment() {
//        // Vérifier que les contrôles sont correctement assignés
//        assertEquals(TEST_UP, playerComponent.getInputUp(), "La touche haut devrait être W");
//        assertEquals(TEST_DOWN, playerComponent.getInputDown(), "La touche bas devrait être S");
//        assertEquals(TEST_LEFT, playerComponent.getInputLeft(), "La touche gauche devrait être A");
//        assertEquals(TEST_RIGHT, playerComponent.getInputRight(), "La touche droite devrait être D");
//        assertEquals(TEST_PRIMARY_FIRE, playerComponent.getPrimaryFire(), "La touche de tir primaire devrait être J");
//        assertEquals(TEST_SECONDARY_FIRE, playerComponent.getSecondaryFire(), "La touche de tir secondaire devrait être K");
//        assertEquals(TEST_DROP_WEAPON, playerComponent.getDropWeapon(), "La touche pour lâcher l'arme devrait être L");
//    }
//
//    @Test
//    public void testPlayerIdIncrement() {
//        // Créer plusieurs joueurs et vérifier que leurs IDs sont uniques et incrémentés
//        PlayerComponent player1 = new PlayerComponent(TEST_UP, TEST_DOWN, TEST_LEFT, TEST_RIGHT,
//                TEST_PRIMARY_FIRE, TEST_SECONDARY_FIRE, TEST_DROP_WEAPON);
//        PlayerComponent player2 = new PlayerComponent(TEST_UP, TEST_DOWN, TEST_LEFT, TEST_RIGHT,
//                TEST_PRIMARY_FIRE, TEST_SECONDARY_FIRE, TEST_DROP_WEAPON);
//
//        assertNotEquals(player1.getId(), player2.getId(), "Les IDs des joueurs devraient être différents");
//        assertEquals(player1.getId() + 1, player2.getId(), "L'ID devrait être incrémenté de 1");
//    }
//
//    @Test
//    public void testMovementSpeed() {
//        AtomicReference<Double> initialX = new AtomicReference<>(0.0);
//        AtomicReference<Double> initialY = new AtomicReference<>(0.0);
//
//        // Test du mouvement vers la droite
//        playerComponent.setXvalue(1.0);
//        animationComponent.moveRight();
//        animationComponent.onUpdate(1.0/60.0);
//
//        double newX = entity.getTransformComponent().getX();
//        assertTrue(newX > initialX.get(), "Le joueur devrait se déplacer vers la droite");
//
//        // Réinitialiser la position
//        entity.getTransformComponent().setX(0);
//        entity.getTransformComponent().setY(0);
//
//        // Test du mouvement vers la gauche
//        playerComponent.setXvalue(-1.0);
//        animationComponent.moveLeft();
//        animationComponent.onUpdate(1.0/60.0);
//
//        newX = entity.getTransformComponent().getX();
//        assertTrue(newX < initialX.get(), "Le joueur devrait se déplacer vers la gauche");
//    }
//
//    @Test
//    public void testDeceleration() {
//        // Position initiale
//        entity.getTransformComponent().setX(0);
//        entity.getTransformComponent().setY(0);
//
//        // Appliquer un mouvement
//        playerComponent.setXvalue(1.0);
//        animationComponent.moveRight();
//
//        // Premier update
//        animationComponent.onUpdate(1.0/60.0);
//        double speedAfterFirstUpdate = entity.getTransformComponent().getX();
//
//        // Plusieurs updates pour voir la décélération
//        for (int i = 0; i < 60; i++) {
//            animationComponent.onUpdate(1.0/60.0);
//        }
//
//        double finalPosition = entity.getTransformComponent().getX();
//
//        // La vitesse finale devrait être plus petite que la vitesse initiale
//        assertTrue(finalPosition > speedAfterFirstUpdate,
//                "Le joueur devrait continuer à bouger mais décélérer");
//    }
//
//    @Test
//    public void testSpeedLimits() {
//        // Test des limites de vitesse
//        playerComponent.setXvalue(2.0); // Valeur supérieure à la normale
//        animationComponent.moveRight();
//        animationComponent.onUpdate(1.0/60.0);
//
//        double position1 = entity.getTransformComponent().getX();
//
//        // Réinitialiser
//        entity.getTransformComponent().setX(0);
//
//        playerComponent.setXvalue(1.0); // Valeur normale
//        animationComponent.moveRight();
//        animationComponent.onUpdate(1.0/60.0);
//
//        double position2 = entity.getTransformComponent().getX();
//
//        // La différence ne devrait pas être proportionnelle à la différence d'input
//        assertTrue(position1 <= position2 * 2,
//                "La vitesse devrait être limitée même avec un input plus grand");
//    }
//
//    @Test
//    public void testEquippedWeaponEffect() {
//        // Test du comportement sans arme
//        playerComponent.setXvalue(1.0);
//        animationComponent.moveRight();
//        double scaleWithoutWeapon = entity.getScaleX();
//
//        // Vérifier que l'orientation change sans arme
//        assertTrue(scaleWithoutWeapon != 1,
//                "Le sprite devrait changer d'orientation sans arme équipée");
//    }
//}