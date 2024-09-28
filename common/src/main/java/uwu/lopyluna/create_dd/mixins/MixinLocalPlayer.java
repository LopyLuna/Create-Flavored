package uwu.lopyluna.create_dd.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uwu.lopyluna.create_dd.content.blocks.contraptions.contraption_block.BoatContraptionEntity;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer extends AbstractClientPlayer {
    @Shadow public Input input;
    @Shadow private boolean handsBusy;

    public MixinLocalPlayer(ClientLevel pClientLevel, GameProfile pGameProfile, @Nullable ProfilePublicKey pProfilePublicKey) {
        super(pClientLevel, pGameProfile, pProfilePublicKey);
    }

    @Inject(method = "rideTick", at = @At("TAIL"))
    private void create_dd$rideTickForBoatContraptionEntity(CallbackInfo ci) {
        if (this.getVehicle() instanceof BoatContraptionEntity boatContraption) {
            boatContraption.setInput(this.input.left, this.input.right);
            this.handsBusy |= this.input.left || this.input.right;
        }
    }
}
