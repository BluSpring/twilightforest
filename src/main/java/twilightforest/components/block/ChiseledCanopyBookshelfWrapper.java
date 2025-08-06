package twilightforest.components.block;

import net.minecraft.world.item.ItemStack;
import twilightforest.block.ChiseledCanopyShelfBlock;
import twilightforest.block.entity.bookshelf.ChiseledCanopyShelfBlockEntity;
import twilightforest.fabric.transfer.InvWrapper;

public class ChiseledCanopyBookshelfWrapper extends InvWrapper {
	public ChiseledCanopyBookshelfWrapper(ChiseledCanopyShelfBlockEntity inv) {
		super(inv);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (((ChiseledCanopyShelfBlockEntity)this.getInv()).getBlockState().getValue(ChiseledCanopyShelfBlock.SPAWNER)) return ItemStack.EMPTY;
		return super.extractItem(slot, amount, simulate);
	}
}
