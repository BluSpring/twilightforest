package twilightforest.mixin.asm.mob;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.PathfinderMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.ASMHooks;

@Mixin({PathfinderMob.class})
public class PathFinderUnrestrainedByLeashMixin {
   @ModifyReturnValue(
      method = {"shouldStayCloseToLeashHolder"},
      at = {@At("RETURN")}
   )
   private boolean tf$overrideStayCloseToHolder(boolean original) {
      return ASMHooks.overrideStayCloseToHolder(original, (PathfinderMob) (Object) this);
   }
}
