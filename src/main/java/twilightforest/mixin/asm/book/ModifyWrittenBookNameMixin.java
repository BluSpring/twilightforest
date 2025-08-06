package twilightforest.mixin.asm.book;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.WrittenBookItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.ASMHooks;

@Mixin({WrittenBookItem.class})
public abstract class ModifyWrittenBookNameMixin {
   @ModifyReturnValue(
      method = {"getName"},
      at = {@At("RETURN")}
   )
   private Component tf$modifyWrittenBookName(Component original, @Local(argsOnly = true) ItemStack stack) {
      return ASMHooks.modifyWrittenBookName(original, stack);
   }
}
