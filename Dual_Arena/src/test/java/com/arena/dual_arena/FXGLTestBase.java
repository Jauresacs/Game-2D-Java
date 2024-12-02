package com.arena.dual_arena;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FXGLTestBase {

    private static boolean fxglStarted = false;

    @BeforeAll
    public static void setupFXGL() throws Exception {
        if (!fxglStarted) {
            CountDownLatch latch = new CountDownLatch(1);

            Platform.startup(() -> {
                new Thread(() -> {
                    GameApplication.launch(FXGLTestApp.class, new String[]{});
                    latch.countDown();
                }).start();
            });

            latch.await(); // Attend que FXGL soit démarré
            fxglStarted = true;
        }
    }

    public static class FXGLTestApp extends GameApplication {
        @Override
        protected void initSettings(GameSettings settings) {
            settings.setWidth(1024);
            settings.setHeight(576);
            settings.setTitle("Test App");
            settings.setVersion("0.1");
        }
    }
}

