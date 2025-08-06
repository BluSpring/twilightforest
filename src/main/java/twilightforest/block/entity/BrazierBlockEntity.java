package twilightforest.block.entity;

import io.github.fabricators_of_create.porting_lib.blocks.extensions.CustomRenderBoundingBoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import twilightforest.block.BrazierBlock;
import twilightforest.init.TFBlockEntities;

public class BrazierBlockEntity extends BlockEntity implements CustomRenderBoundingBoxBlockEntity {

	private static int tick = 0;

	public BrazierBlockEntity(BlockPos pos, BlockState blockState) {
		super(TFBlockEntities.BRAZIER.get(), pos, blockState);
	}

	public static void tick(Level level, BlockPos pos, BlockState state, BrazierBlockEntity entity) {
		if (level.isClientSide()) {
			if (state.getValue(BrazierBlock.LIGHT).isLit() && state.getValue(BrazierBlock.LIGHT).getSmokeRate() > 0) {
				if (BrazierBlockEntity.tick % state.getValue(BrazierBlock.LIGHT).getSmokeRate() == 0) {
					BlockPos above = pos.above();
					level.addParticle(ParticleTypes.SMOKE, above.getX() + level.random.nextFloat() * 0.4F + 0.3F, above.getY() + 0.9F, above.getZ() + level.random.nextFloat() * 0.4F + 0.3F,
						0.0D, 0.05D, 0.0D);
				}
			}
			BrazierBlockEntity.tick++;
		}
	}

	@Override
	public AABB getRenderBoundingBox() {
		BlockPos pos = this.getBlockPos();
		return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 2.0, pos.getZ() + 1.0);
	}
}
