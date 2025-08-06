package twilightforest.mixin.asm.beardifier;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunction.FunctionContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import twilightforest.ASMHooks;
import twilightforest.fabric.BeardifierExtension;

@Mixin({Beardifier.class})
public abstract class BeardifierMixin implements BeardifierExtension {
   @Unique
   public ObjectListIterator<DensityFunction> twilightforest_customStructureDensities;

   @ModifyReturnValue(
      method = {"compute"},
      at = {@At("RETURN")}
   )
   private double tf$getCustomDensity(double original, @Local(argsOnly = true) FunctionContext context) {
      return ASMHooks.getCustomDensity(original, context, this.twilightforest_customStructureDensities);
   }

   @Override
   public void tf$setCustomStructureDensities(ObjectListIterator<DensityFunction> structureDensities) {
      this.twilightforest_customStructureDensities = structureDensities;
   }
}
