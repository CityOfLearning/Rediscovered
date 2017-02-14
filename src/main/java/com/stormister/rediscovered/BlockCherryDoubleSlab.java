package com.stormister.rediscovered;

public class BlockCherryDoubleSlab extends BlockCherrySlab {

	private final String name = "CherryDoubleSlab";

	public BlockCherryDoubleSlab() {
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
