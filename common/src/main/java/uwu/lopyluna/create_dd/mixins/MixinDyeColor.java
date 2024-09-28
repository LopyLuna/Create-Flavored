package uwu.lopyluna.create_dd.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.MaterialColor;
import uwu.lopyluna.create_dd.access.AccessDyeColor;

@Mixin(DyeColor.class)
public class MixinDyeColor implements AccessDyeColor {
	@Shadow
	@Final
	private String name;
	@Unique
	private TagKey<Item> tag;

	@Inject(method = "<init>", at = @At("TAIL"))
	public void addTag(String woolId, int id, int color, String mapColor, int fireworkColor, MaterialColor signColor, int j, int k, CallbackInfo ci) {
		tag = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("c", name + "_dyes"));
	}

	@Override
	public TagKey<Item> getTag() {
		return tag;
	}
}
