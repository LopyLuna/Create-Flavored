package uwu.lopyluna.create_dd.registry;

import java.util.function.BiConsumer;

@SuppressWarnings({"unused"})
public class DesiresLangPartial {
    public static void provideLang(BiConsumer<String, String> consumer) {
        consume(consumer, "item.create_dd.handheld_block_zapper.tooltip.behaviour1", "_Targeted block_ will become the _material_ placed by the shaper.");
        consume(consumer, "item.create_dd.handheld_block_zapper.tooltip.behaviour2", "Applies currently selected _Brush_ and _Tool_ at the targeted location.");
        consume(consumer, "item.create_dd.handheld_block_zapper.tooltip.behaviour3", "Opens the _Configuration Interface_");
        consume(consumer, "item.create_dd.handheld_block_zapper.tooltip.condition1", "L-Click at Block");
        consume(consumer, "item.create_dd.handheld_block_zapper.tooltip.condition2", "R-Click at Block");
        consume(consumer, "item.create_dd.handheld_block_zapper.tooltip.condition3", "R-Click while Sneaking");
        consume(consumer, "item.create_dd.handheld_block_zapper.tooltip.summary", "_Creative mode_ tool for large-scale _landscaping_ from a distance.");


        consume(consumer, "create.menu.discord", "Discord Server");


        consume(consumer, "create_dd.block_zapper.modifiers", "Modifiers:");
        consume(consumer, "create_dd.block_zapper.no_modifiers", "You have no modifiers applied.");

        consume(consumer, "create_dd.block_zapper.ctrl", " for Modifiers");
        consume(consumer, "create_dd.tooltip.holdForModifiers", "Hold ");

        consume(consumer, "create_dd.block_zapper.breaker_modifier", "Breaker");
        consume(consumer, "create_dd.block_zapper.breaker_modifier.desc", "Drops blocks when clearing & replacing.");

        consume(consumer, "create_dd.block_zapper.void_notice", "Blocks will be voided when clearing & replacing!");
        consume(consumer, "create_dd.block_zapper.breaker_notice", "Apply the Breaker Modifier to obtain blocks when destroy.");

        consume(consumer, "create_dd.block_zapper.size_modifier", "Bulking");
        consume(consumer, "create_dd.block_zapper.size_modifier.desc", "Can place 2x more blocks.")
        ;
        consume(consumer, "create_dd.block_zapper.range_modifier", "Range");
        consume(consumer, "create_dd.block_zapper.range_modifier.desc", "Can place blocks farther up to ");
        consume(consumer, "create_dd.block_zapper.range_modifier.desc2", " blocks.");

        consume(consumer, "create_dd.block_zapper.speed_modifier", "Quick Charging");
        consume(consumer, "create_dd.block_zapper.speed_modifier.desc", "Reduce the cooldown to ");
        consume(consumer, "create_dd.block_zapper.speed_modifier.desc2", " seconds.");

        consume(consumer, "create_dd.block_zapper.need_upgrade_below", "Need Upgrade before this Tier!");
        consume(consumer, "create_dd.block_zapper.too_much", "Too much levels for this modifier!");
        consume(consumer, "create_dd.block_zapper.not_enough_blocks", "Not holding enough selected blocks!");
        consume(consumer, "create_dd.recipe.fan_sanding.fan", "Fan behind Sand");
        consume(consumer, "create_dd.recipe.fan_freezing.fan", "Fan behind Powdered Snow");
        consume(consumer, "create_dd.recipe.fan_seething.fan", "Fan behind Super Heated Blaze Burner");
        consume(consumer, "create_dd.recipe.fan_sanding", "Bulk Sanding");
        consume(consumer, "create_dd.recipe.fan_freezing", "Bulk Freezing");
        consume(consumer, "create_dd.recipe.fan_seething", "Bulk Seething");
        consume(consumer, "create_dd.recipe.hydraulic_compacting", "Hydraulic Compacting");
        consume(consumer, "itemGroup.create_dd.base", "Create: Dreams n' Desires");
        consume(consumer, "itemGroup.create_dd.palettes", "DnDesires Building Blocks");
        consume(consumer, "itemGroup.create_dd.beta", "DnDesires Beta Stuff");
        consume(consumer, "itemGroup.create_dd.classic", "DnDesires Classic Stuff");

    }

    private static void consume(BiConsumer<String, String> consumer, String key, String enUS) {
        consumer.accept(key, enUS);
    }
}
