package twilightforest.enums.extensions;

import io.github.fabricators_of_create.porting_lib.core.util.Lazy;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.block.Block;
import twilightforest.TFEnumExtensions;
import twilightforest.fabric.EnumUtils;
import twilightforest.init.TFBlocks;
import twilightforest.mixin.ee.BoatTypeAccessor;
import twilightforest.util.ModidPrefixUtil;

public class TFBoatTypeEnumExtension {

	private ModidPrefixUtil modidPrefixUtil = ModidPrefixUtil.INSTANCE;

	private static Boat.Type create(String name, Block planks) {
		return EnumUtils.addEnumToClass(Boat.Type.class, BoatTypeAccessor.getValues(), name, size -> BoatTypeAccessor.create(name, size, planks, name), values -> BoatTypeAccessor.setValues(values.toArray(new Boat.Type[0])));
	}

	public final Lazy<Boat.Type> TWILIGHT_OAK = Lazy.of(() -> create(modidPrefixUtil.stringPrefix("twilight_oak"), TFBlocks.TWILIGHT_OAK_PLANKS.get()));
	public final Lazy<Boat.Type> CANOPY = Lazy.of(() -> create(modidPrefixUtil.stringPrefix("canopy"), TFBlocks.CANOPY_PLANKS.get()));
	public final Lazy<Boat.Type> MANGROVE = Lazy.of(() -> create(modidPrefixUtil.stringPrefix("mangrove"), TFBlocks.MANGROVE_PLANKS.get()));
	public final Lazy<Boat.Type> DARK = Lazy.of(() -> create(modidPrefixUtil.stringPrefix("dark"), TFBlocks.DARK_PLANKS.get()));
	public final Lazy<Boat.Type> TIME = Lazy.of(() -> create(modidPrefixUtil.stringPrefix("time"), TFBlocks.TIME_PLANKS.get()));
	public final Lazy<Boat.Type> TRANSFORMATION = Lazy.of(() -> create(modidPrefixUtil.stringPrefix("transformation"), TFBlocks.TRANSFORMATION_PLANKS.get()));
	public final Lazy<Boat.Type> MINING = Lazy.of(() -> create(modidPrefixUtil.stringPrefix("mining"), TFBlocks.MINING_PLANKS.get()));
	public final Lazy<Boat.Type> SORTING = Lazy.of(() -> create(modidPrefixUtil.stringPrefix("sorting"), TFBlocks.SORTING_PLANKS.get()));

}
