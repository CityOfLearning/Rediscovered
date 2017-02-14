package com.stormister.rediscovered;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEmptyPeonyBush extends BlockBush {
	public static final String[] field_149859_a = new String[] { "peony" };
	public static final String[] field_149858_b = new String[] { "peony" };

	public static int func_149856_f(String p_149856_0_) {
		int i;

		for (i = 0; i < field_149858_b.length; ++i) {
			if (field_149858_b[i].equals(p_149856_0_)) {
				return i;
			}
		}

		for (i = 0; i < field_149859_a.length; ++i) {
			if (field_149859_a[i].equals(p_149856_0_)) {
				return i;
			}
		}

		return 0;
	}

	public static BlockFlower func_149857_e(String p_149857_0_) {
		String[] astring = field_149858_b;
		int i = astring.length;
		int j;
		String s1;

		for (j = 0; j < i; ++j) {
			s1 = astring[j];

			if (s1.equals(p_149857_0_)) {
				return Blocks.yellow_flower;
			}
		}

		astring = field_149859_a;
		i = astring.length;

		for (j = 0; j < i; ++j) {
			s1 = astring[j];

			if (s1.equals(p_149857_0_)) {
				return Blocks.red_flower;
			}
		}

		return null;
	}

	protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos) {
		float f = 1.0F;
		BlockPos blockpos1 = pos.down();

		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				float f1 = 0.0F;
				worldIn.getBlockState(blockpos1.add(i, 0, j));

				f1 = 3.0F;

				if ((i != 0) || (j != 0)) {
					f1 /= 4.0F;
				}

				f += f1;
			}
		}

		BlockPos blockpos2 = pos.north();
		BlockPos blockpos3 = pos.south();
		BlockPos blockpos4 = pos.west();
		BlockPos blockpos5 = pos.east();
		boolean flag = (blockIn == worldIn.getBlockState(blockpos4).getBlock())
				|| (blockIn == worldIn.getBlockState(blockpos5).getBlock());
		boolean flag1 = (blockIn == worldIn.getBlockState(blockpos2).getBlock())
				|| (blockIn == worldIn.getBlockState(blockpos3).getBlock());

		if (flag && flag1) {
			f /= 2.0F;
		} else {
			boolean flag2 = (blockIn == worldIn.getBlockState(blockpos4.north()).getBlock())
					|| (blockIn == worldIn.getBlockState(blockpos5.north()).getBlock())
					|| (blockIn == worldIn.getBlockState(blockpos5.south()).getBlock())
					|| (blockIn == worldIn.getBlockState(blockpos4.south()).getBlock());

			if (flag2) {
				f /= 2.0F;
			}
		}

		return f;
	}

	private final String name = "EmptyPeonyBush";

	protected BlockEmptyPeonyBush(int par1) {
		super(Material.plants);
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(mod_Rediscovered.modid + "_" + name);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		worldIn.setBlockToAir(pos.up());
	}

	/**
	 * Checks to see if its valid to put this block at the specified
	 * coordinates. Args: world, x, y, z
	 */
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return super.canPlaceBlockAt(worldIn, pos) && canBlockStay(worldIn, pos, worldIn.getBlockState(pos));
	}

	/**
	 * is the block grass, dirt or farmland
	 */
	@Override
	protected boolean canPlaceBlockOn(Block p_149854_1_) {
		return (p_149854_1_ == Blocks.grass) || (p_149854_1_ == Blocks.dirt) || (p_149854_1_ == Blocks.farmland);
	}

	/**
	 * checks if the block can stay, if not drop as item
	 */
	@Override
	protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state) {
		if (!canBlockStay(world, pos, state)) {
			world.setBlockToAir(pos);
		}
	}

	/**
	 * Determines the damage on the item the block drops. Used in cloth and
	 * wood.
	 */
	public int damageDropped(int p_149692_1_) {
		return p_149692_1_;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		return null;
	}

	public String getName() {
		return name;
	}

	@SideOnly(Side.CLIENT)

	/**
	 * only called by clickMiddleMouseButton , and passed to
	 * inventory.setCurrentItem (along with isCreative)
	 */
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
		return new ItemStack(mod_Rediscovered.EmptyPeonyBush);
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) {
		list.add(new ItemStack(itemIn, 1));
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False
	 * (examples: signs, buttons, stairs, etc)
	 */
	@Override
	public boolean isNormalCube() {
		return false;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube? This determines whether
	 * or not to render the shared face of two adjacent blocks and also whether
	 * the player can attach torches, redstone wire, etc to this block.
	 */
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		worldIn.setBlockState(pos.up(), mod_Rediscovered.EmptyPeonyBushTop.getDefaultState());
		return mod_Rediscovered.EmptyPeonyBush.getDefaultState();
	}

	/**
	 * Lets the block know when one of its neighbor changes. Doesn't know which
	 * neighbor changed (coordinates passed are their own) Args: x, y, z,
	 * neighbor Block
	 */
	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block neighborBlock) {
		super.onNeighborBlockChange(world, pos, state, neighborBlock);
		checkAndDropBlock(world, pos, state);
	}

	/**
	 * Updates the blocks bounds based on its current state. Args: world, x, y,
	 * z
	 */
	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		checkAndDropBlock(world, pos, state);

		if (world.getLightFromNeighbors(pos.up()) >= 9) {
			float f = getGrowthChance(this, world, pos);

			if (rand.nextInt((int) (25.0F / f) + 1) == 0) {
				world.setBlockToAir(pos.up());
				Blocks.double_plant.placeAt(world, pos, BlockDoublePlant.EnumPlantType.PAEONIA, 2);
			}
		}
	}
}