package com.stormister.rediscovered.world;

import java.util.Random;

import com.stormister.rediscovered.Rediscovered;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGeneratorPigmanVillage implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider) {
		if ((world.provider.getDimensionId() == Rediscovered.DimID) && Rediscovered.EnablePigmanVillages) {
			generateSky(world, random, chunkX * 16, chunkZ * 16);
		}

	}

	public void generateSky(World world, Random rand, int y, int z) {

		rand.nextInt(16);
		rand.nextInt(150);
		rand.nextInt(16);
	}

}