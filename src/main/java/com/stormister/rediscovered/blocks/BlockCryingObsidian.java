package com.stormister.rediscovered.blocks;

import com.stormister.rediscovered.mod_Rediscovered;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockCryingObsidian extends Block {
	private final String name = "CryingObsidian";

	public BlockCryingObsidian() {
		super(Material.rock);
		this.setHarvestLevel("pickaxe", 3);
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(mod_Rediscovered.modid + "_" + name);
	}

	public String getName() {
		return name;
	}
}