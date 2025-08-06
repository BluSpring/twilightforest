package twilightforest.mixin.asm.multipart;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.ASMHooks;

@Mixin({ServerEntity.class})
public class SendDirtyEntityDataMixin {
   @ModifyExpressionValue(
      method = {"sendDirtyEntityData"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/server/level/ServerEntity;entity:Lnet/minecraft/world/entity/Entity;"
      )}
   )
   private Entity tf$sendDirtyEntityData(Entity original) {
      return ASMHooks.sendDirtyEntityData(original);
   }
}
