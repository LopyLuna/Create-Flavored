package uwu.lopyluna.create_dd.registry;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SimpleFoiledItem;

import static uwu.lopyluna.create_dd.Desires.REGISTRATE;

@SuppressWarnings({"unused"})
public class ClassicItems {

    static {
        REGISTRATE.creativeModeTab(() -> DesiresCreativeModeTabs.CLASSIC_CREATIVE_TAB);
    }

    public static final ItemEntry<Item>
            integrated_circuit = ingredient("integrated_circuit"),
            integrated_mechanism = ingredient("integrated_mechanism"),
            calculation_mechanism = ingredient("calculation_mechanism"),
            infernal_mechanism = ingredient("infernal_mechanism"),
            sealed_mechanism = ingredient("sealed_mechanism"),
            abstruse_mechanism = ingredient("abstruse_mechanism");

    public static final ItemEntry<SequencedAssemblyItem>
            incomplete_integrated_circuit = sequencedIngredient("incomplete_integrated_circuit"),
            incomplete_integrated_mechanism = sequencedIngredient("incomplete_integrated_mechanism"),
            incomplete_abstruse_mechanism = sequencedIngredient("incomplete_abstruse_mechanism"),
            incomplete_calculation_mechanism = sequencedIngredient("incomplete_calculation_mechanism"),
            incomplete_infernal_mechanism = sequencedIngredient("incomplete_infernal_mechanism"),
            incomplete_sealed_mechanism = sequencedIngredient("incomplete_sealed_mechanism");

    private static ItemEntry<Item> ingredient(String name) {
        return REGISTRATE.item(name, Item::new)
                .register();
    }

    private static ItemEntry<SimpleFoiledItem> foilIngredient(String name) {
        return REGISTRATE.item(name, SimpleFoiledItem::new)
                .register();
    }

    private static ItemEntry<SequencedAssemblyItem> sequencedIngredient(String name) {
        return REGISTRATE.item(name, SequencedAssemblyItem::new)
                .register();
    }

    @SafeVarargs
    private static ItemEntry<Item> taggedIngredient(String name, TagKey<Item>... tags) {
        return REGISTRATE.item(name, Item::new)
                .tag(tags)
                .register();
    }

    public static void register() {}
}
