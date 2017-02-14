package com.stormister.rediscovered;

import net.minecraft.creativetab.CreativeTabs;

public class BlockDirtHalfSlab extends BlockDirtSlab {

	private final String name = "DirtHalfSlab";

	public BlockDirtHalfSlab() {
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
