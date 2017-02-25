package com.stormister.rediscovered.blocks;

import java.util.Random;

import com.stormister.rediscovered.Rediscovered;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRuby extends Block {
	private final String name = "RubyBlock";

	public BlockRuby(String texture) {
		super(Material.rock);
		this.setHarvestLevel("pickaxe", 1);
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(Rediscovered.modid + "_" + name);
	}

	public String getName() {
		return name;
	}

	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	public Block idDropped(int i, Random random, int j) {
		return Rediscovered.RubyBlock;
	}

	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(Random random) {
		return 1;
	}
}
