package com.stormister.rediscovered;

public class BlockDirtDoubleSlab extends BlockDirtSlab {

	private final String name = "DirtDoubleSlab";

	public BlockDirtDoubleSlab() {
		setUnlocalizedName(mod_Rediscovered.modid + "_" + name);
	}

	public String getName() {
		return name;
	}

	@Override
	public final boolean isDouble() {
		return true;
	}
}
