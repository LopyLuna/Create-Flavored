package uwu.lopyluna.create_dd.content.items.equipment.block_zapper;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.equipment.zapper.ZapperItemRenderer;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

import static java.lang.Math.max;
import static uwu.lopyluna.create_dd.DesireUtil.asResource;

public class BlockZapperItemRenderer extends ZapperItemRenderer {

	protected static final PartialModel SCOPE = new PartialModel(asResource("item/handheld_block_zapper/scope"));
	protected static final PartialModel CHARGE = new PartialModel(asResource("item/handheld_block_zapper/charge"));
	protected static final PartialModel CONVERTER = new PartialModel(asResource("item/handheld_block_zapper/converter"));
	protected static final PartialModel BULK = new PartialModel(asResource("item/handheld_block_zapper/bulk"));
	protected static final PartialModel CORE_BULK = new PartialModel(asResource("item/handheld_block_zapper/core_bulk"));
	protected static final PartialModel CORE_GLOW_BULK = new PartialModel(asResource("item/handheld_block_zapper/core_glow_bulk"));

	protected static final PartialModel CORE = new PartialModel(asResource("item/handheld_block_zapper/core"));
	protected static final PartialModel CORE_GLOW = new PartialModel(asResource("item/handheld_block_zapper/core_glow"));
	protected static final PartialModel ACCELERATOR = new PartialModel(asResource("item/handheld_block_zapper/accelerator"));

	@Override
	protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemTransforms.TransformType transformType,
		PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		super.render(stack, model, renderer, transformType, ms, buffer, light, overlay);

		float pt = AnimationTickHolder.getPartialTicks();
		float worldTime = AnimationTickHolder.getRenderTime() / 20;

		if (!(stack.getItem() instanceof BlockZapperItem item))
			return;

		renderer.renderSolid(model.getOriginalModel(), light);

		CompoundTag nbt = stack.getOrCreateTag();
		if (item.getRangeModifier(nbt) > 0) {
			renderer.renderSolid(SCOPE.get(), light);
		}
		if (item.getSpeedModifier(nbt) > 0) {
			renderer.renderSolid(CHARGE.get(), light);
		}
		if (item.getSizeModifier(nbt)) {
			renderer.renderSolid(BULK.get(), light);
		}
		if (item.getBreakerModifier(nbt)) {
			renderer.renderSolid(CONVERTER.get(), light);
		}


		LocalPlayer player = Minecraft.getInstance().player;
        assert player != null;
        boolean leftHanded = player.getMainArm() == HumanoidArm.LEFT;
		boolean mainHand = player.getMainHandItem() == stack;
		boolean offHand = player.getOffhandItem() == stack;
		float animation = (getAnimationProgress(pt, leftHanded, mainHand) * 0.25f)  * item.getSpeedModifier(nbt);

		// Core glows
		float multiplier;
		if (mainHand || offHand) 
			multiplier = animation;
		else
			multiplier = Mth.sin(worldTime * 5);

		int lightItensity = (int) (15 * Mth.clamp(multiplier, 0, 1));
		int glowLight = LightTexture.pack(lightItensity, max(lightItensity, 4));
		if (item.getSizeModifier(nbt)) {
			renderer.renderSolidGlowing(CORE_BULK.get(), glowLight);
			renderer.renderGlowing(CORE_GLOW_BULK.get(), glowLight);
		}
		renderer.renderSolidGlowing(CORE.get(), glowLight);
		renderer.renderGlowing(CORE_GLOW.get(), glowLight);

		// Accelerator spins
		float angle = worldTime * -25;
		if (mainHand || offHand)
			angle += 360 * animation;

		angle %= 360;
		float offset = -.155f;
		ms.translate(0, offset, 0);
		ms.mulPose(Vector3f.ZP.rotationDegrees(angle));
		ms.translate(0, -offset, 0);
		renderer.render(ACCELERATOR.get(), light);
	}

}
