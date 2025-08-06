package twilightforest;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.util.ModidPrefixUtil;
import twilightforest.world.components.BiomeColorAlgorithms;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@SuppressWarnings("unused") // Referenced by enumextender.json
public class TFEnumExtensions {

	private static BiomeColorAlgorithms biomeColorAlgorithms = new BiomeColorAlgorithms();

	private static final ModidPrefixUtil modidPrefixUtil = ModidPrefixUtil.INSTANCE; // Enum extensions run before the bean context loads

}
