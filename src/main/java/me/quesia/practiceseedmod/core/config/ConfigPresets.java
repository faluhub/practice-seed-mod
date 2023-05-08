package me.quesia.practiceseedmod.core.config;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ConfigPresets {
    public enum BarterSeedPresets {
        TIER_1("God Tier", new Long[] {
                -8732036818447716676L,
                -1884005847802675995L,
                -3312417286059183733L,
                1374525786593404439L,
                7352106512758557180L
        }),
        TIER_2("Above Average", new Long[] {
                3582757515363742853L,
                4726709422702595798L,
                -6012023186598150178L,
                4253907537979024729L,
                6092915669897044479L
        }),
        TIER_3("Average", new Long[] {
                -4869965656826902791L,
                -4036931787693754848L,
                -3835802711079067260L,
                -6817862731698137194L,
                -6951211317951024155L
        }),
        TIER_4("Below Average", new Long[] {
                7979390706502516491L,
                952429184568956194L,
                8053049648269309006L,
                -7170870497233978482L,
                2154379900888233237L
        }),
        TIER_5("Possibly Shit", new Long[] {
                -8428630386903600944L,
                -3810908670647857112L,
                7769790928494411656L,
                5181066584369975135L,
                -8104579087841319926L
        }),
        VANILLA("Vanilla", null);

        private final String cosmeticName;
        private final @Nullable List<Long> seeds;

        BarterSeedPresets(String cosmeticName, @Nullable Long[] seeds) {
            this.cosmeticName = cosmeticName;
            if (seeds != null) { this.seeds = List.of(seeds); }
            else { this.seeds = new ArrayList<>(); }
        }

        public String getCosmeticName() {
            return this.cosmeticName;
        }

        public @Nullable List<Long> getSeeds() {
            return this.seeds;
        }
    }

    public enum DragonPerchTimes {
        FAST("Fast", 30),
        SPEEDY("Speedy", 45),
        AVERAGE("Average", 70),
        SLOW("Slow", 120),
        ILLUMINA("Illumina", 500);

        private final String cosmeticName;
        private final int seconds;

        DragonPerchTimes(String cosmeticName, int seconds) {
            this.cosmeticName = cosmeticName;
            this.seconds = seconds;
        }

        public String getCosmeticName() {
            return this.cosmeticName;
        }

        public int getSeconds() {
            return this.seconds;
        }
    }
}
