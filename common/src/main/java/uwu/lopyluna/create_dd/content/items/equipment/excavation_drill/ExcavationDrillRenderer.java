package uwu.lopyluna.create_dd.content.items.equipment.excavation_drill;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import uwu.lopyluna.create_dd.DesireUtil;
import uwu.lopyluna.create_dd.infrastructure.config.DesiresConfigs;

public class ExcavationDrillRenderer extends CustomRenderedItemModelRenderer {
    protected static final PartialModel ITEM = new PartialModel(DesireUtil.asResource("item/excavation_drill/item"));
    protected static final PartialModel HEAD = new PartialModel(DesireUtil.asResource("item/excavation_drill/head"));
    protected static final PartialModel GEAR = new PartialModel(DesireUtil.asResource("item/excavation_drill/gear"));

    private static final Vec3 HEAD_ROTATION_OFFSET = new Vec3(0, -4 / 16f, -7 / 16f);
    private static final Vec3 GEAR_ROTATION_OFFSET = new Vec3(0, -3 / 16f, 1 / 16f);

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemTransforms.TransformType transformType,
                          PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        boolean playerHeldAttack = mc.options.keyAttack.isDown();
        assert mc.level != null;
        boolean playerHeldShift = mc.level.isClientSide() ? DesiresConfigs.client().invertExcavationDrillFunction.get() != mc.options.keyShift.isDown() : mc.options.keyShift.isDown();
        TransformStack stacker = TransformStack.cast(ms);
        float worldTime = AnimationTickHolder.getRenderTime();
        renderer.render(ITEM.get(), light);

        ms.pushPose();
        float angle = worldTime * .5f % 360;
        stacker.translate(GEAR_ROTATION_OFFSET)
                .rotateZ(angle)
                .translateBack(GEAR_ROTATION_OFFSET);
        renderer.render(GEAR.get(), light);
        ms.popPose();

        ms.pushPose();
        float angleFast = (worldTime * 0.5f % 360) * (
                playerHeldShift ?
                playerHeldAttack ? -64 : -32 :
                playerHeldAttack ? -32 : -16);
        stacker.translate(HEAD_ROTATION_OFFSET)
                .rotateZ(angleFast)
                .translateBack(HEAD_ROTATION_OFFSET);
        renderer.render(HEAD.get(), light);
        ms.popPose();


    }
}