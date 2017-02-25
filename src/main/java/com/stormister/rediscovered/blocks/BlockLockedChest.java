package com.stormister.rediscovered.blocks;

import java.util.Random;

import com.stormister.rediscovered.Rediscovered;
import com.stormister.rediscovered.blocks.tiles.TileEntityLockedChest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLockedChest extends BlockContainer {
	@SideOnly(Side.CLIENT)

	static final class SwitchEnumFacing {
		static final int[] FACING_LOOKUP = new int[EnumFacing.values().length];

		static {
			try {
				FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 1;
			} catch (NoSuchFieldError var4) {
				;
			}

			try {
				FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 2;
			} catch (NoSuchFieldError var3) {
				;
			}

			try {
				FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 3;
			} catch (NoSuchFieldError var2) {
				;
			}

			try {
				FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 4;
			} catch (NoSuchFieldError var1) {
				;
			}
		}
	}

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	public static void setState(boolean active, World worldIn, BlockPos pos) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		TileEntity tileentity = worldIn.getTileEntity(pos);

		worldIn.setBlockState(pos,
				Rediscovered.LockedChest.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
		worldIn.setBlockState(pos,
				Rediscovered.LockedChest.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);

		if (tileentity != null) {
			tileentity.validate();
			worldIn.setTileEntity(pos, tileentity);
		}
	}

	private final String name = "LockedChest";

	public BlockLockedChest(String textureName) {
		super(Material.wood);
		setCreativeTab(CreativeTabs.tabDecorations);
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(Rediscovered.modid + "_" + name);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING });
	}

	/**
	 * each class overrdies this to return a new <className>
	 */
	@Override
	public TileEntity createNewTileEntity(World par1World, int par1) {
		return new TileEntityLockedChest();
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(Rediscovered.LockedChest);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	public String getName() {
		return name;
	}

	@Override
	public int getRenderType() {
		return 3;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IBlockState getStateForEntityRender(IBlockState state) {
		return getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
			enumfacing = EnumFacing.NORTH;
		}

		return getDefaultState().withProperty(FACING, enumfacing);
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			playerIn.openGui(Rediscovered.instance, Rediscovered.guiIDLockedChest, world, pos.getX(), pos.getY(),
					pos.getZ());
		}
		return true;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		setDefaultFacing(worldIn, pos, state);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
			int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

		if (stack.hasDisplayName()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileEntityFurnace) {
				((TileEntityFurnace) tileentity).setCustomInventoryName(stack.getDisplayName());
			}
		}
	}

	private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
		if (!worldIn.isRemote) {
			Block block = worldIn.getBlockState(pos.north()).getBlock();
			Block block1 = worldIn.getBlockState(pos.south()).getBlock();
			Block block2 = worldIn.getBlockState(pos.west()).getBlock();
			Block block3 = worldIn.getBlockState(pos.east()).getBlock();
			EnumFacing enumfacing = state.getValue(FACING);

			if ((enumfacing == EnumFacing.NORTH) && block.isFullBlock() && !block1.isFullBlock()) {
				enumfacing = EnumFacing.SOUTH;
			} else if ((enumfacing == EnumFacing.SOUTH) && block1.isFullBlock() && !block.isFullBlock()) {
				enumfacing = EnumFacing.NORTH;
			} else if ((enumfacing == EnumFacing.WEST) && block2.isFullBlock() && !block3.isFullBlock()) {
				enumfacing = EnumFacing.EAST;
			} else if ((enumfacing == EnumFacing.EAST) && block3.isFullBlock() && !block2.isFullBlock()) {
				enumfacing = EnumFacing.WEST;
			}

			worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
	}
}