package me.wurgo.practiceseedmod;

import java.lang.reflect.Field;

public class WorldConstants {
    public static boolean hasDroppedFlint = false;

    public static void reset() {
        for (Field field : WorldConstants.class.getDeclaredFields()) {
            if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                try { field.setBoolean(WorldConstants.class, false); }
                catch (IllegalAccessException ignored) {}
            }
        }
    }
}