package uwu.lopyluna.create_dd.registry.destinies;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Rarity;
import uwu.lopyluna.create_dd.content.items.materials.BlazeGold;
import uwu.lopyluna.create_dd.registry.DesiresCreativeModeTabs;

import static uwu.lopyluna.create_dd.Desires.REGISTRATE;

@SuppressWarnings("unused")
public class ExtrasItems {
    static {
        REGISTRATE.creativeModeTab(() -> DesiresCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static final ItemEntry<BlazeGold> BLAZE_GOLD =
            REGISTRATE.item("blaze_gold", BlazeGold::new)
                    .properties(p -> p.stacksTo(16)
                            .rarity(Rarity.UNCOMMON)
                            .fireResistant())
                    .register();

    public static final ItemEntry<BlazeGold> BLAZE_GOLD_SHEET =
            REGISTRATE.item("blaze_gold_sheet", BlazeGold::new)
                    .properties(p -> p.stacksTo(16)
                            .rarity(Rarity.UNCOMMON)
                            .fireResistant())
                    .register();

    public static void register() {}
}
