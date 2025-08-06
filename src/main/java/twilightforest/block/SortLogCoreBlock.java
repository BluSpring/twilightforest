package twilightforest.block;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlottedStackStorage;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.config.TFConfig;
import twilightforest.data.tags.EntityTagGenerator;
import twilightforest.init.TFParticleType;
import twilightforest.network.ParticlePacket;
import twilightforest.util.BlockCapabilityDirectionalCache;
import twilightforest.util.WorldUtil;

import java.util.*;

public class SortLogCoreBlock extends SpecialMagicLogBlock {

	//private final BlockCapabilityDirectionalCache<Storage<ItemVariant>> capabilityCache = new BlockCapabilityDirectionalCache<>();

	public SortLogCoreBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean doesCoreFunction() {
		return !TFConfig.disableSortingCore;
	}

	@Override
	void performTreeEffect(ServerLevel level, BlockPos pos, RandomSource rand) {
		Map<List<Storage<ItemVariant>>, Vec3> inputMap = new HashMap<>();
		Map<Storage<ItemVariant>, Vec3> outputMap = new HashMap<>();

		for (BlockPos blockPos : WorldUtil.getAllAround(pos, TFConfig.sortingCoreRange)) { // Get every itemHandler from every block in the area
			if (!blockPos.equals(pos)) {
				BlockEntity blockEntity = level.getBlockEntity(blockPos);
				if (blockEntity != null) {
					// Put it in the input if its within 2 blocks
					if (Math.abs(blockPos.getX() - pos.getX()) <= 2 && Math.abs(blockPos.getY() - pos.getY()) <= 2 && Math.abs(blockPos.getZ() - pos.getZ()) <= 2) {
						List<Storage<ItemVariant>> handlers = new ArrayList<>();
						for (Direction side : Direction.values()) {
							Storage<ItemVariant> handler = ItemStorage.SIDED.find(level, blockPos, side);
							if (handler != null) handlers.add(handler);
						}
						if (!handlers.isEmpty()) {
							inputMap.put(handlers, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
						}
					} else { // Output if its outside that range
						for (Direction side : Direction.values()) {
							Storage<ItemVariant> handler = ItemStorage.SIDED.find(level, blockPos, side);
							if (handler != null) outputMap.put(handler, Vec3.upFromBottomCenterOf(blockPos, 1.9D));
						}
					}
				}
			}
		}

		List<Entity> alreadyUsedForInput = new ArrayList<>(); // Keep track of entities we already have for inputs, so we can skip over them when looking for outputs

		/*level.getEntities((Entity) null, new AABB(pos).inflate(2), entity -> entity.isAlive() && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach(entity -> {
			List<Storage<ItemVariant>> handlers = new ArrayList<>();
			for (Direction side : Direction.values()) {
				Storage<ItemVariant> handler = entity.getCapability(Capabilities.ItemHandler.ENTITY_AUTOMATION, side);
				if (handler != null) handlers.add(handler);
			}
			if (!handlers.isEmpty()) {
				inputMap.put(handlers, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
				alreadyUsedForInput.add(entity);
			}
		});*/

		if (inputMap.isEmpty()) return; // No input

		/*level.getEntities((Entity) null, new AABB(pos).inflate(16), entity -> entity.isAlive() && !alreadyUsedForInput.contains(entity) && entity.getType().is(EntityTagGenerator.SORTABLE_ENTITIES)).forEach(entity -> {
			for (Direction side : Direction.values()) {
				Storage<ItemVariant> handler = entity.getCapability(Capabilities.ItemHandler.ENTITY_AUTOMATION, side);
				if (handler != null) outputMap.put(handler, entity.position().add(0D, entity.getBbHeight() + 0.9D, 0D));
			}
		});*/

		if (outputMap.isEmpty()) return; // No output

		for (Map.Entry<List<Storage<ItemVariant>>, Vec3> inputHandlers : inputMap.entrySet()) {
			boolean transferred = false;
			for (Storage<ItemVariant> inputIItemHandler : inputHandlers.getKey()) {
				for (StorageView<ItemVariant> view : inputIItemHandler){
					if (TransferUtil.simulateExtractView(view, view.getResource(), 1L) > 0) {
						Map<Long, Storage<ItemVariant>> outputsByCount = new HashMap<>();

						for (Storage<ItemVariant> outputIItemHandler : outputMap.keySet()) {
							long count = 0;
							for (StorageView<ItemVariant> view2 : outputIItemHandler) {
								if (view2.getResource().isOf(view.getResource().getItem())) {
									count += view2.getAmount();
								}
							}
							if (count > 0) outputsByCount.put(count, outputIItemHandler);
						}

						for (Long count : outputsByCount.keySet().stream().sorted(Comparator.comparingLong(Long::longValue).reversed()).toList()) {
							Storage<ItemVariant> outputIItemHandler = outputsByCount.get(count);
							int firstProperStack = -1;
							int j = 0;
							for (StorageView<ItemVariant> view2 : outputIItemHandler) {
								if (view2.getResource().isOf(view.getResource().getItem()) && view2.getResource().componentsMatch(view.getResource().getComponents())) {
									if (firstProperStack == -1 && view2.isResourceBlank()) {
										firstProperStack = j; //We reference the index of the first empty slot, in case there is no stacks that aren't at max size
									} else if (view2.getAmount() < view2.getCapacity()) {
										firstProperStack = j;
										break;
									}
								}
								j++;
							}
							if (firstProperStack != -1) { // If there weren't any non-full stacks, we transfer to an empty space instead
								ItemStack newStack = TransferUtil.extractAnyItem(inputIItemHandler, 1);
								if (!newStack.isEmpty() && TransferUtil.insertItem(outputIItemHandler, newStack) > 0) {
									transferred = true;

									Vec3 xyz = outputMap.get(outputIItemHandler);
									Vec3 diff = inputHandlers.getValue().subtract(xyz);

									ParticlePacket particlePacket = new ParticlePacket();
									double x = diff.x - 0.25D + rand.nextDouble() * 0.5D;
									double y = diff.y - 1.75D + rand.nextDouble() * 0.5D;
									double z = diff.z - 0.25D + rand.nextDouble() * 0.5D;
									particlePacket.queueParticle(TFParticleType.SORTING_PARTICLE.get(), false, xyz, new Vec3(x, y, z).scale(1D / diff.length()));
									for (ServerPlayer player : PlayerLookup.around(level, xyz, 64.0)) {
										ServerPlayNetworking.send(player, particlePacket);
									}
									break;
								}
							}
						}
					}
					if (transferred) break;// If we transferred the item from this Entry already, we break, since all IItemHandlers in one entry come from the same source
				}
				if (transferred) break; // Again, since we only transfer once per source, break
			}
		}
	}
}
