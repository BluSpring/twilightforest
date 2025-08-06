package twilightforest.mixin.asm.multipart;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.ASMHooks;

@Mixin({EntityRenderDispatcher.class})
public class ResolveEntityRendererMixin {
   @ModifyExpressionValue(
      method = {"getRenderer"},
      at = {@At(
         value = "INVOKE",
         target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;",
         ordinal = 2
      )}
   )
   private <V> V tf$resolveEntityRenderer(V original, @Local(argsOnly = true) Entity entity) {
      return (V)ASMHooks.resolveEntityRenderer((EntityRenderer<?>)original, entity);
   }
}
