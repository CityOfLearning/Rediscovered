package com.stormister.rediscovered.blocks;

import java.util.List;
import java.util.Random;

import com.stormister.rediscovered.Rediscovered;
import com.stormister.rediscovered.world.WorldGenCherryTrees;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCherrySapling extends BlockSapling {
	// private static final PropertyBool TYPE = PropertyBool.create("cherry");
	// public static final PropertyInteger STAGE =
	// PropertyInteger.create("stage", 0, 1);

	private final String name = "CherrySapling";

	public BlockCherrySapling() {
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(Rediscovered.modid + "_" + name);
		float f = 0.4F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
		setCreativeTab(CreativeTabs.tabDecorations);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, BlockPlanks.EnumType.OAK).withProperty(STAGE,
				Integer.valueOf(0)));
	}

	@Override
	public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) {
			return;
		}
		Object object = rand.nextInt(10) == 0 ? new WorldGenBigTree(true) : new WorldGenTrees(true);
		object = new WorldGenCherryTrees(true);

		IBlockState iblockstate1 = Blocks.air.getDefaultState();

		worldIn.setBlockState(pos, iblockstate1, 4);

		if (!((WorldGenerator) object).generate(worldIn, rand, pos)) {
			worldIn.setBlockState(pos, state, 4);
		}
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;
		i = i | state.getValue(TYPE).getMetadata();
		i = i | (state.getValue(STAGE).intValue() << 3);
		return i;
	}

	public String getName() {
		return name;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, BlockPlanks.EnumType.OAK).withProperty(STAGE,
				Integer.valueOf((meta & 8) >> 3));
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		list.add(new ItemStack(itemIn, 1, 0));
	}
}