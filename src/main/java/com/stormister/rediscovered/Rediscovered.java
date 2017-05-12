package com.stormister.rediscovered;

import java.util.HashMap;
import java.util.Random;

import org.apache.logging.log4j.Logger;

import com.stormister.rediscovered.entity.EntityFish;
import com.stormister.rediscovered.entity.EntityParrow;
import com.stormister.rediscovered.entity.EntitySkeletonHorse;
import com.stormister.rediscovered.entity.EntityZombieHorse;
import com.stormister.rediscovered.world.WorldGeneratorPigmanVillage;
import com.stormister.rediscovered.world.WorldGeneratorRuby;
import com.stormister.rediscovered.world.empty.WorldProviderVoid;
import com.stormister.rediscovered.world.sky.BiomeGenSky;
import com.stormister.rediscovered.world.sky.WorldProviderSky;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Rediscovered.modid, name = "Minecraft Rediscovered Mod", version = "1.3")

/*
 * Current Changelog - 1.3 -Added splash potions and changed how current potions
 * work -Changed rotating gear animation -Changed Leather-Chain to Studded
 * -Added Studded Chestplate with quiver
 */

public class Rediscovered {
	public static final String modid = "Rediscovered";
	@Instance
	public static Rediscovered instance;
	public static Configuration config;
	public static HashMap<String, BlockPos> usernameLastPosMap = new HashMap<String, BlockPos>();
	public static final int guiIDLockedChest = 0;

	public static Logger logger;

	public static int ZombieHorseSpawn;

	public static int SkeletonHorseSpawn;
	public static int RedDragonSpawn;
	public static int SkyChickenSpawn;
	public static int GiantSpawn;
	public static int FishSpawn;
	public static int SkyBiomeID;
	public static boolean EnablePigmanVillages;
	public static boolean EnableQuivers;
	public static boolean EnableDungeonLoot;
	public static boolean EnableRubyOre;
	public static boolean GVillagerSpawn;
	public static int nextID = 0;
	public static BiomeGenBase sky;
	@SidedProxy(clientSide = "com.stormister.rediscovered.ClientProxyRediscovered", serverSide = "com.stormister.rediscovered.CommonProxyRediscovered")
	public static CommonProxyRediscovered proxy;

	// Purple Arrow Damage
	public static DamageSource causeParrowDamage(EntityParrow par0EntityDarkArrow, Entity par1Entity) {
		return (new EntityDamageSourceIndirect("parrow", par0EntityDarkArrow, par1Entity)).setProjectile();
	}

	public static int nextInternalID() {
		Rediscovered.nextID++;
		return Rediscovered.nextID - 1;
	}

	public Random ChunkGenRand;
	public int ChunkGenRandNum;

	public String getModName() {
		return "rediscovered";
	}

