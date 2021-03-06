package com.stormister.rediscovered;

import com.stormister.rediscovered.blocks.tiles.TileEntityLockedChest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class RediscoveredGuiHandler implements IGuiHandler {

	public RediscoveredGuiHandler() {
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileentity = world.getTileEntity(new BlockPos(x, y, z));

		switch (ID) {
		case Rediscovered.guiIDLockedChest:
			if (tileentity instanceof TileEntityLockedChest) {
				return new GuiLockedChest(player);
			}
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileentity = world.getTileEntity(new BlockPos(x, y, z));

		switch (ID) {
		case Rediscovered.guiIDLockedChest:
			if (tileentity instanceof TileEntityLockedChest) {
				return new ContainerLockedChest(player);
			}
		}
		return null;
	}

}
