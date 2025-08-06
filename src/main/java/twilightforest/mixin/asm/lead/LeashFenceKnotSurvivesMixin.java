package twilightforest.mixin.asm.lead;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.ASMHooks;

@Mixin({LeashFenceKnotEntity.class})
public class LeashFenceKnotSurvivesMixin {
   @ModifyReturnValue(
      method = {"survives"},
      at = {@At("RETURN")}
   )
   private boolean tf$leashFenceKnotSurvives(boolean original) {
      return ASMHooks.leashFenceKnotSurvives(original, (LeashFenceKnotEntity) (Object) this);
   }
}
