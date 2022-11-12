package me.wurgo.practiceseedmod.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigPresets {
    public enum BarterSeedPresets {
        TIER_1("God Tier", new String[] {
                "-8732036818447716676",
                "-1884005847802675995",
                "-3312417286059183733",
                "1374525786593404439",
                "7352106512758557180"
        }),
        TIER_2("Above Average", new String[] {
                "3582757515363742853",
                "4726709422702595798",
                "-6012023186598150178",
                "4253907537979024729",
                "6092915669897044479"
        }),
        TIER_3("Average", new String[] {
                "-4869965656826902791",
                "-4036931787693754848",
                "-3835802711079067260",
                "-6817862731698137194",
                "-6951211317951024155"
        }),
        TIER_4("Below Average", new String[] {
                "7979390706502516491",
                "952429184568956194",
                "8053049648269309006",
                "-7170870497233978482",
                "2154379900888233237"
        }),
        TIER_5("Possibly Shit", new String[] {
                "-8428630386903600944",
                "-3810908670647857112",
                "7769790928494411656",
                "5181066584369975135",
                "-8104579087841319926"
        });

        public final String cosmeticName;
        public final List<Long> seeds;

        BarterSeedPresets(String cosmeticName, String[] seeds) {
            this.cosmeticName = cosmeticName;
            this.seeds = new ArrayList<>();
            for (String seed : seeds) {
                this.seeds.add(Long.parseLong(seed));
            }
        }
    }
}
