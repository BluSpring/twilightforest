package twilightforest.mixin;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ExistingFileHelper.class)
public class ExistingFileHelperMixin {
	@Overwrite
	public boolean exists(ResourceLocation loc, PackType packType) {
		return true;
	}
}
