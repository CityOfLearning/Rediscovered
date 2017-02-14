package com.stormister.rediscovered;

import net.minecraft.creativetab.CreativeTabs;

public class BlockCherryHalfSlab extends BlockCherrySlab {

	private final String name = "CherryHalfSlab";

	public BlockCherryHalfSlab() {
		setUnlocalizedName(mod_Rediscovered.modid + "_" + name);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	public String getName() {
		return name;
	}

	@Override
	public final boolean isDouble() {
		return false;
	}
}
