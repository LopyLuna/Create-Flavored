package uwu.lopyluna.create_dd.access;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface AccessDyeColor {
    default TagKey<Item> getTag() {
		return null;
	}
}
