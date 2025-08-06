package twilightforest.fabric;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public interface CustomMapItem {
	MapItemSavedData getCustomMapData(ItemStack stack, Level level);
}
