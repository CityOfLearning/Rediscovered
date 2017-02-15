package com.stormister.rediscovered.blocks;

import com.stormister.rediscovered.mod_Rediscovered;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockCherryWood extends Block {
	/** The type of tree this block came from. */
	public static final String[] woodType = new String[] { "cherry" };
	private final String name = "CherryPlank";

	public BlockCherryWood(String texture) {
		super(Material.wood);
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(mod_Rediscovered.modid + "_" + name);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	public String getName() {
		return name;
	}
}
