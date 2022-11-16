package me.wurgo.practiceseedmod.core;

import java.util.Random;

public class RandomSeedGenerator {
    private int getBlazeRods(long seed, int kills) {
        Random random = new Random(seed);
        int rods = 0;
        for (int i = 0; i < kills; i++) {
            if (random.nextInt(2) == 0) {
                rods++;
            }
        }
        return rods;
    }

    private boolean hasEnoughRods(long seed, int rods, int kills) {
        return this.getBlazeRods(seed, kills) == rods;
    }

    public long getBlazeDropSeed(int rods, int kills) {
        Random random = new Random();
        while (true) {
            long seed = random.nextLong();
            if (this.hasEnoughRods(seed, rods, kills)) {
                return seed;
            }
        }
    }
}
