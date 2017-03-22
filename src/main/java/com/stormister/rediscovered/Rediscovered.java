package com.stormister.rediscovered;

import java.util.HashMap;
import java.util.Random;

import com.stormister.rediscovered.blocks.BlockChair;
import com.stormister.rediscovered.blocks.BlockCherryDoubleSlab;
import com.stormister.rediscovered.blocks.BlockCherryHalfSlab;
import com.stormister.rediscovered.blocks.BlockCherryLeaves;
import com.stormister.rediscovered.blocks.BlockCherryLog;
import com.stormister.rediscovered.blocks.BlockCherrySapling;
import com.stormister.rediscovered.blocks.BlockCherryStairs;
import com.stormister.rediscovered.blocks.BlockCherryWood;
import com.stormister.rediscovered.blocks.BlockDirtDoubleSlab;
import com.stormister.rediscovered.blocks.BlockDirtHalfSlab;
import com.stormister.rediscovered.blocks.BlockDragonEggRed;
import com.stormister.rediscovered.blocks.BlockEmptyPeonyBush;
import com.stormister.rediscovered.blocks.BlockEmptyPeonyBushTop;
import com.stormister.rediscovered.blocks.BlockEmptyRoseBush;
import com.stormister.rediscovered.blocks.BlockEmptyRoseBushTop;
import com.stormister.rediscovered.blocks.BlockLantern;
import com.stormister.rediscovered.blocks.BlockLanternPhys;
import com.stormister.rediscovered.blocks.BlockLectern;
import com.stormister.rediscovered.blocks.BlockLecternOpen;
import com.stormister.rediscovered.blocks.BlockLockedChest;
import com.stormister.rediscovered.blocks.BlockPeony;
import com.stormister.rediscovered.blocks.BlockRose;
import com.stormister.rediscovered.blocks.BlockRuby;
import com.stormister.rediscovered.blocks.BlockRubyOre;
import com.stormister.rediscovered.blocks.BlockSpikes;
import com.stormister.rediscovered.blocks.BlockTable;
import com.stormister.rediscovered.blocks.tiles.TileEntityLectern;
import com.stormister.rediscovered.blocks.tiles.TileEntityLecternOpen;
import com.stormister.rediscovered.blocks.tiles.TileEntityLockedChest;
import com.stormister.rediscovered.blocks.tiles.TileEntityTable;
import com.stormister.rediscovered.entity.EntityFish;
import com.stormister.rediscovered.entity.EntityGiant;
import com.stormister.rediscovered.entity.EntityGoodDragon;
import com.stormister.rediscovered.entity.EntityGreenVillager;
import com.stormister.rediscovered.entity.EntityMountableBlock;
import com.stormister.rediscovered.entity.EntityParrow;
import com.stormister.rediscovered.entity.EntityPigmanMob;
import com.stormister.rediscovered.entity.EntityPigmanVillager;
import com.stormister.rediscovered.entity.EntityRediscoveredPotion;
import com.stormister.rediscovered.entity.EntityScarecrow;
import com.stormister.rediscovered.entity.EntitySkeletonHorse;
import com.stormister.rediscovered.entity.EntitySkyChicken;
import com.stormister.rediscovered.entity.EntityZombieHorse;
import com.stormister.rediscovered.items.ItemBlockCherrySlab;
import com.stormister.rediscovered.items.ItemBlockDirtSlab;
import com.stormister.rediscovered.items.ItemLantern;
import com.stormister.rediscovered.items.ItemLeatherChain;
import com.stormister.rediscovered.items.ItemPotionRediscovered;
import com.stormister.rediscovered.items.ItemQuiver;
import com.stormister.rediscovered.items.ItemRuby;
import com.stormister.rediscovered.items.ItemScarecrow;
import com.stormister.rediscovered.world.WorldGeneratorPigmanVillage;
import com.stormister.rediscovered.world.WorldGeneratorRuby;
import com.stormister.rediscovered.world.empty.WorldProviderVoid;
import com.stormister.rediscovered.world.sky.BiomeGenSky;
import com.stormister.rediscovered.world.sky.WorldProviderHeaven;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

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
	// public static Block Gear;
	// public static Item ItemGear;
	public static BlockDirtHalfSlab DirtSlab;
	public static BlockDirtDoubleSlab DirtDoubleSlab;
	public static BlockCherryHalfSlab CherrySlab;
	public static BlockCherryDoubleSlab CherryDoubleSlab;
	public static Block CherryStairs;
	public static Item Quiver;
	public static Item LeatherQuiver;
	public static Item ChainQuiver;
	public static Item GoldQuiver;
	public static Item IronQuiver;
	public static Item DiamondQuiver;
	public static Item LeatherChainQuiver;
	public static Item LeatherChainHelmet;
	public static Item LeatherChainChest;
	public static Item LeatherChainLegs;
	public static Item LeatherChainBoots;
	public static Item RediscoveredPotion;
	public static Block RubyOre;
	public static Block RubyBlock;
	public static Item gemRuby;
	public static BlockCherryLog CherryLog;
	public static Block CherryPlank;
	public static Block CherryLeaves;
	public static Block CherrySapling;
	public static Block Rose;
	public static Block EmptyRoseBush;
	public static Block EmptyRoseBushTop;
	public static Block Peony;
	public static Block EmptyPeonyBush;
	public static Block EmptyPeonyBushTop;
	public static Block Spikes;
	public static Block DragonEggRed;
	public static Block LockedChest;
	public static BlockChair Chair;
	public static BlockTable Table;
	public static Block Lectern;
	public static Block LecternOpen;
	public static Block Lantern;
	public static Block LanternPhys;
	public static Item ItemLantern;
	public static Item Scarecrow;
	public static int ZombieHorseSpawn;

	public static int SkeletonHorseSpawn;
	public static int RedDragonSpawn;
	public static int SkyChickenSpawn;
	public static int GiantSpawn;
	public static int FishSpawn;
	public static int PurpleArrowID;
	public static int PotionID;
	public static int MountableBlockID;
	public static int PigmanID;
	public static int PigmanMobID;
	public static int GreenVillagerID;
	public static int SkyChickenID;
	public static int GiantID;
	public static int FishID;
	public static int ZombieHorseID;
	public static int SkeletonHorseID;
	public static int ScarecrowID;
	public static int RedDragonID;
	public static int HeavenBiomeID;
	public static boolean EnablePigmanVillages;
	public static boolean EnableQuivers;
	public static boolean EnableDungeonLoot;
	public static boolean EnableRubyOre;
	public static boolean GVillagerSpawn;
	public static int nextID = 0;
	public static BiomeGenBase heaven;
	public static ArmorMaterial EnumArmorMaterialInvinc = EnumHelper.addArmorMaterial("Invincible", "Quiver", -1,
			new int[] { 0, 0, 0, 0 }, 0);

	public static ArmorMaterial EnumArmorMaterialLC = EnumHelper.addArmorMaterial("LC", "leatherchain", 20,
			new int[] { 3, 8, 6, 2 }, 27);
	@SidedProxy(clientSide = "com.stormister.rediscovered.ClientProxyRediscovered", serverSide = "com.stormister.rediscovered.CommonProxyRediscovered")
	public static CommonProxyRediscovered proxy;

	// Purple Arrow Damage
	public static DamageSource causeParrowDamage(EntityParrow par0EntityDarkArrow, Entity par1Entity) {
		return (new EntityDamageSourceIndirect("parrow", par0EntityDarkArrow, par1Entity)).setProjectile();
	}

	// Lantern Hotbar Check
	public static boolean hasLitLanternOnHotbar(InventoryPlayer inv) {
		for (int i = 0; i < 9; i++) {
			ItemStack item = inv.mainInventory[i];

			if ((item != null) && (item.getItem() == ItemLantern)) {
				return true;
			}
		}

		return false;
	}

	public static int nextInternalID() {
		Rediscovered.nextID++;
		return Rediscovered.nextID - 1;
	}

	// Block Renders
	public static void registerBlockRenders(Block block, String name) {
		Item item = Item.getItemFromBlock(block);
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(item, 0,
				new ModelResourceLocation(Rediscovered.modid + ":" + name, "inventory"));
	}

	public static void registerItemRenders(Item item, int meta, String name) {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(item, meta,
				new ModelResourceLocation(Rediscovered.modid + ":" + name, "inventory"));
	}

	// Item Renders
	public static void registerItemRenders(Item item, String name) {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(item, 0,
				new ModelResourceLocation(Rediscovered.modid + ":" + name, "inventory"));
	}

	// Entity Renders
	public static void registerRediscoveredMob(Class<? extends Entity> var0, String var1, int id) {
		if (id == -1) {
			EntityRegistry.registerModEntity(var0, var1, nextInternalID(), Rediscovered.instance, 80, 3, true);
		} else {
			EntityRegistry.registerModEntity(var0, var1, id, Rediscovered.instance, 80, 3, true);
		}
	}

	public static void registerRediscoveredMob(Class<? extends Entity> var0, String var1, int back, int fore, int id) {
		if (id == -1) {
			EntityRegistry.registerModEntity(var0, var1, nextInternalID(), Rediscovered.instance, 80, 3, true, back,
					fore);
		} else {
			EntityRegistry.registerModEntity(var0, var1, id, Rediscovered.instance, 80, 3, true, back, fore);
		}
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
		if (e.getSide() == Side.CLIENT) {
			// registerBlockRenders(CryingObsidian, "CryingObsidian");
			// registerBlockRenders(Sponge, "Sponge");
			registerBlockRenders(Spikes, "Spikes");
			registerBlockRenders(DragonEggRed, "DragonEggRed");
			registerBlockRenders(RubyOre, "RubyOre");
			registerBlockRenders(RubyBlock, "RubyBlock");
			registerBlockRenders(Lantern, "Lantern");
			registerBlockRenders(LanternPhys, "LanternPhys");
			registerBlockRenders(LockedChest, "LockedChest");
			registerBlockRenders(Chair, "Chair");
			registerBlockRenders(Table, "Table");
			registerBlockRenders(Lectern, "Lectern");
			registerBlockRenders(Lectern, "LecternOpen");
			registerBlockRenders(CherrySlab, "CherryHalfSlab");
			registerBlockRenders(CherryDoubleSlab, "CherryDoubleSlab");
			registerBlockRenders(DirtSlab, "DirtHalfSlab");
			registerBlockRenders(DirtDoubleSlab, "DirtDoubleSlab");
			registerBlockRenders(Rose, "Rose");
			registerBlockRenders(EmptyRoseBush, "EmptyRoseBush");
			registerBlockRenders(EmptyRoseBushTop, "EmptyRoseBushTop");
			registerBlockRenders(Peony, "Peony");
			registerBlockRenders(EmptyPeonyBush, "EmptyPeonyBush");
			registerBlockRenders(EmptyPeonyBushTop, "EmptyPeonyBushTop");
			registerBlockRenders(CherryLog, "CherryLog");
			registerBlockRenders(CherryLeaves, "CherryLeaves");
			registerBlockRenders(CherryPlank, "CherryPlank");
			registerBlockRenders(CherrySapling, "CherrySapling");
			registerBlockRenders(CherryStairs, "CherryStairs");
			// registerBlockRenders(Gear, "GearWall");

			if (EnableQuivers) {
				registerItemRenders(Quiver, "Quiver");
				registerItemRenders(LeatherQuiver, "LeatherQuiver");
				registerItemRenders(ChainQuiver, "ChainQuiver");
				registerItemRenders(GoldQuiver, "GoldQuiver");
				registerItemRenders(IronQuiver, "IronQuiver");
				registerItemRenders(DiamondQuiver, "DiamondQuiver");
				registerItemRenders(LeatherChainQuiver, "LeatherChainQuiver");
			}
			registerItemRenders(LeatherChainHelmet, "LeatherChainHelmet");
			registerItemRenders(LeatherChainChest, "LeatherChainChest");
			registerItemRenders(LeatherChainLegs, "LeatherChainLegs");
			registerItemRenders(LeatherChainBoots, "LeatherChainBoots");
			registerItemRenders(gemRuby, "gemRuby");
			registerItemRenders(RediscoveredPotion, 0, "Nausea");
			registerItemRenders(RediscoveredPotion, 1, "Blindness");
			registerItemRenders(RediscoveredPotion, 2, "Dullness");
			registerItemRenders(RediscoveredPotion, 100, "NauseaSplash");
			registerItemRenders(RediscoveredPotion, 101, "BlindnessSplash");
			registerItemRenders(RediscoveredPotion, 102, "DullnessSplash");
			registerItemRenders(ItemLantern, "ItemLantern");
			registerItemRenders(Scarecrow, "Scarecrow");
			// registerItemRenders(ItemGear, "ItemGear");
		}

		registerRecipes();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		registerConfig(e);
		Spikes = (new BlockSpikes()).setHardness(1.5F).setResistance(5.0F).setStepSound(Block.soundTypeWood);
		DragonEggRed = (new BlockDragonEggRed()).setHardness(3.0F).setResistance(15.0F)
				.setStepSound(Block.soundTypeStone).setLightLevel(0.125F).setCreativeTab(CreativeTabs.tabMisc);
		RubyOre = (new BlockRubyOre("rubyore")).setHardness(3F).setResistance(5F).setStepSound(Block.soundTypeStone)
				.setCreativeTab(CreativeTabs.tabBlock);
		RubyBlock = (new BlockRuby("rubyblock")).setHardness(5F).setResistance(10F).setStepSound(Block.soundTypeStone)
				.setCreativeTab(CreativeTabs.tabBlock);
		Lantern = new BlockLantern();
		LanternPhys = new BlockLanternPhys("lantern");
		LockedChest = (new BlockLockedChest("lockedchest")).setHardness(0.0F).setLightLevel(1.0F)
				.setStepSound(Block.soundTypeWood).setTickRandomly(true).setCreativeTab(CreativeTabs.tabDecorations);
		Chair = (BlockChair) (new BlockChair()).setHardness(2.5F).setStepSound(Block.soundTypeWood);
		Table = (BlockTable) (new BlockTable(0)).setHardness(2.5F).setStepSound(Block.soundTypeWood);
		Lectern = (new BlockLectern()).setHardness(2.5F).setStepSound(Block.soundTypeWood);
		LecternOpen = (new BlockLecternOpen()).setHardness(2.5F).setStepSound(Block.soundTypeWood);
		CherrySlab = (BlockCherryHalfSlab) new BlockCherryHalfSlab().setHardness(2.0F).setResistance(5.0F)
				.setStepSound(Block.soundTypeWood);
		;
		CherryDoubleSlab = (BlockCherryDoubleSlab) new BlockCherryDoubleSlab().setHardness(2.0F).setResistance(5.0F)
				.setStepSound(Block.soundTypeWood);
		;
		DirtSlab = (BlockDirtHalfSlab) new BlockDirtHalfSlab().setHardness(0.5F).setStepSound(Block.soundTypeGravel);
		DirtDoubleSlab = (BlockDirtDoubleSlab) new BlockDirtDoubleSlab().setHardness(0.5F)
				.setStepSound(Block.soundTypeGravel);
		Rose = (new BlockRose(0)).setHardness(0.0F).setStepSound(Block.soundTypeGrass);
		EmptyRoseBush = (new BlockEmptyRoseBush(0)).setHardness(0.0F).setStepSound(Block.soundTypeGrass);
		EmptyRoseBushTop = (new BlockEmptyRoseBushTop()).setHardness(0.0F).setStepSound(Block.soundTypeGrass);
		Peony = (new BlockPeony(0)).setHardness(0.0F).setStepSound(Block.soundTypeGrass);
		EmptyPeonyBush = (new BlockEmptyPeonyBush(0)).setHardness(0.0F).setStepSound(Block.soundTypeGrass);
		EmptyPeonyBushTop = (new BlockEmptyPeonyBushTop()).setHardness(0.0F).setStepSound(Block.soundTypeGrass);
		CherryLog = (BlockCherryLog) (new BlockCherryLog("log_cherry")).setHardness(2.0F)
				.setStepSound(Block.soundTypeWood);
		CherryLeaves = (new BlockCherryLeaves()).setHardness(0.2F).setLightOpacity(1)
				.setStepSound(Block.soundTypeGrass);
		CherryPlank = (new BlockCherryWood("planks_cherry")).setHardness(2.0F).setResistance(5.0F)
				.setStepSound(Block.soundTypeWood);
		CherrySapling = (new BlockCherrySapling()).setHardness(0.0F).setStepSound(Block.soundTypeGrass);
		CherryStairs = (new BlockCherryStairs(CherryPlank.getDefaultState()));
		// Gear = (new
		// BlockGear()).setHardness(1.0F).setStepSound(Block.soundTypeMetal);

		// Items
		if (EnableQuivers) {
			Quiver = (new ItemQuiver(EnumArmorMaterialInvinc, 0, 1, "Quiver")).setCreativeTab(CreativeTabs.tabCombat);
			LeatherQuiver = (new ItemQuiver(ArmorMaterial.LEATHER, 0, 1, "LeatherQuiver")).setContainerItem(Quiver)
					.setCreativeTab(CreativeTabs.tabCombat);
			ChainQuiver = (new ItemQuiver(ArmorMaterial.CHAIN, 0, 1, "ChainQuiver")).setContainerItem(Quiver)
					.setCreativeTab(CreativeTabs.tabCombat);
			GoldQuiver = (new ItemQuiver(ArmorMaterial.GOLD, 0, 1, "GoldQuiver")).setContainerItem(Quiver)
					.setCreativeTab(CreativeTabs.tabCombat);
			IronQuiver = (new ItemQuiver(ArmorMaterial.IRON, 0, 1, "IronQuiver")).setContainerItem(Quiver)
					.setCreativeTab(CreativeTabs.tabCombat);
			DiamondQuiver = (new ItemQuiver(ArmorMaterial.DIAMOND, 0, 1, "DiamondQuiver")).setContainerItem(Quiver)
					.setCreativeTab(CreativeTabs.tabCombat);
			LeatherChainQuiver = (new ItemQuiver(EnumArmorMaterialLC, 0, 1, "LeatherChainQuiver"))
					.setContainerItem(Quiver).setCreativeTab(CreativeTabs.tabCombat);
		}
		LeatherChainHelmet = (new ItemLeatherChain(EnumArmorMaterialLC, 0, 0)).setCreativeTab(CreativeTabs.tabCombat);
		LeatherChainChest = (new ItemLeatherChain(EnumArmorMaterialLC, 0, 1)).setCreativeTab(CreativeTabs.tabCombat);
		LeatherChainLegs = (new ItemLeatherChain(EnumArmorMaterialLC, 0, 2)).setCreativeTab(CreativeTabs.tabCombat);
		LeatherChainBoots = (new ItemLeatherChain(EnumArmorMaterialLC, 0, 3)).setCreativeTab(CreativeTabs.tabCombat);
		gemRuby = (new ItemRuby()).setCreativeTab(CreativeTabs.tabMaterials);
		RediscoveredPotion = (new ItemPotionRediscovered());
		ItemLantern = new ItemLantern();
		Scarecrow = (new ItemScarecrow()).setCreativeTab(CreativeTabs.tabDecorations);
		// ItemGear = (new ItemGear()).setCreativeTab(CreativeTabs.tabRedstone);

		heaven = (new BiomeGenSky(HeavenBiomeID)).setColor(16421912).setBiomeName("Heaven").setDisableRain();

		instance = this;
		MinecraftForge.EVENT_BUS.register(new com.stormister.rediscovered.RediscoveredEventHandler());

		EntitySpawnPlacementRegistry.setPlacementType(EntityFish.class, EntityLiving.SpawnPlacementType.IN_WATER);
		EntityRegistry.addSpawn(EntityFish.class, FishSpawn, 5, 20, EnumCreatureType.WATER_CREATURE, BiomeGenBase.beach,
				BiomeGenBase.extremeHills, BiomeGenBase.extremeHillsEdge, BiomeGenBase.forest, BiomeGenBase.forestHills,
				BiomeGenBase.jungle, BiomeGenBase.jungleHills, BiomeGenBase.mushroomIsland,
				BiomeGenBase.mushroomIslandShore, BiomeGenBase.ocean, BiomeGenBase.plains, BiomeGenBase.river,
				BiomeGenBase.swampland, heaven);
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

		registerGameRegistryThings();

		proxy.registerRenderThings();
	}

	// Config
	public void registerConfig(FMLPreInitializationEvent e) {
		config = new Configuration(e.getSuggestedConfigurationFile());
		config.load();

		// IDs
		PurpleArrowID = config.get("ID's", "Purple Arrow ID (-1 means it will automatically assign an ID)", -1)
				.getInt();
		PotionID = config.get("ID's", "Thrown Potion ID (-1 means it will automatically assign an ID)", -1).getInt();
		MountableBlockID = config.get("ID's", "Mountable Block ID (-1 means it will automatically assign an ID)", -1)
				.getInt();
		PigmanID = config.get("ID's", "Pigman ID (-1 means it will automatically assign an ID)", -1).getInt();
		PigmanMobID = config.get("ID's", "Pigman Mob ID (-1 means it will automatically assign an ID)", -1).getInt();
		GreenVillagerID = config.get("ID's", "Green Villager ID (-1 means it will automatically assign an ID)", -1)
				.getInt();
		SkyChickenID = config.get("ID's", "Sky Chicken ID (-1 means it will automatically assign an ID)", -1).getInt();
		GiantID = config.get("ID's", "Giant ID (-1 means it will automatically assign an ID)", -1).getInt();
		FishID = config.get("ID's", "Fish ID (-1 means it will automatically assign an ID)", -1).getInt();
		ZombieHorseID = config.get("ID's", "Zombie Horse ID (-1 means it will automatically assign an ID)", -1)
				.getInt();
		SkeletonHorseID = config.get("ID's", "Skeleton Horse ID (-1 means it will automatically assign an ID)", -1)
				.getInt();
		ScarecrowID = config.get("ID's", "Scarecrow ID (-1 means it will automatically assign an ID)", -1).getInt();
		RedDragonID = config.get("ID's", "Red Dragon ID (-1 means it will automatically assign an ID)", -1).getInt();
		HeavenBiomeID = config.get("ID's", "Sky Biome ID", 153).getInt();

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

	// GameRegistry
	public void registerGameRegistryThings() {
		GameRegistry.registerBlock(CherrySlab, ItemBlockCherrySlab.class, CherrySlab.getName(), CherrySlab,
				CherryDoubleSlab, false);
		GameRegistry.registerBlock(CherryDoubleSlab, ItemBlockCherrySlab.class, CherryDoubleSlab.getName(), CherrySlab,
				CherryDoubleSlab, true);
		GameRegistry.registerBlock(DirtSlab, ItemBlockDirtSlab.class, DirtSlab.getName(), DirtSlab, DirtDoubleSlab,
				false);
		GameRegistry.registerBlock(DirtDoubleSlab, ItemBlockDirtSlab.class, DirtDoubleSlab.getName(), DirtSlab,
				DirtDoubleSlab, true);
		OreDictionary.registerOre("woodLog", CherryLog);
		OreDictionary.registerOre("logWood", CherryLog);
		OreDictionary.registerOre("plankWood", CherryPlank);
		OreDictionary.registerOre("woodSlab", CherrySlab);
		OreDictionary.registerOre("slabWood", CherrySlab);
		OreDictionary.registerOre("woodSlab", CherryDoubleSlab);
		OreDictionary.registerOre("slabWood", CherryDoubleSlab);
		OreDictionary.registerOre("leavesTree", CherryLeaves);
		OreDictionary.registerOre("saplingTree", CherrySapling);
		OreDictionary.registerOre("stairWood", CherryStairs);
		OreDictionary.registerOre("woodStair", CherryStairs);
		OreDictionary.registerOre("oreRuby", RubyOre);
		OreDictionary.registerOre("gemRuby", gemRuby);

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new RediscoveredGuiHandler());

		registerRediscoveredMob(EntityParrow.class, "ParrowRediscovered", PurpleArrowID);
		registerRediscoveredMob(EntityRediscoveredPotion.class, "PotionRediscovered", PotionID);
		registerRediscoveredMob(EntityMountableBlock.class, "EntityMountableBlockRediscovered", MountableBlockID);
		registerRediscoveredMob(EntityPigmanVillager.class, "PigmanRediscovered", 0xf0a5a2, 0xa1a1a1, PigmanID);
		registerRediscoveredMob(EntityPigmanMob.class, "PigmanMobRediscovered", 0xf0a5a2, 0xa1a1a1, PigmanMobID);
		registerRediscoveredMob(EntityGreenVillager.class, "GreenVillagerRediscovered", 5651507, 7969893,
				GreenVillagerID);
		registerRediscoveredMob(EntitySkyChicken.class, "SkyChickenRediscovered", SkyChickenID);
		registerRediscoveredMob(EntityGiant.class, "GiantZombieRediscovered", 2243405, 7969893, GiantID);
		registerRediscoveredMob(EntityFish.class, "FishRediscovered", 44975, 2243405, FishID);
		registerRediscoveredMob(EntityZombieHorse.class, "ZombieHorseRediscovered", 0x4c7129, 15656192, ZombieHorseID);
		registerRediscoveredMob(EntitySkeletonHorse.class, "SkeletonHorseRediscovered", 12698049, 15656192,
				SkeletonHorseID);
		registerRediscoveredMob(EntityScarecrow.class, "ScarecrowRediscovered", ScarecrowID);
		registerRediscoveredMob(EntityGoodDragon.class, "RedDragonRediscovered", RedDragonID);

		DimensionManager.registerProviderType(2, WorldProviderHeaven.class, true);
		DimensionManager.registerProviderType(3, WorldProviderVoid.class, true);
		BiomeManager.removeSpawnBiome(heaven);
		BiomeManager.removeStrongholdBiome(heaven);
		BiomeManager.removeVillageBiome(heaven);
		WorldChunkManager.allowedBiomes.remove(heaven);

		if (EnableRubyOre) {
			GameRegistry.registerWorldGenerator(new WorldGeneratorRuby(), 0);
		}
		if (EnablePigmanVillages) {
			GameRegistry.registerWorldGenerator(new WorldGeneratorPigmanVillage(), 0);
		}
		if (EnableDungeonLoot) {
			ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST)
					.addItem(new WeightedRandomChestContent(new ItemStack(ItemLantern), 1, 1, 15));
			ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR)
					.addItem(new WeightedRandomChestContent(new ItemStack(ItemLantern), 1, 1, 15));
		}
		GameRegistry.registerTileEntity(TileEntityTable.class, "tileentitytable");
		GameRegistry.registerTileEntity(TileEntityLectern.class, "tileentitylectern");
		GameRegistry.registerTileEntity(TileEntityLecternOpen.class, "tileentitylecternopen");
		GameRegistry.registerTileEntity(TileEntityLockedChest.class, "tileentitylockedchest");
	}

	// Recipes
	public void registerRecipes() {
		if (EnableQuivers) {
			GameRegistry.addShapedRecipe(new ItemStack(Quiver), "FLL", "SLL", "SLL", 'F', Items.feather, 'S',
					Items.string, 'L', Items.leather);
			GameRegistry.addShapelessRecipe(new ItemStack(LeatherQuiver, 1),
					new Object[] { Quiver, Items.leather_chestplate });
			GameRegistry.addShapedRecipe(new ItemStack(Items.leather_chestplate), "Q", 'Q', LeatherQuiver);
			GameRegistry.addShapelessRecipe(new ItemStack(ChainQuiver, 1),
					new Object[] { Quiver, Items.chainmail_chestplate });
			GameRegistry.addShapedRecipe(new ItemStack(Items.chainmail_chestplate), "Q", 'Q', ChainQuiver);
			GameRegistry.addShapelessRecipe(new ItemStack(GoldQuiver, 1),
					new Object[] { Quiver, Items.golden_chestplate });
			GameRegistry.addShapedRecipe(new ItemStack(Items.golden_chestplate), "Q", 'Q', GoldQuiver);
			GameRegistry.addShapelessRecipe(new ItemStack(IronQuiver, 1),
					new Object[] { Quiver, Items.iron_chestplate });
			GameRegistry.addShapedRecipe(new ItemStack(Items.iron_chestplate), "Q", 'Q', IronQuiver);
			GameRegistry.addShapelessRecipe(new ItemStack(DiamondQuiver, 1),
					new Object[] { Quiver, Items.diamond_chestplate });
			GameRegistry.addShapedRecipe(new ItemStack(Items.diamond_chestplate), "Q", 'Q', DiamondQuiver);
			GameRegistry.addShapelessRecipe(new ItemStack(LeatherChainQuiver, 1),
					new Object[] { Quiver, LeatherChainChest });
			GameRegistry.addShapedRecipe(new ItemStack(LeatherChainChest), "Q", 'Q', LeatherChainQuiver);
		}
		GameRegistry.addShapedRecipe(new ItemStack(LeatherChainHelmet), "L", "C", 'L', Items.leather_helmet, 'C',
				Items.chainmail_helmet);
		GameRegistry.addShapedRecipe(new ItemStack(LeatherChainChest), "L", "C", 'L', Items.leather_chestplate, 'C',
				Items.chainmail_chestplate);
		GameRegistry.addShapedRecipe(new ItemStack(LeatherChainLegs), "L", "C", 'L', Items.leather_leggings, 'C',
				Items.chainmail_leggings);
		GameRegistry.addShapedRecipe(new ItemStack(LeatherChainBoots), "L", "C", 'L', Items.leather_boots, 'C',
				Items.chainmail_boots);
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 1), new Object[] { new ItemStack(Rose) });
		GameRegistry.addShapelessRecipe(new ItemStack(Items.dye, 2, 9), new Object[] { new ItemStack(Peony) });
		GameRegistry.addShapelessRecipe(new ItemStack(CherryPlank, 4), new Object[] { CherryLog });
		GameRegistry.addRecipe(new ItemStack(CherrySlab, 6), new Object[] { "SSS", 'S', CherryPlank });
		GameRegistry.addRecipe(new ItemStack(CherryStairs, 4), new Object[] { "  S", " SS", "SSS", 'S', CherryPlank });
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Spikes, 1), true,
				new Object[] { "   ", "I I", "LLL", 'L', "plankWood", 'I', Items.iron_ingot }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Scarecrow, 1), true,
				new Object[] { " P ", "SHS", " S ", 'S', Items.stick, 'H', Blocks.hay_block, 'P', Blocks.pumpkin }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(RubyBlock, 1), true,
				new Object[] { "LLL", "LLL", "LLL", 'L', "gemRuby" }));
		GameRegistry.addSmelting(RubyOre, new ItemStack(gemRuby), 1);
		GameRegistry
				.addRecipe(new ShapedOreRecipe(new ItemStack(gemRuby, 9), true, new Object[] { "L", 'L', RubyBlock }));
		GameRegistry.addShapelessRecipe(new ItemStack(RediscoveredPotion, 1, 0),
				new Object[] { Items.rotten_flesh, new ItemStack(Items.potionitem, 1, 16) });
		GameRegistry.addShapelessRecipe(new ItemStack(RediscoveredPotion, 1, 100),
				new Object[] { new ItemStack(RediscoveredPotion, 1, 0), Items.gunpowder });
		GameRegistry.addShapelessRecipe(new ItemStack(RediscoveredPotion, 1, 1),
				new Object[] { Items.poisonous_potato, new ItemStack(Items.potionitem, 1, 16) });
		GameRegistry.addShapelessRecipe(new ItemStack(RediscoveredPotion, 1, 101),
				new Object[] { new ItemStack(RediscoveredPotion, 1, 1), Items.gunpowder });
		GameRegistry.addShapelessRecipe(new ItemStack(RediscoveredPotion, 1, 2),
				new Object[] { Items.apple, new ItemStack(Items.potionitem, 1, 16) });
		GameRegistry.addShapelessRecipe(new ItemStack(RediscoveredPotion, 1, 102),
				new Object[] { new ItemStack(RediscoveredPotion, 1, 2), Items.gunpowder });
		GameRegistry.addRecipe(new ItemStack(DirtSlab, 6), new Object[] { "DDD", 'D', Blocks.dirt });
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(LockedChest, 1),
				new Object[] { "WWW", "WIW", "WWW", 'I', Items.iron_ingot, 'W', "plankWood" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Chair, 1), true,
				new Object[] { "L  ", "LLL", "L L", 'L', "plankWood" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Table, 1), true,
				new Object[] { "   ", "LLL", "L L", 'L', "plankWood" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(Lectern, 1), true,
				new Object[] { " B ", " L ", "LGL", 'L', "plankWood", 'G', Items.gold_ingot, 'B', Items.book }));
		// GameRegistry.addRecipe(new ItemStack(ItemGear, 8), new Object[] { " I
		// ", "III", " I ", 'I', Items.iron_ingot });
		GameRegistry.addRecipe(new ItemStack(ItemLantern, 1),
				new Object[] { " I ", " S ", " I ", 'I', Items.iron_ingot, 'S', Items.glowstone_dust });
	}

}
