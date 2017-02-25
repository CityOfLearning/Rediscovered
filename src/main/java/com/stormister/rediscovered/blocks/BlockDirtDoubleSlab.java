package com.stormister.rediscovered.blocks;

import com.stormister.rediscovered.Rediscovered;

public class BlockDirtDoubleSlab extends BlockDirtSlab {

	private final String name = "DirtDoubleSlab";

	public BlockDirtDoubleSlab() {
		setUnlocalizedName(Rediscovered.modid + "_" + name);
	}

	public String getName() {
		return name;
	}

	@Override
	public final boolean isDouble() {
		return true;
	}
}
