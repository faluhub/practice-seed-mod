package me.wurgo.practiceseedmod.core;

import java.util.Random;

public class RandomSeedGenerator {
    private int getPassesForIterations(long seed, int iterations) {
        Random random = new Random(seed);
        int passes = 0;
        for (int i = 0; i < iterations; i++) {
            if (random.nextInt(2) == 0) {
                passes++;
            }
        }
        return passes;
    }

    private boolean hasEnoughPasses(long seed, int requiredPasses, int iterations) {
        return this.getPassesForIterations(seed, iterations) == requiredPasses;
    }

    public long getSeed(int requiredPasses, int iterations) {
        Random random = new Random();
        while (true) {
            long seed = random.nextLong();
            if (this.hasEnoughPasses(seed, requiredPasses, iterations)) {
                return seed;
            }
        }
    }
}
