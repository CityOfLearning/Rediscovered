package com.stormister.rediscovered.blocks;

import com.stormister.rediscovered.mod_Rediscovered;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockCherryLog extends BlockLog {
	static final class SwitchEnumAxis {
		static final int[] AXIS_LOOKUP = new int[BlockLog.EnumAxis.values().length];

		static {
			try {
				AXIS_LOOKUP[BlockLog.EnumAxis.X.ordinal()] = 1;
			} catch (NoSuchFieldError var3) {
				;
			}

			try {
				AXIS_LOOKUP[BlockLog.EnumAxis.Z.ordinal()] = 2;
			} catch (NoSuchFieldError var2) {
				;
			}

			try {
				AXIS_LOOKUP[BlockLog.EnumAxis.NONE.ordinal()] = 3;
			} catch (NoSuchFieldError var1) {
				;
			}
		}
	}

	/** The type of tree this log came from. */
	public static final String[] woodType = new String[] { "cherry" };

	private final String name = "CherryLog";

	public BlockCherryLog(String texture) {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(mod_Rediscovered.modid + "_" + name);
		setCreativeTab(CreativeTabs.tabBlock);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { LOG_AXIS });
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		byte b0 = 0;
		int i = b0;

		switch (BlockCherryLog.SwitchEnumAxis.AXIS_LOOKUP[state.getValue(LOG_AXIS).ordinal()]) {
		case 1:
			i |= 4;
			break;
		case 2:
			i |= 8;
			break;
		case 3:
			i |= 12;
		}

		return i;
	}

	public String getName() {
		return name;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = getDefaultState();

		switch (meta & 12) {
		case 0:
			iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
			break;
		case 4:
			iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
			break;
		case 8:
			iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
			break;
		default:
			iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
		}

		return iblockstate;
	}
}