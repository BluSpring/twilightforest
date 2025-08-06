package twilightforest.mixin.asm.multipart;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.util.Iterator;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.ASMHooks;

@Mixin({LevelRenderer.class})
public class ResolveEntitiesForRenderingMixin {
   @ModifyExpressionValue(
      method = {"renderLevel"},
      at = {@At(
         value = "INVOKE",
         target = "Ljava/lang/Iterable;iterator()Ljava/util/Iterator;",
         ordinal = 0
      )}
   )
   private Iterator<Entity> tf$resolveEntitiesForRendering(Iterator<Entity> original) {
      return ASMHooks.resolveEntitiesForRendering(original);
   }
}
