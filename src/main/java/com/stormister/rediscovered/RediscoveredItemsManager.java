package com.stormister.rediscovered;

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
import com.stormister.rediscovered.items.ItemBlockCherrySlab;
import com.stormister.rediscovered.items.ItemBlockDirtSlab;
import com.stormister.rediscovered.items.ItemLantern;
import com.stormister.rediscovered.items.ItemLeatherChain;
import com.stormister.rediscovered.items.ItemPotionRediscovered;
import com.stormister.rediscovered.items.ItemQuiver;
import com.stormister.rediscovered.items.ItemRuby;
import com.stormister.rediscovered.items.ItemScarecrow;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RediscoveredItemsManager {
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

	public static ArmorMaterial EnumArmorMaterialInvinc = EnumHelper.addArmorMaterial("Invincible", "Quiver", -1,
			new int[] { 0, 0, 0, 0 }, 0);

	public static ArmorMaterial EnumArmorMaterialLC = EnumHelper.addArmorMaterial("LC", "leatherchain", 20,
			new int[] { 3, 8, 6, 2 }, 27);

	public static void init() {
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
		CherryDoubleSlab = (BlockCherryDoubleSlab) new BlockCherryDoubleSlab().setHardness(2.0F).setResistance(5.0F)
				.setStepSound(Block.soundTypeWood);
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
		if (Rediscovered.EnableQuivers) {
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
	}

	// GameRegistry
	public static void registerGameRegistryThings() {
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

		NetworkRegistry.INSTANCE.registerGuiHandler(Rediscovered.instance, new RediscoveredGuiHandler());

		if (Rediscovered.EnableDungeonLoot) {
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
	public static void registerRecipes() {
		if (Rediscovered.EnableQuivers) {
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
