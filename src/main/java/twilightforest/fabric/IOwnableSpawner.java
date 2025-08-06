package twilightforest.fabric;

import com.mojang.datafixers.util.Either;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IOwnableSpawner {
	default Either<BlockEntity, Entity> getOwner() {
		return null;
	}
}
