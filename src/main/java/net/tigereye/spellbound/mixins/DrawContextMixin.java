package net.tigereye.spellbound.mixins;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.tigereye.spellbound.enchantments.unbreaking.BufferedEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class DrawContextMixin {
    @Inject(at = @At(value = "INVOKE"), method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
    public void spellboundDrawContextDrawItemInSlotMixin(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci){
        BufferedEnchantment.RenderBufferItemOverlay(stack,x,y);
    }
}
