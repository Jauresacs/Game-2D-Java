module com.arena.dual_arena {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires jdk.jconsole;
    requires jinput;
    requires com.almasb.fxgl.entity;
    requires de.ralleytn.plugins.jinput.xinput;

    opens com.arena.dual_arena to javafx.fxml;
    exports com.arena.dual_arena;


    opens com.arena.dual_arena.components;
    opens com.arena.dual_arena.models.weapons;
    opens com.arena.dual_arena.models.projectiles;
    opens com.arena.dual_arena.factories;
    opens com.arena.dual_arena.types;
    opens assets.levels;
    opens assets.textures;
    opens assets.sounds;
    opens assets.music;
}