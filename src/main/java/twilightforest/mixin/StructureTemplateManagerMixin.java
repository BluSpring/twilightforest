package twilightforest.mixin;

import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(StructureTemplateManager.class)
public class StructureTemplateManagerMixin {
	@Shadow
	@Final
	private HolderGetter<Block> blockLookup;

	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public StructureTemplate readStructure(CompoundTag nbt) {
		StructureTemplate structureTemplate = new StructureTemplate();
//		int i = NbtUtils.getDataVersion(nbt, 500);
		structureTemplate.load(this.blockLookup, nbt);
		return structureTemplate;
	}
}
