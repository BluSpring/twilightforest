package twilightforest.mixin.asm.armor;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.ASMHooks;

@Mixin({LivingEntity.class})
public class ArmorVisibilityRenderingMixin {
   @ModifyExpressionValue(
      method = {"getVisibilityPercent"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;getArmorCoverPercentage()F"
      )}
   )
   private float tf$modifyArmorVisibility(float original) {
      return ASMHooks.modifyArmorVisibility(original, (LivingEntity) (Object) this);
   }
}
