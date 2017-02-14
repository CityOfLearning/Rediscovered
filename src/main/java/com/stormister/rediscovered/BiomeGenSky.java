package com.stormister.rediscovered;

import java.util.Random;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeGenSky extends BiomeGenBase {
	public BiomeGenSky(int par1) {
		super(par1);
		spawnableMonsterList.clear();
		spawnableCreatureList.clear();
		spawnableWaterCreatureList.clear();
		spawnableCaveCreatureList.clear();
		spawnableCreatureList
				.add(new BiomeGenBase.SpawnListEntry(EntityGiant.class, mod_Rediscovered.GiantSpawn, 2, 2));
		spawnableCreatureList
				.add(new BiomeGenBase.SpawnListEntry(EntityGoodDragon.class, mod_Rediscovered.RedDragonSpawn, 4, 4));
		spawnableCreatureList
				.add(new BiomeGenBase.SpawnListEntry(EntitySkyChicken.class, mod_Rediscovered.SkyChickenSpawn, 6, 6));
	}

	/**
	 * Gets a WorldGen appropriate for this biome.
	 */
	@Override
	public WorldGenAbstractTree genBigTreeChance(Random par1Random) {
		return (new WorldGenCherryTrees(true));
	}
}