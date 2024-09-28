package uwu.lopyluna.create_dd.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import static uwu.lopyluna.create_dd.Desires.MOD_ID;
import static uwu.lopyluna.create_dd.registry.DesiresTags.optionalTag;

@SuppressWarnings({"unchecked"})
public class DTags {
    public static <T> TagKey<T> tag(String type, String id) {
        return ("item".equalsIgnoreCase(type))  ? (TagKey<T>) optionalTag(Registry.ITEM_REGISTRY,  new ResourceLocation(MOD_ID, id.toLowerCase())) :
                ("block".equalsIgnoreCase(type))  ? (TagKey<T>) optionalTag(Registry.BLOCK_REGISTRY, new ResourceLocation(MOD_ID, id.toLowerCase())) :
                        ("fluid".equalsIgnoreCase(type))  ? (TagKey<T>) optionalTag(Registry.FLUID_REGISTRY, new ResourceLocation(MOD_ID, id.toLowerCase())) :
                                null;
    }
    public static <T> TagKey<T> tag(String type, String mod_id, String id) {
        return ("item".equalsIgnoreCase(type))  ? (TagKey<T>) optionalTag(Registry.ITEM_REGISTRY,  new ResourceLocation(mod_id.toLowerCase(), id.toLowerCase())) :
                ("block".equalsIgnoreCase(type))  ? (TagKey<T>) optionalTag(Registry.BLOCK_REGISTRY, new ResourceLocation(mod_id.toLowerCase(), id.toLowerCase())) :
                        ("fluid".equalsIgnoreCase(type))  ? (TagKey<T>) optionalTag(Registry.FLUID_REGISTRY, new ResourceLocation(mod_id.toLowerCase(), id.toLowerCase())) :
                                null;
    }
}