	public String getVersion() {
		return "1.8.9";
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init();
		proxy.registerItemRenderers();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		logger = event.getModLog();

		registerConfig(event);

		sky = (new BiomeGenSky(SkyBiomeID)).setColor(16421912).setBiomeName("Sky").setDisableRain();

		instance = this;
		MinecraftForge.EVENT_BUS.register(new RediscoveredEventHandler());
		MinecraftForge.EVENT_BUS.register(new Sniffer());

		EntitySpawnPlacementRegistry.setPlacementType(EntityFish.class, EntityLiving.SpawnPlacementType.IN_WATER);
		EntityRegistry.addSpawn(EntityFish.class, FishSpawn, 5, 20, EnumCreatureType.WATER_CREATURE, BiomeGenBase.beach,
				BiomeGenBase.extremeHills, BiomeGenBase.extremeHillsEdge, BiomeGenBase.forest, BiomeGenBase.forestHills,
				BiomeGenBase.jungle, BiomeGenBase.jungleHills, BiomeGenBase.mushroomIsland,
				BiomeGenBase.mushroomIslandShore, BiomeGenBase.ocean, BiomeGenBase.plains, BiomeGenBase.river,
				BiomeGenBase.swampland, sky);
		EntityRegistry.addSpawn(EntityZombieHorse.class, ZombieHorseSpawn, 1, 1, EnumCreatureType.MONSTER,
				BiomeGenBase.beach, BiomeGenBase.extremeHills, BiomeGenBase.extremeHillsEdge, BiomeGenBase.forest,
				BiomeGenBase.forestHills, BiomeGenBase.jungleHills, BiomeGenBase.plains, BiomeGenBase.river,
				BiomeGenBase.swampland, BiomeGenBase.taiga, BiomeGenBase.icePlains, BiomeGenBase.desert,
				BiomeGenBase.megaTaiga, BiomeGenBase.roofedForest, BiomeGenBase.mesa, BiomeGenBase.savanna);
		EntityRegistry.addSpawn(EntitySkeletonHorse.class, SkeletonHorseSpawn, 1, 1, EnumCreatureType.MONSTER,
				BiomeGenBase.beach, BiomeGenBase.extremeHills, BiomeGenBase.extremeHillsEdge, BiomeGenBase.forest,
				BiomeGenBase.forestHills, BiomeGenBase.jungleHills, BiomeGenBase.plains, BiomeGenBase.river,
				BiomeGenBase.swampland, BiomeGenBase.taiga, BiomeGenBase.icePlains, BiomeGenBase.desert,
				BiomeGenBase.megaTaiga, BiomeGenBase.roofedForest, BiomeGenBase.mesa, BiomeGenBase.savanna);

		DimensionManager.registerProviderType(DimensionManager.getNextFreeDimId(), WorldProviderSky.class, true);
		DimensionManager.registerProviderType(DimensionManager.getNextFreeDimId(), WorldProviderVoid.class, true);
		BiomeManager.removeSpawnBiome(sky);
		BiomeManager.removeStrongholdBiome(sky);
		BiomeManager.removeVillageBiome(sky);
		WorldChunkManager.allowedBiomes.remove(sky);

		if (Rediscovered.EnableRubyOre) {
			GameRegistry.registerWorldGenerator(new WorldGeneratorRuby(), 0);
		}
		if (Rediscovered.EnablePigmanVillages) {
			GameRegistry.registerWorldGenerator(new WorldGeneratorPigmanVillage(), 0);
		}

		proxy.preInit();

		proxy.registerEntityRenderers();
	}

	// Config
	public void registerConfig(FMLPreInitializationEvent e) {
		config = new Configuration(e.getSuggestedConfigurationFile());
		config.load();

		// IDs
		SkyBiomeID = config.get("ID's", "Sky Biome ID", 153).getInt();

		// Booleans
		EnableQuivers = config.get("Options", "Enable Quivers", true).getBoolean(true);
		EnableDungeonLoot = config.get("Options", "Enable Lanterns appear in Dungeon Chests", true).getBoolean(true);
		EnableRubyOre = config.get("Options", "Enable Ruby Ore Generates Underground", true).getBoolean(true);
		EnablePigmanVillages = config.get("Options", "Enable Pigman Villages in the Sky Dimension", true)
				.getBoolean(true);
		GVillagerSpawn = config.get("Options", "Enable Green Villager Spawn in Villages", true).getBoolean(true);
		ZombieHorseSpawn = config.get("Options", "Zombie Horse Spawn Rate", 25).getInt();
		SkeletonHorseSpawn = config.get("Options", "Skeleton Horse Spawn Rate", 25).getInt();
		RedDragonSpawn = config.get("Options", "Red Dragon Spawn Rate", 50).getInt();
		SkyChickenSpawn = config.get("Options", "Sky Chicken Spawn Rate", 150).getInt();
		GiantSpawn = config.get("Options", "Giant Spawn Rate", 150).getInt();
		FishSpawn = config.get("Options", "Fish Spawn Rate", 150).getInt();
		config.save();
	}

}
