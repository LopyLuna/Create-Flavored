package uwu.lopyluna.create_dd.content.items.equipment.handheld_nozzle;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.render.RenderTypes;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.DyeHelper;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import uwu.lopyluna.create_dd.DesireUtil;

import static java.lang.Math.max;
import static uwu.lopyluna.create_dd.DesireUtil.randomChance;

public class HandheldNozzleRenderer extends CustomRenderedItemModelRenderer {
    protected static final PartialModel ITEM = new PartialModel(DesireUtil.asResource("item/handheld_nozzle/item"));
    protected static final PartialModel GREEN = new PartialModel(DesireUtil.asResource("item/handheld_nozzle/glow_green"));
    protected static final PartialModel RED = new PartialModel(DesireUtil.asResource("item/handheld_nozzle/glow_red"));
    protected static final PartialModel GEAR = new PartialModel(DesireUtil.asResource("item/handheld_nozzle/gear"));

    private static final Vec3 GEAR_ROTATION_OFFSET = new Vec3(0, -3 / 16f, 1 / 16f);

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemTransforms.TransformType transformType,
                          PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        if (!(stack.getItem() instanceof HandheldNozzleItem item))
            return;
        CompoundTag nbt = stack.getOrCreateTag();

        TransformStack stacker = TransformStack.cast(ms);
        float worldTime = AnimationTickHolder.getRenderTime();

        boolean flag1 = item.getIsActive(nbt);
        boolean flag2 = item.getIsBlowing(nbt);

        renderer.render(ITEM.get(), light);

        ms.pushPose();
        Couple<Integer> couple = DyeHelper.DYE_TABLE.get(flag1 ? DyeColor.LIME : DyeColor.RED);
        int brightColor = couple.getFirst();
        int darkColor = couple.getSecond();
        int color = randomChance(25) ? darkColor : brightColor;

        float multiplier = -Mth.sin(worldTime * .25f % 360);
        int lightIntensity = (int) (15 * Mth.clamp(multiplier, 0.5, 1));
        int glowLight = LightTexture.pack(lightIntensity, max(lightIntensity, 4));
        BakedModel glow = flag1 ? GREEN.get() : RED.get();
        buffer.getBuffer(RenderTypes.getAdditive()).color(color);
        renderer.render(glow, RenderTypes.getAdditive(), glowLight);

        ms.popPose();

        ms.pushPose();
        float angle = worldTime * (flag1 ? 10 : .5f) % 360;
        stacker.translate(GEAR_ROTATION_OFFSET)
                .rotateZ(flag2 ? angle : -angle)
                .translateBack(GEAR_ROTATION_OFFSET);
        renderer.render(GEAR.get(), light);
        ms.popPose();



    }
}