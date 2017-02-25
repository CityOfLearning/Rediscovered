package com.stormister.rediscovered.items;

import com.stormister.rediscovered.blocks.BlockDirtDoubleSlab;
import com.stormister.rediscovered.blocks.BlockDirtHalfSlab;

import net.minecraft.block.Block;
import net.minecraft.item.ItemSlab;

public class ItemBlockDirtSlab extends ItemSlab {
	/**
	 * Initializes a new instance of the ItemBlockStainedBrickSlab class.
	 *
	 * @param block
	 *            the block behind the item.
	 * @param slab
	 *            the half height slab.
	 * @param doubleSlab
	 *            the full height slab.
	 * @param stacked
	 *            whether or not the block is the stacked version.
	 */
	public ItemBlockDirtSlab(final Block block, final BlockDirtHalfSlab slab, final BlockDirtDoubleSlab doubleSlab,
			final Boolean stacked) {
		super(block, slab, doubleSlab);
	}
}