package com.stormister.rediscovered.blocks;

import java.util.Random;

import com.stormister.rediscovered.mod_Rediscovered;
import com.stormister.rediscovered.world.WorldGenCherryTrees;

import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockCherrySapling extends BlockBush implements IGrowable {
	private static final PropertyBool TYPE = PropertyBool.create("cherry");
	public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);
	private final String name = "CherrySapling";

	public BlockCherrySapling() {
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(mod_Rediscovered.modid + "_" + name);
		float f = 0.4F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
		setCreativeTab(CreativeTabs.tabDecorations);
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return worldIn.rand.nextFloat() < 0.45D;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { TYPE, STAGE });
	}

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

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	public String getName() {
		return name;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, false).withProperty(STAGE, Integer.valueOf((meta & 8) >> 3));
	}

	public void grow(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (state.getValue(STAGE).intValue() == 0) {
			worldIn.setBlockState(pos, state.cycleProperty(STAGE), 4);
		} else {
			generateTree(worldIn, pos, state, rand);
		}
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		this.grow(worldIn, pos, state, rand);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			super.updateTick(worldIn, pos, state, rand);

			if ((worldIn.getLightFromNeighbors(pos.up()) >= 9) && (rand.nextInt(7) == 0)) {
				this.grow(worldIn, pos, state, rand);
			}
		}
	}
}