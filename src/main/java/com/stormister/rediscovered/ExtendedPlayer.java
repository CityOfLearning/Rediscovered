package com.stormister.rediscovered;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedPlayer implements IExtendedEntityProperties {
	public final static String EXT_PROP_NAME = "RediscoveredPlayerSkyRespawnCoords";

	/**
	 * Returns ExtendedPlayer properties for player This method is for
	 * convenience only; it will make your code look nicer
	 */
	public static final ExtendedPlayer get(EntityPlayer player) {
		return (ExtendedPlayer) player.getExtendedProperties(EXT_PROP_NAME);
	}

	/**
	 * Used to register these extended properties for the player during
	 * EntityConstructing event This method is for convenience only; it will
	 * make your code look nicer
	 */
	public static final void register(EntityPlayer player) {
		player.registerExtendedProperties(ExtendedPlayer.EXT_PROP_NAME, new ExtendedPlayer(player));
	}

	private int respawnX = 0, respawnY = 64, respawnZ = 0;

	public ExtendedPlayer(EntityPlayer player) {
	}

	public BlockPos getRespawn() {
		BlockPos coordinates = new BlockPos(respawnX, respawnY, respawnZ);
		return coordinates;
	}

	@Override
	public void init(Entity entity, World world) {
	}

	// Load whatever data you saved
	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
		respawnX = properties.getInteger("RespawnX");
		respawnY = properties.getInteger("RespawnY");
		respawnZ = properties.getInteger("RespawnZ");
	}

	// Save any custom data that needs saving here
	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = new NBTTagCompound();

		properties.setInteger("RespawnX", respawnX);
		properties.setInteger("RespawnY", respawnY);
		properties.setInteger("RespawnZ", respawnZ);

		compound.setTag(EXT_PROP_NAME, properties);
	}

	/**
	 * Returns true if the amount of mana was consumed or false if the player's
	 * current mana was insufficient
	 */
	public void setRespawn(int x, int y, int z) {
		respawnX = x;
		respawnY = y;
		respawnZ = z;
		System.out.println("[REDISCOVERED]Respawn coords from NBT: " + respawnX + "/" + respawnY + "/" + respawnZ);
	}
}