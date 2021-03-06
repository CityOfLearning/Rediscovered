package com.stormister.rediscovered.items;

import com.stormister.rediscovered.Rediscovered;
import com.stormister.rediscovered.RediscoveredItemsManager;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemTable extends Item {
	private final String name = "ItemTable";
	private Block block = RediscoveredItemsManager.Table;

	public ItemTable() {
		super();
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(Rediscovered.modid + "_" + name);
	}

	public String getName() {
		return name;
	}

	/**
	 * Callback for item usage. If the item does something special on right
	 * clicking, he will have one of those. Return True if something happen and
	 * false if it don't. This is for ITEMS, not BLOCKS
	 */
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		IBlockState iblockstate = worldIn.getBlockState(pos);
		Block block = iblockstate.getBlock();

		if ((block == Blocks.snow_layer) && (iblockstate.getValue(BlockSnow.LAYERS).intValue() < 1)) {
			side = EnumFacing.UP;
		} else if (!block.isReplaceable(worldIn, pos)) {
			pos = pos.offset(side);
		}

		if (!playerIn.canPlayerEdit(pos, side, stack)) {
			return false;
		} else if (stack.stackSize == 0) {
			return false;
		} else {
			if (worldIn.canBlockBePlaced(this.block, pos, false, side, (Entity) null, stack)) {
				IBlockState iblockstate1 = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, 0, playerIn);

				if (worldIn.setBlockState(pos, iblockstate1, 3)) {
					iblockstate1 = worldIn.getBlockState(pos);

					if (iblockstate1.getBlock() == this.block) {
						ItemBlock.setTileEntityNBT(worldIn, playerIn, pos, stack);
						iblockstate1.getBlock().onBlockPlacedBy(worldIn, pos, iblockstate1, playerIn, stack);
					}

					worldIn.playSoundEffect(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F,
							this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F,
							this.block.stepSound.getFrequency() * 0.8F);
					--stack.stackSize;
					return true;
				}
			}

			return false;
		}
	}
}
