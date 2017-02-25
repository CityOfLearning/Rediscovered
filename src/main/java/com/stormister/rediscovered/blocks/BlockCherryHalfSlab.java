package com.stormister.rediscovered.blocks;

import com.stormister.rediscovered.Rediscovered;

import net.minecraft.creativetab.CreativeTabs;

public class BlockCherryHalfSlab extends BlockCherrySlab {

	private final String name = "CherryHalfSlab";

	public BlockCherryHalfSlab() {
		setUnlocalizedName(Rediscovered.modid + "_" + name);
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
