package twilightforest.mixin.asm.armor;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.ASMHooks;

@Mixin({HumanoidArmorLayer.class})
public abstract class CancelArmorRenderingMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {
   @Inject(
      method = {"renderArmorPiece"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/item/ArmorItem;getEquipmentSlot()Lnet/minecraft/world/entity/EquipmentSlot;"
      )},
      cancellable = true
   )
   private void tf$cancelArmorRendering(
      PoseStack poseStack,
      MultiBufferSource bufferSource,
      T livingEntity,
      EquipmentSlot slot,
      int packedLight,
      A model,
      CallbackInfo ci,
      @Local ItemStack stack
   ) {
      if (ASMHooks.cancelArmorRendering(true, stack)) {
         ci.cancel();
      }
   }
}
