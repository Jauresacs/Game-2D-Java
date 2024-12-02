package com.arena.dual_arena.factories;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.arena.dual_arena.components.PlayerComponent;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerMovementTest {

    private static PlayerFactory playerFactory;

    private static Entity player;
    private WeaponFactory weaponFactory;
    private static PlayerComponent playerComponent;

    @BeforeAll
    static void setUp() {
        SpawnData spawnData2 = new SpawnData(300, 400);
        spawnData2.put("posX", 300);
        spawnData2.put("posY", 400);
        spawnData2.put("inputUp", KeyCode.I);
        spawnData2.put("inputDown", KeyCode.K);
        spawnData2.put("inputLeft", KeyCode.J);
        spawnData2.put("inputRight", KeyCode.L);
        spawnData2.put("primaryFire", KeyCode.ENTER);
        spawnData2.put("secondaryFire", KeyCode.RIGHT);
        spawnData2.put("dropWeapon", KeyCode.P);
        playerFactory = new PlayerFactory();
        player = playerFactory.spawnPlayer(spawnData2);
        playerFactory = new PlayerFactory();

        playerComponent = player.getComponent(PlayerComponent.class);



    }

    @Test
    void testMoveUp() {
        playerComponent.setYvalue(1); // Valeur négative pour aller vers le haut
        playerComponent.move(); // Appelle la mise à jour avec un tpf (time per frame)

        // Vérifie que la position Y a diminué
        assertEquals(398, player.getY(),
                "La position Y devrait diminuer après un mouvement vers le haut.");
    }

    @Test
    void testMoveDown() {
        playerComponent.setYvalue(-1); // Valeur positive pour aller vers le bas
        playerComponent.move();

        assertEquals(398, player.getY(),
                "La position Y devrait augmenter après un mouvement vers le bas.");
    }

    @Test
    void testMoveLeft() {
        playerComponent.setXvalue(-1);
        playerComponent.move();

        assertEquals(298, player.getX(),
                "La position X devrait diminuer après un mouvement vers la gauche.");
    }

    @Test
    void testMoveRight() {
        playerComponent.setXvalue(1); // Valeur positive pour aller vers la droite
        playerComponent.move();

        assertEquals(298 , player.getX(),
                "La position X devrait augmenter après un mouvement vers la droite.");
    }
}
