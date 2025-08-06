package twilightforest.fabric.transfer;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ForgeItemHandlerHelper {
	public static ItemStack insertItem(ForgeItemHandler dest, @NotNull ItemStack stack, boolean simulate) {
		if (dest == null || stack.isEmpty())
			return stack;

		for (int i = 0; i < dest.getSlotCount(); i++)
		{
			stack = dest.insertItem(i, stack, simulate);
			if (stack.isEmpty())
			{
				return ItemStack.EMPTY;
			}
		}

		return stack;
	}
}