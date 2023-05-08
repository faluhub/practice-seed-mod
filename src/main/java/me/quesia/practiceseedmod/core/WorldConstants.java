package me.quesia.practiceseedmod.core;

import java.lang.reflect.Field;
import java.time.Instant;

public class WorldConstants {
    public static boolean HAS_PERCHED = false;
    public static Instant END_ENTER_TIMESTAMP;

    public static void reset() {
        for (Field field : WorldConstants.class.getDeclaredFields()) {
            if (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class)) {
                try { field.setBoolean(WorldConstants.class, false); }
                catch (IllegalAccessException ignored) {}
            } else if (field.getType().equals(int.class) || field.getType().equals(Integer.class)) {
                try { field.setInt(WorldConstants.class, 0); }
                catch (IllegalAccessException ignored) {}
            } else {
                try { field.set(WorldConstants.class, null); }
                catch (IllegalAccessException ignored) {}
            }
        }
    }
}
