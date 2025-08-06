package twilightforest.data;

import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.material.MapColor;
import twilightforest.data.tags.ItemTagGenerator;
import twilightforest.init.*;
import twilightforest.mixin.ParrotAccessor;
import twilightforest.util.datamaps.CrumbledBlock;
import twilightforest.util.datamaps.EntityTransformation;
import twilightforest.util.datamaps.MagicMapBiomeColor;
import twilightforest.util.datamaps.OreMapOreColor;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DataMapGenerator {
	public DataMapGenerator() {
		//super(output, provider);
	}

	//@Override
	@SuppressWarnings("deprecation")
	public void gather() {
		ComposterBlock.COMPOSTABLES.put(TFBlocks.FALLEN_LEAVES.asItem(), (0.1F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.CANOPY_LEAVES.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.CLOVER_PATCH.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.DARK_LEAVES.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.FIDDLEHEAD.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.HEDGE.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.MANGROVE_LEAVES.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.MAYAPPLE.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.MINING_LEAVES.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TWILIGHT_OAK_LEAVES.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.RAINBOW_OAK_LEAVES.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.ROOT_STRAND.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.SORTING_LEAVES.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.THORN_LEAVES.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TIME_LEAVES.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TRANSFORMATION_LEAVES.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TWILIGHT_OAK_SAPLING.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.CANOPY_SAPLING.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.MANGROVE_SAPLING.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.DARKWOOD_SAPLING.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.RAINBOW_OAK_SAPLING.asItem(), (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFItems.TORCHBERRIES, (0.3F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.BEANSTALK_LEAVES.asItem(), (0.5F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.MOSS_PATCH.asItem(), (0.5F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.ROOT_BLOCK.asItem(), (0.5F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.THORN_ROSE.asItem(), (0.5F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TROLLVIDR.asItem(), (0.5F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.HOLLOW_OAK_SAPLING.asItem(), (0.5F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TIME_SAPLING.asItem(), (0.5F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TRANSFORMATION_SAPLING.asItem(), (0.5F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.MINING_SAPLING.asItem(), (0.5F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.SORTING_SAPLING.asItem(), (0.5F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TORCHBERRY_PLANT.asItem(), (0.5F));
		ComposterBlock.COMPOSTABLES.put(TFItems.LIVEROOT, (0.5F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.HUGE_MUSHGLOOM_STEM.asItem(), (0.65F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.HUGE_WATER_LILY.asItem(), (0.65F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.LIVEROOT_BLOCK.asItem(), (0.65F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.MUSHGLOOM.asItem(), (0.65F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.UBEROUS_SOIL.asItem(), (0.65F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.HUGE_STALK.asItem(), (0.65F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.UNRIPE_TROLLBER.asItem(), (0.65F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.TROLLBER.asItem(), (0.65F));
		ComposterBlock.COMPOSTABLES.put(TFItems.MAZE_WAFER, (0.65F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.HUGE_LILY_PAD.asItem(), (0.85F));
		ComposterBlock.COMPOSTABLES.put(TFBlocks.HUGE_MUSHGLOOM.asItem(), (0.85F));
		ComposterBlock.COMPOSTABLES.put(TFItems.EXPERIMENT_115, (0.85F));
		ComposterBlock.COMPOSTABLES.put(TFItems.MAGIC_BEANS, (0.85F));

		FuelRegistry.INSTANCE.add(ItemTagGenerator.BANISTERS, 300);

		ParrotAccessor.getMobSoundMap().put(TFEntities.ALPHA_YETI.get(), (TFSounds.ALPHA_YETI_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.BLOCKCHAIN_GOBLIN.get(), (TFSounds.REDCAP_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.CARMINITE_BROODLING.get(), (SoundEvents.PARROT_IMITATE_SPIDER));
		ParrotAccessor.getMobSoundMap().put(TFEntities.CARMINITE_GOLEM.get(), (TFSounds.CARMINITE_GOLEM_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.FIRE_BEETLE.get(), (SoundEvents.PARROT_IMITATE_SPIDER));
		ParrotAccessor.getMobSoundMap().put(TFEntities.CARMINITE_GHASTLING.get(), (SoundEvents.PARROT_IMITATE_GHAST));
		ParrotAccessor.getMobSoundMap().put(TFEntities.CARMINITE_GHASTGUARD.get(), (SoundEvents.PARROT_IMITATE_GHAST));
		ParrotAccessor.getMobSoundMap().put(TFEntities.HEDGE_SPIDER.get(), (SoundEvents.PARROT_IMITATE_SPIDER));
		ParrotAccessor.getMobSoundMap().put(TFEntities.HELMET_CRAB.get(), (SoundEvents.PARROT_IMITATE_SPIDER));
		ParrotAccessor.getMobSoundMap().put(TFEntities.HOSTILE_WOLF.get(), (TFSounds.HOSTILE_WOLF_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.HYDRA.get(), (TFSounds.HYDRA_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.STABLE_ICE_CORE.get(), (TFSounds.ICE_CORE_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.KING_SPIDER.get(), (SoundEvents.PARROT_IMITATE_SPIDER));
		ParrotAccessor.getMobSoundMap().put(TFEntities.KOBOLD.get(), (TFSounds.KOBOLD_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.LICH.get(), (SoundEvents.PARROT_IMITATE_BLAZE));
		ParrotAccessor.getMobSoundMap().put(TFEntities.MAZE_SLIME.get(), (SoundEvents.PARROT_IMITATE_SLIME));
		ParrotAccessor.getMobSoundMap().put(TFEntities.LICH_MINION.get(), (SoundEvents.PARROT_IMITATE_ZOMBIE));
		ParrotAccessor.getMobSoundMap().put(TFEntities.MINOSHROOM.get(), (TFSounds.MINOTAUR_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.MINOTAUR.get(), (TFSounds.MINOTAUR_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.MIST_WOLF.get(), (TFSounds.HOSTILE_WOLF_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.MOSQUITO_SWARM.get(), (TFSounds.MOSQUITO_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.NAGA.get(), (TFSounds.NAGA_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.KNIGHT_PHANTOM.get(), (TFSounds.WRAITH_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.PINCH_BEETLE.get(), (SoundEvents.PARROT_IMITATE_SPIDER));
		ParrotAccessor.getMobSoundMap().put(TFEntities.REDCAP.get(), (TFSounds.REDCAP_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.REDCAP_SAPPER.get(), (TFSounds.REDCAP_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.SKELETON_DRUID.get(), (SoundEvents.PARROT_IMITATE_SKELETON));
		ParrotAccessor.getMobSoundMap().put(TFEntities.SLIME_BEETLE.get(), (SoundEvents.PARROT_IMITATE_SLIME));
		ParrotAccessor.getMobSoundMap().put(TFEntities.SNOW_GUARDIAN.get(), (TFSounds.ICE_CORE_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.SNOW_QUEEN.get(), (TFSounds.ICE_CORE_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.SWARM_SPIDER.get(), (SoundEvents.PARROT_IMITATE_SPIDER));
		ParrotAccessor.getMobSoundMap().put(TFEntities.TOWERWOOD_BORER.get(), (SoundEvents.PARROT_IMITATE_SILVERFISH));
		ParrotAccessor.getMobSoundMap().put(TFEntities.DEATH_TOME.get(), (TFSounds.DEATH_TOME_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.UR_GHAST.get(), (SoundEvents.PARROT_IMITATE_GHAST));
		ParrotAccessor.getMobSoundMap().put(TFEntities.WINTER_WOLF.get(), (TFSounds.HOSTILE_WOLF_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.WRAITH.get(), (TFSounds.WRAITH_PARROT.get()));
		ParrotAccessor.getMobSoundMap().put(TFEntities.YETI.get(), (TFSounds.ALPHA_YETI_PARROT.get()));

		var transformation = EntityTransformation.OMINOUS_FIRE;
		this.add2WayTransform(transformation, TFEntities.MINOTAUR, EntityType.ZOMBIFIED_PIGLIN);
		this.add2WayTransform(transformation, TFEntities.DEER, EntityType.COW);
		this.add2WayTransform(transformation, TFEntities.BOAR, EntityType.PIG);
		this.add2WayTransform(transformation, TFEntities.BIGHORN_SHEEP, EntityType.SHEEP);
		this.add2WayTransform(transformation, TFEntities.DWARF_RABBIT, EntityType.RABBIT);
		this.add2WayTransform(transformation, TFEntities.TINY_BIRD, EntityType.PARROT);
		this.add2WayTransform(transformation, TFEntities.RAVEN, EntityType.BAT);
		this.add2WayTransform(transformation, TFEntities.HOSTILE_WOLF, EntityType.WOLF);
		this.add2WayTransform(transformation, TFEntities.PENGUIN, EntityType.CHICKEN);
		this.add2WayTransform(transformation, TFEntities.HEDGE_SPIDER, EntityType.SPIDER);
		this.add2WayTransform(transformation, TFEntities.SWARM_SPIDER, EntityType.CAVE_SPIDER);
		this.add2WayTransform(transformation, TFEntities.WRAITH, EntityType.VEX);
		this.add2WayTransform(transformation, TFEntities.SKELETON_DRUID, EntityType.WITCH);
		this.add2WayTransform(transformation, TFEntities.CARMINITE_GHASTGUARD, EntityType.GHAST);
		this.add2WayTransform(transformation, TFEntities.TOWERWOOD_BORER, EntityType.SILVERFISH);
		this.add2WayTransform(transformation, TFEntities.MAZE_SLIME, EntityType.SLIME);

        var zombieBuilder = EntityTransformation.OMINOUS_FIRE;
		this.add1WayTransform(zombieBuilder, EntityType.VILLAGER, EntityType.ZOMBIE_VILLAGER);
		this.add1WayTransform(zombieBuilder, EntityType.PIGLIN, EntityType.ZOMBIFIED_PIGLIN);
		this.add1WayTransform(zombieBuilder, EntityType.HORSE, EntityType.ZOMBIE_HORSE);

		CrumbledBlock.CRUMBLE_HORN.put(Blocks.STONE_BRICKS, new CrumbledBlock(Blocks.CRACKED_STONE_BRICKS, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.INFESTED_STONE_BRICKS, new CrumbledBlock(Blocks.INFESTED_CRACKED_STONE_BRICKS, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.POLISHED_BLACKSTONE_BRICKS, new CrumbledBlock(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, new CrumbledBlock(Blocks.BLACKSTONE, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.NETHER_BRICKS, new CrumbledBlock(Blocks.CRACKED_NETHER_BRICKS, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.DEEPSLATE_BRICKS, new CrumbledBlock(Blocks.CRACKED_DEEPSLATE_BRICKS, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.DEEPSLATE_TILES, new CrumbledBlock(Blocks.CRACKED_DEEPSLATE_TILES, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(TFBlocks.MAZESTONE_BRICK.get(), new CrumbledBlock(TFBlocks.CRACKED_MAZESTONE.get(), 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(TFBlocks.UNDERBRICK.get(), new CrumbledBlock(TFBlocks.CRACKED_UNDERBRICK.get(), 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(TFBlocks.DEADROCK.get(), new CrumbledBlock(TFBlocks.CRACKED_DEADROCK.get(), 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(TFBlocks.CRACKED_DEADROCK.get(), new CrumbledBlock(TFBlocks.WEATHERED_DEADROCK.get(), 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(TFBlocks.TOWERWOOD.get(), new CrumbledBlock(TFBlocks.CRACKED_TOWERWOOD.get(), 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(TFBlocks.CASTLE_BRICK.get(), new CrumbledBlock(TFBlocks.CRACKED_CASTLE_BRICK.get(), 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(TFBlocks.CRACKED_CASTLE_BRICK.get(), new CrumbledBlock(TFBlocks.WORN_CASTLE_BRICK.get(), 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(TFBlocks.NAGASTONE_PILLAR.get(), new CrumbledBlock(TFBlocks.CRACKED_NAGASTONE_PILLAR.get(), 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(TFBlocks.ETCHED_NAGASTONE.get(), new CrumbledBlock(TFBlocks.CRACKED_ETCHED_NAGASTONE.get(), 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(TFBlocks.CASTLE_BRICK_STAIRS.get(), new CrumbledBlock(TFBlocks.CRACKED_CASTLE_BRICK_STAIRS.get(), 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(TFBlocks.NAGASTONE_STAIRS_LEFT.get(), new CrumbledBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_LEFT.get(), 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(TFBlocks.NAGASTONE_STAIRS_RIGHT.get(), new CrumbledBlock(TFBlocks.CRACKED_NAGASTONE_STAIRS_RIGHT.get(), 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.STONE, new CrumbledBlock(Blocks.COBBLESTONE, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.COBBLESTONE, new CrumbledBlock(Blocks.GRAVEL, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.SANDSTONE, new CrumbledBlock(Blocks.SAND, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.RED_SANDSTONE, new CrumbledBlock(Blocks.RED_SAND, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.GRASS_BLOCK, new CrumbledBlock(Blocks.DIRT, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.PODZOL, new CrumbledBlock(Blocks.DIRT, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.MYCELIUM, new CrumbledBlock(Blocks.DIRT, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.COARSE_DIRT, new CrumbledBlock(Blocks.DIRT, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.ROOTED_DIRT, new CrumbledBlock(Blocks.DIRT, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.OXIDIZED_COPPER, new CrumbledBlock(Blocks.WEATHERED_COPPER, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.WEATHERED_COPPER, new CrumbledBlock(Blocks.EXPOSED_COPPER, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.EXPOSED_COPPER, new CrumbledBlock(Blocks.COPPER_BLOCK, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.OXIDIZED_CUT_COPPER, new CrumbledBlock(Blocks.WEATHERED_CUT_COPPER, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.WEATHERED_CUT_COPPER, new CrumbledBlock(Blocks.EXPOSED_CUT_COPPER, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.EXPOSED_CUT_COPPER, new CrumbledBlock(Blocks.CUT_COPPER, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.OXIDIZED_CUT_COPPER_STAIRS, new CrumbledBlock(Blocks.WEATHERED_CUT_COPPER_STAIRS, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.WEATHERED_CUT_COPPER_STAIRS, new CrumbledBlock(Blocks.EXPOSED_CUT_COPPER_STAIRS, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.EXPOSED_CUT_COPPER_STAIRS, new CrumbledBlock(Blocks.CUT_COPPER_STAIRS, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.OXIDIZED_CUT_COPPER_SLAB, new CrumbledBlock(Blocks.WEATHERED_CUT_COPPER_SLAB, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.WEATHERED_CUT_COPPER_SLAB, new CrumbledBlock(Blocks.EXPOSED_CUT_COPPER_SLAB, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.EXPOSED_CUT_COPPER_SLAB, new CrumbledBlock(Blocks.CUT_COPPER_SLAB, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.OXIDIZED_CHISELED_COPPER, new CrumbledBlock(Blocks.WEATHERED_CHISELED_COPPER, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.WEATHERED_CHISELED_COPPER, new CrumbledBlock(Blocks.EXPOSED_CHISELED_COPPER, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.EXPOSED_CHISELED_COPPER, new CrumbledBlock(Blocks.CHISELED_COPPER, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.OXIDIZED_COPPER_GRATE, new CrumbledBlock(Blocks.WEATHERED_COPPER_GRATE, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.WEATHERED_COPPER_GRATE, new CrumbledBlock(Blocks.EXPOSED_COPPER_GRATE, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.EXPOSED_COPPER_GRATE, new CrumbledBlock(Blocks.COPPER_GRATE, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.OXIDIZED_COPPER_BULB, new CrumbledBlock(Blocks.WEATHERED_COPPER_BULB, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.WEATHERED_COPPER_BULB, new CrumbledBlock(Blocks.EXPOSED_COPPER_BULB, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.EXPOSED_COPPER_BULB, new CrumbledBlock(Blocks.COPPER_BULB, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.OXIDIZED_COPPER_TRAPDOOR, new CrumbledBlock(Blocks.WEATHERED_COPPER_TRAPDOOR, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.WEATHERED_COPPER_TRAPDOOR, new CrumbledBlock(Blocks.EXPOSED_COPPER_TRAPDOOR, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.EXPOSED_COPPER_TRAPDOOR, new CrumbledBlock(Blocks.COPPER_TRAPDOOR, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.OXIDIZED_COPPER_DOOR, new CrumbledBlock(Blocks.WEATHERED_COPPER_DOOR, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.WEATHERED_COPPER_DOOR, new CrumbledBlock(Blocks.EXPOSED_COPPER_DOOR, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.EXPOSED_COPPER_DOOR, new CrumbledBlock(Blocks.COPPER_DOOR, 0.2F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.GRAVEL, new CrumbledBlock(Blocks.AIR, 0.05F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.DIRT, new CrumbledBlock(Blocks.AIR, 0.05F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.SAND, new CrumbledBlock(Blocks.AIR, 0.05F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.RED_SAND, new CrumbledBlock(Blocks.AIR, 0.05F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.CLAY, new CrumbledBlock(Blocks.AIR, 0.05F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.ANDESITE, new CrumbledBlock(Blocks.AIR, 0.05F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.DIORITE, new CrumbledBlock(Blocks.AIR, 0.05F));
		CrumbledBlock.CRUMBLE_HORN.put(Blocks.GRANITE, new CrumbledBlock(Blocks.AIR, 0.05F));

		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.FOREST, new MagicMapBiomeColor(MapColor.PLANT, 1));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.DENSE_FOREST, new MagicMapBiomeColor(MapColor.PLANT, 0));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.LAKE, new MagicMapBiomeColor(MapColor.WATER, 3));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.STREAM, new MagicMapBiomeColor(MapColor.WATER, 1));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.SWAMP, new MagicMapBiomeColor(MapColor.DIAMOND, 3));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.FIRE_SWAMP, new MagicMapBiomeColor(MapColor.NETHER, 1));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.CLEARING, new MagicMapBiomeColor(MapColor.GRASS, 2));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.OAK_SAVANNAH, new MagicMapBiomeColor(MapColor.GRASS, 0));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.HIGHLANDS, new MagicMapBiomeColor(MapColor.DIRT, 0));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.THORNLANDS, new MagicMapBiomeColor(MapColor.WOOD, 3));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.FINAL_PLATEAU, new MagicMapBiomeColor(MapColor.COLOR_LIGHT_GRAY, 2));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.FIREFLY_FOREST, new MagicMapBiomeColor(MapColor.EMERALD, 1));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.DARK_FOREST, new MagicMapBiomeColor(MapColor.COLOR_GREEN, 3));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.DARK_FOREST_CENTER, new MagicMapBiomeColor(MapColor.COLOR_ORANGE, 3));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.SNOWY_FOREST, new MagicMapBiomeColor(MapColor.SNOW, 1));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.GLACIER, new MagicMapBiomeColor(MapColor.ICE, 1));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.MUSHROOM_FOREST, new MagicMapBiomeColor(MapColor.COLOR_ORANGE, 0));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.DENSE_MUSHROOM_FOREST, new MagicMapBiomeColor(MapColor.COLOR_PINK, 0));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.ENCHANTED_FOREST, new MagicMapBiomeColor(MapColor.COLOR_CYAN, 2));
		MagicMapBiomeColor.BIOME_COLOR_MAP.put(TFBiomes.SPOOKY_FOREST, new MagicMapBiomeColor(MapColor.COLOR_PURPLE, 0));

		OreMapOreColor.ORE_COLOR_MAP.put(BlockTags.COPPER_ORES, new OreMapOreColor(MapColor.COLOR_ORANGE));
		OreMapOreColor.ORE_COLOR_MAP.put(BlockTags.COAL_ORES, new OreMapOreColor(MapColor.COLOR_BLACK));
		OreMapOreColor.ORE_COLOR_MAP.put(BlockTags.IRON_ORES, new OreMapOreColor(MapColor.RAW_IRON));
		OreMapOreColor.ORE_COLOR_MAP.put(BlockTags.LAPIS_ORES, new OreMapOreColor(MapColor.LAPIS));
		OreMapOreColor.ORE_COLOR_MAP.put(BlockTags.GOLD_ORES, new OreMapOreColor(MapColor.GOLD));
		OreMapOreColor.ORE_COLOR_MAP.put(BlockTags.REDSTONE_ORES, new OreMapOreColor(MapColor.COLOR_RED));
		OreMapOreColor.ORE_COLOR_MAP.put(BlockTags.DIAMOND_ORES, new OreMapOreColor(MapColor.DIAMOND));
		OreMapOreColor.ORE_COLOR_MAP.put(BlockTags.EMERALD_ORES, new OreMapOreColor(MapColor.EMERALD));
		//OreMapOreColor.ORE_COLOR_MAP.put(Blocks.ANCIENT_DEBRIS, new OreMapOreColor(MapColor.TERRACOTTA_BROWN));
	}

	private void add1WayTransform(Map<EntityType<?>, EntityTransformation> builder, EntityType<?> from, EntityType<?> to) {
		builder.put(from, new EntityTransformation(to));
	}

	private void add2WayTransform(Map<EntityType<?>, EntityTransformation> builder, Holder<EntityType<?>> tfMob, EntityType<?> vanillaMob) {
		builder.put(tfMob.value(), new EntityTransformation(vanillaMob));
		builder.put(vanillaMob, new EntityTransformation(tfMob.value()));
	}
}
