package com.stormister.rediscovered.blocks;

import com.stormister.rediscovered.Rediscovered;

public class BlockCherryDoubleSlab extends BlockCherrySlab {

	private final String name = "CherryDoubleSlab";

	public BlockCherryDoubleSlab() {
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
