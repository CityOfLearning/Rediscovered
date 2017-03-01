package com.stormister.rediscovered.world;

import com.stormister.rediscovered.Rediscovered;

import net.minecraft.util.BlockPos;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
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
	public boolean canRespawnHere() {
		return true;
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
		return 25;
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
	@SideOnly(Side.CLIENT)
	public double getVoidFogYFactor() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isSkyColored() {
		return true;
	}

	@Override
	public void registerWorldChunkManager() {
		worldChunkMgr = new WorldChunkManagerHell(Rediscovered.heaven, dimensionId);
		worldObj.setSeaLevel(25);
		hasNoSky = false;
	}
	
	public BlockPos getRandomizedSpawnPoint()
    {
		BlockPos ret = this.worldObj.getSpawnPoint();

        if (worldObj.getWorldInfo().getGameType() != WorldSettings.GameType.ADVENTURE)
        {
            ret = worldObj.getTopSolidOrLiquidBlock(ret.add(worldObj.rand.nextInt(64) - 32, 0, worldObj.rand.nextInt(64) - 32));
            
            while(!canCoordinateBeSpawn(ret.getX(), ret.getZ())){
            	ret = worldObj.getTopSolidOrLiquidBlock(ret.add(worldObj.rand.nextInt(64) - 32, 0, worldObj.rand.nextInt(64) - 32));
            }
        }
        return ret;
    }
}