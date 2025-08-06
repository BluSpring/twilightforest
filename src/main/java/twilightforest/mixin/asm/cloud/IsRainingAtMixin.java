package twilightforest.mixin.asm.cloud;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.ASMHooks;

@Mixin({Level.class})
public class IsRainingAtMixin {
   @ModifyReturnValue(
      method = {"isRainingAt"},
      at = {@At("RETURN")}
   )
   private boolean tf$checkIsRainingAt(boolean original, @Local(argsOnly = true) BlockPos pos) {
      return ASMHooks.isRainingAt(original, (Level) (Object) this, pos);
   }
}
