package me.wurgo.practiceseedmod.core.config;

import net.minecraft.client.gui.screen.Screen;

public class ConfigUtils {
    public static int compareAB(int a, int b, int limit) {
        int value = !Screen.hasShiftDown() ? a + 1 : a - 1;
        if (value < 0) { value = b; }
        else if (value > limit || value > b) { value = 0; }
        return value;
    }

    public static int compareBA(int b, int a, int limit) {
        int value = !Screen.hasShiftDown() ? b + 1 : b - 1;
        if (value < -1) { value = a; }
        else if (value > limit) { value = -1; }
        else if (value < a && value != -1) {
            if (b < value) { value = a; }
            else { value = -1; }
        }
        return value;
    }
}
