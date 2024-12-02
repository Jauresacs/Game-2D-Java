package com.arena.dual_arena.utils;

public class DebugMode {
    private static boolean isDebugMode = false;

    public static boolean isDebugMode() {
        return isDebugMode;
    }

    public static void toggleDebugMode() {
        isDebugMode = !isDebugMode;
    }
}
