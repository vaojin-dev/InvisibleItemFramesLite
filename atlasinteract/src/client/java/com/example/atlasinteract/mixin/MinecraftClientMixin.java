package com.vaojin.atlasinteract.mixin;

import com.vaojin.atlasinteract.network.EmptyClickPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {

    @Inject(method = "startUseItem", at = @At("HEAD"))
    private void onStartUseItem(CallbackInfo ci) {
        Minecraft client = (Minecraft) (Object) this;

        if (client.player == null || client.player.connection == null) return;

        ItemStack mainHand = client.player.getMainHandItem();
        ItemStack offHand = client.player.getOffhandItem();

        if (mainHand.isEmpty() && offHand.isEmpty()) {
            
            ClientPlayNetworking.send(new EmptyClickPayload());
            
        }
    }
}