package com.stormister.rediscovered;

import java.util.Random;

import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRubyOre extends BlockOre {
	private final String name = "RubyOre";

	public BlockRubyOre(String texture) {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(mod_Rediscovered.modid + "_" + name);
		setCreativeTab(CreativeTabs.tabBlock);
		this.setHarvestLevel("pickaxe", 2);
	}

	@Override
	public int getExpDrop(net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
		if (getItemDropped(world.getBlockState(pos), RANDOM, fortune) != Item.getItemFromBlock(this)) {
			return 1 + RANDOM.nextInt(5);
		}
		return 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return mod_Rediscovered.gemRuby;
	}

	public String getName() {
		return name;
	}
}
