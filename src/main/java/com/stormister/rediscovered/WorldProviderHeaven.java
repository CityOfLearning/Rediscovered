package com.stormister.rediscovered;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderHeaven extends WorldProvider {
	@Override
	public float calculateCelestialAngle(long par1, float par3) {
		return 0.0F;
	}

	@Override
	public boolean canCoordinateBeSpawn(int par1, int par2) {
		return false;
	}

	@Override
	public boolean canRespawnHere() {
		return false;
	}

	@Override
	public IChunkProvider createChunkGenerator() {
		return new ChunkProviderHeaven(worldObj, worldObj.getSeed());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean doesXZShowFog(int par1, int par2) {
		return false;
	}

	@Override
	public int getAverageGroundLevel() {
		return 200;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getCloudHeight() {
		return 0F;
	}

	@Override
	public String getDimensionName() {
		return "Sky";
	}

	@Override
	public String getInternalNameSuffix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getMovementFactor() {
		return 1.2;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getVoidFogYFactor() {
		return 30;
	}

	// public ChunkCoordinates getEntrancePortalLocation() {
	// return new ChunkCoordinates(50, 75, 0);
	// }
	@Override
	@SideOnly(Side.CLIENT)
	public String getWelcomeMessage() {
		if ((this instanceof WorldProviderHeaven)) {
			return "Falling Asleep...";
		}
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isSkyColored() {
		return true;
	}

	@Override
	public void registerWorldChunkManager() {
		worldChunkMgr = new WorldChunkManagerHell(mod_Rediscovered.heaven, dimensionId);
		dimensionId = mod_Rediscovered.DimID;
		hasNoSky = false;
	}

}