package com.stormister.rediscovered.entity;

import java.util.Iterator;
import java.util.Random;

import com.stormister.rediscovered.PigmenRegistry;
import com.stormister.rediscovered.RediscoveredItemsManager;
import com.stormister.rediscovered.entity.ai.EntityAILookAtTradePigman;
import com.stormister.rediscovered.entity.ai.EntityAIPigmanMate;
import com.stormister.rediscovered.entity.ai.EntityAITradePigman;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
//import net.minecraft.entity.ai.EntityAIPlay;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
//import net.minecraft.entity.ai.EntityAIVillagerMate;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityPigmanVillager extends EntityAgeable implements INpc, IMerchant {
	public static class EmeraldForItems implements EntityPigmanVillager.ITradeList {
		public Item field_179405_a;
		public EntityPigmanVillager.PriceInfo field_179404_b;

		public EmeraldForItems(Item p_i45815_1_, EntityPigmanVillager.PriceInfo p_i45815_2_) {
			field_179405_a = p_i45815_1_;
			field_179404_b = p_i45815_2_;
		}

		/**
		 * Affects the given MerchantRecipeList to possibly add or remove
		 * MerchantRecipes.
		 */
		@Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
			int i = 1;

			if (field_179404_b != null) {
				i = field_179404_b.func_179412_a(random);
			}

			recipeList.add(new MerchantRecipe(new ItemStack(field_179405_a, i, 0), RediscoveredItemsManager.gemRuby));
		}
	}

	public static class ItemAndEmeraldToItem implements EntityPigmanVillager.ITradeList {
		public ItemStack field_179411_a;
		public EntityPigmanVillager.PriceInfo field_179409_b;
		public ItemStack field_179410_c;
		public EntityPigmanVillager.PriceInfo field_179408_d;

		public ItemAndEmeraldToItem(Item p_i45813_1_, EntityPigmanVillager.PriceInfo p_i45813_2_, Item p_i45813_3_,
				EntityPigmanVillager.PriceInfo p_i45813_4_) {
			field_179411_a = new ItemStack(p_i45813_1_);
			field_179409_b = p_i45813_2_;
			field_179410_c = new ItemStack(p_i45813_3_);
			field_179408_d = p_i45813_4_;
		}

		/**
		 * Affects the given MerchantRecipeList to possibly add or remove
		 * MerchantRecipes.
		 */
		@Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
			int i = 1;

			if (field_179409_b != null) {
				i = field_179409_b.func_179412_a(random);
			}

			int j = 1;

			if (field_179408_d != null) {
				j = field_179408_d.func_179412_a(random);
			}

			recipeList.add(new MerchantRecipe(new ItemStack(field_179411_a.getItem(), i, field_179411_a.getMetadata()),
					new ItemStack(RediscoveredItemsManager.gemRuby),
					new ItemStack(field_179410_c.getItem(), j, field_179410_c.getMetadata())));
		}
	}

	public interface ITradeList {
		/**
		 * Affects the given MerchantRecipeList to possibly add or remove
		 * MerchantRecipes.
		 */
		void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random);
	}

	public static class ListEnchantedBookForEmeralds implements EntityPigmanVillager.ITradeList {

		/**
		 * Affects the given MerchantRecipeList to possibly add or remove
		 * MerchantRecipes.
		 */
		@Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
			Enchantment enchantment = Enchantment.enchantmentsBookList[random
					.nextInt(Enchantment.enchantmentsBookList.length)];
			int i = MathHelper.getRandomIntegerInRange(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
			ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, i));
			int j = 2 + random.nextInt(5 + (i * 10)) + (3 * i);

			if (j > 64) {
				j = 64;
			}

			recipeList.add(new MerchantRecipe(new ItemStack(Items.book),
					new ItemStack(RediscoveredItemsManager.gemRuby, j), itemstack));
		}
	}

	public static class ListEnchantedItemForEmeralds implements EntityPigmanVillager.ITradeList {
		public ItemStack field_179407_a;
		public EntityPigmanVillager.PriceInfo field_179406_b;

		public ListEnchantedItemForEmeralds(Item p_i45814_1_, EntityPigmanVillager.PriceInfo p_i45814_2_) {
			field_179407_a = new ItemStack(p_i45814_1_);
			field_179406_b = p_i45814_2_;
		}

		/**
		 * Affects the given MerchantRecipeList to possibly add or remove
		 * MerchantRecipes.
		 */
		@Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
			int i = 1;

			if (field_179406_b != null) {
				i = field_179406_b.func_179412_a(random);
			}

			ItemStack itemstack = new ItemStack(RediscoveredItemsManager.gemRuby, i, 0);
			ItemStack itemstack1 = new ItemStack(field_179407_a.getItem(), 1, field_179407_a.getMetadata());
			itemstack1 = EnchantmentHelper.addRandomEnchantment(random, itemstack1, 5 + random.nextInt(15));
			recipeList.add(new MerchantRecipe(itemstack, itemstack1));
		}
	}

	public static class ListItemForEmeralds implements EntityPigmanVillager.ITradeList {
		public ItemStack field_179403_a;
		public EntityPigmanVillager.PriceInfo field_179402_b;

		public ListItemForEmeralds(Item p_i45811_1_, EntityPigmanVillager.PriceInfo p_i45811_2_) {
			field_179403_a = new ItemStack(p_i45811_1_);
			field_179402_b = p_i45811_2_;
		}

		public ListItemForEmeralds(ItemStack p_i45812_1_, EntityPigmanVillager.PriceInfo p_i45812_2_) {
			field_179403_a = p_i45812_1_;
			field_179402_b = p_i45812_2_;
		}

		/**
		 * Affects the given MerchantRecipeList to possibly add or remove
		 * MerchantRecipes.
		 */
		@Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
			int i = 1;

			if (field_179402_b != null) {
				i = field_179402_b.func_179412_a(random);
			}

			ItemStack itemstack;
			ItemStack itemstack1;

			if (i < 0) {
				itemstack = new ItemStack(RediscoveredItemsManager.gemRuby, 1, 0);
				itemstack1 = new ItemStack(field_179403_a.getItem(), -i, field_179403_a.getMetadata());
			} else {
				itemstack = new ItemStack(RediscoveredItemsManager.gemRuby, i, 0);
				itemstack1 = new ItemStack(field_179403_a.getItem(), 1, field_179403_a.getMetadata());
			}

			recipeList.add(new MerchantRecipe(itemstack, itemstack1));
		}
	}

	public static class PriceInfo extends Tuple {

		public PriceInfo(int p_i45810_1_, int p_i45810_2_) {
			super(Integer.valueOf(p_i45810_1_), Integer.valueOf(p_i45810_2_));
		}

		public int func_179412_a(Random p_179412_1_) {
			return ((Integer) getFirst()).intValue() >= ((Integer) getSecond()).intValue()
					? ((Integer) getFirst()).intValue()
					: ((Integer) getFirst()).intValue() + p_179412_1_
							.nextInt((((Integer) getSecond()).intValue() - ((Integer) getFirst()).intValue()) + 1);
		}
	}

	/**
	 * A multi-dimensional array mapping the various professions, careers and
	 * career levels that a Villager may offer
	 */
	@Deprecated // Use PigmanRegistry
	private static final EntityPigmanVillager.ITradeList[][][][] DEFAULT_TRADE_LIST_MAP = new EntityPigmanVillager.ITradeList[][][][] {
			{ { { new EntityPigmanVillager.EmeraldForItems(Items.wheat, new EntityPigmanVillager.PriceInfo(18, 22)),
					new EntityPigmanVillager.EmeraldForItems(Items.potato, new EntityPigmanVillager.PriceInfo(15, 19)),
					new EntityPigmanVillager.EmeraldForItems(Items.carrot, new EntityPigmanVillager.PriceInfo(15, 19)),
					new EntityPigmanVillager.ListItemForEmeralds(Items.bread,
							new EntityPigmanVillager.PriceInfo(-4, -2)) },
					{ new EntityPigmanVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.pumpkin),
							new EntityPigmanVillager.PriceInfo(8, 13)),
							new EntityPigmanVillager.ListItemForEmeralds(
									Items.pumpkin_pie, new EntityPigmanVillager.PriceInfo(-3, -2)) },
					{ new EntityPigmanVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.melon_block),
							new EntityPigmanVillager.PriceInfo(7, 12)),
							new EntityPigmanVillager.ListItemForEmeralds(
									Items.apple, new EntityPigmanVillager.PriceInfo(-5, -7)) },
					{ new EntityPigmanVillager.ListItemForEmeralds(Items.cookie,
							new EntityPigmanVillager.PriceInfo(-6, -10)),
							new EntityPigmanVillager.ListItemForEmeralds(
									Items.cake, new EntityPigmanVillager.PriceInfo(1, 1)) } },
					{ { new EntityPigmanVillager.EmeraldForItems(Items.string,
							new EntityPigmanVillager.PriceInfo(15, 20)),
							new EntityPigmanVillager.EmeraldForItems(
									Items.coal, new EntityPigmanVillager.PriceInfo(16, 24)),
							new EntityPigmanVillager.ItemAndEmeraldToItem(Items.fish,
									new EntityPigmanVillager.PriceInfo(6, 6), Items.cooked_fish,
									new EntityPigmanVillager.PriceInfo(6, 6)) },
							{ new EntityPigmanVillager.ListEnchantedItemForEmeralds(Items.fishing_rod,
									new EntityPigmanVillager.PriceInfo(7, 8)) } },
					{ { new EntityPigmanVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.wool),
							new EntityPigmanVillager.PriceInfo(16, 22)),
							new EntityPigmanVillager.ListItemForEmeralds(
									Items.shears, new EntityPigmanVillager.PriceInfo(3, 4)) },
							{ new EntityPigmanVillager.ListItemForEmeralds(
									new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 0),
									new EntityPigmanVillager.PriceInfo(1, 2)),
									new EntityPigmanVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 1),
											new EntityPigmanVillager.PriceInfo(1, 2)),
									new EntityPigmanVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 2),
											new EntityPigmanVillager.PriceInfo(1, 2)),
									new EntityPigmanVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 3),
											new EntityPigmanVillager.PriceInfo(1, 2)),
									new EntityPigmanVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 4),
											new EntityPigmanVillager.PriceInfo(1, 2)),
									new EntityPigmanVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 5),
											new EntityPigmanVillager.PriceInfo(1, 2)),
									new EntityPigmanVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 6),
											new EntityPigmanVillager.PriceInfo(1, 2)),
									new EntityPigmanVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 7),
											new EntityPigmanVillager.PriceInfo(1, 2)),
									new EntityPigmanVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 8),
											new EntityPigmanVillager.PriceInfo(1, 2)),
									new EntityPigmanVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 9),
											new EntityPigmanVillager.PriceInfo(1, 2)),
									new EntityPigmanVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 10),
											new EntityPigmanVillager.PriceInfo(1, 2)),
									new EntityPigmanVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 11),
											new EntityPigmanVillager.PriceInfo(1, 2)),
									new EntityPigmanVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 12),
											new EntityPigmanVillager.PriceInfo(1, 2)),
									new EntityPigmanVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 13),
											new EntityPigmanVillager.PriceInfo(1, 2)),
									new EntityPigmanVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 14),
											new EntityPigmanVillager.PriceInfo(1, 2)),
									new EntityPigmanVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 15),
											new EntityPigmanVillager.PriceInfo(1, 2)) } },
					{ { new EntityPigmanVillager.EmeraldForItems(Items.string,
							new EntityPigmanVillager.PriceInfo(15, 20)),
							new EntityPigmanVillager.ListItemForEmeralds(Items.arrow,
									new EntityPigmanVillager.PriceInfo(-12, -8)) },
							{ new EntityPigmanVillager.ListItemForEmeralds(Items.bow,
									new EntityPigmanVillager.PriceInfo(2, 3)),
									new EntityPigmanVillager.ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.gravel),
											new EntityPigmanVillager.PriceInfo(10, 10), Items.flint,
											new EntityPigmanVillager.PriceInfo(6, 10)) } } },
			{ { { new EntityPigmanVillager.EmeraldForItems(Items.paper, new EntityPigmanVillager.PriceInfo(24, 36)),
					new EntityPigmanVillager.ListEnchantedBookForEmeralds() },
					{ new EntityPigmanVillager.EmeraldForItems(Items.book, new EntityPigmanVillager.PriceInfo(8, 10)),
							new EntityPigmanVillager.ListItemForEmeralds(Items.compass,
									new EntityPigmanVillager.PriceInfo(10, 12)),
							new EntityPigmanVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.bookshelf),
									new EntityPigmanVillager.PriceInfo(3, 4)) },
					{ new EntityPigmanVillager.EmeraldForItems(Items.written_book,
							new EntityPigmanVillager.PriceInfo(2, 2)),
							new EntityPigmanVillager.ListItemForEmeralds(Items.clock,
									new EntityPigmanVillager.PriceInfo(10, 12)),
							new EntityPigmanVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.glass),
									new EntityPigmanVillager.PriceInfo(-5, -3)) },
					{ new EntityPigmanVillager.ListEnchantedBookForEmeralds() },
					{ new EntityPigmanVillager.ListEnchantedBookForEmeralds() },
					{ new EntityPigmanVillager.ListItemForEmeralds(Items.name_tag,
							new EntityPigmanVillager.PriceInfo(20, 22)) } } },
			{ { { new EntityPigmanVillager.EmeraldForItems(Items.rotten_flesh,
					new EntityPigmanVillager.PriceInfo(36, 40)),
					new EntityPigmanVillager.EmeraldForItems(Items.gold_ingot,
							new EntityPigmanVillager.PriceInfo(8, 10)) },
					{ new EntityPigmanVillager.ListItemForEmeralds(Items.redstone,
							new EntityPigmanVillager.PriceInfo(-4, -1)),
							new EntityPigmanVillager.ListItemForEmeralds(
									new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()),
									new EntityPigmanVillager.PriceInfo(-2, -1)) },
					{ new EntityPigmanVillager.ListItemForEmeralds(Items.ender_eye,
							new EntityPigmanVillager.PriceInfo(7, 11)),
							new EntityPigmanVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.glowstone),
									new EntityPigmanVillager.PriceInfo(-3, -1)) },
					{ new EntityPigmanVillager.ListItemForEmeralds(Items.experience_bottle,
							new EntityPigmanVillager.PriceInfo(3, 11)) } } },
			{ { { new EntityPigmanVillager.EmeraldForItems(Items.coal, new EntityPigmanVillager.PriceInfo(16, 24)),
					new EntityPigmanVillager.ListItemForEmeralds(Items.iron_helmet,
							new EntityPigmanVillager.PriceInfo(4, 6)) },
					{ new EntityPigmanVillager.EmeraldForItems(Items.iron_ingot,
							new EntityPigmanVillager.PriceInfo(7, 9)),
							new EntityPigmanVillager.ListItemForEmeralds(Items.iron_chestplate,
									new EntityPigmanVillager.PriceInfo(10, 14)) },
					{ new EntityPigmanVillager.EmeraldForItems(Items.diamond, new EntityPigmanVillager.PriceInfo(3, 4)),
							new EntityPigmanVillager.ListEnchantedItemForEmeralds(Items.diamond_chestplate,
									new EntityPigmanVillager.PriceInfo(16, 19)) },
					{ new EntityPigmanVillager.ListItemForEmeralds(Items.chainmail_boots,
							new EntityPigmanVillager.PriceInfo(5, 7)),
							new EntityPigmanVillager.ListItemForEmeralds(Items.chainmail_leggings,
									new EntityPigmanVillager.PriceInfo(9, 11)),
							new EntityPigmanVillager.ListItemForEmeralds(Items.chainmail_helmet,
									new EntityPigmanVillager.PriceInfo(5, 7)),
							new EntityPigmanVillager.ListItemForEmeralds(Items.chainmail_chestplate,
									new EntityPigmanVillager.PriceInfo(11, 15)) } },
					{ { new EntityPigmanVillager.EmeraldForItems(Items.coal,
							new EntityPigmanVillager.PriceInfo(16, 24)),
							new EntityPigmanVillager.ListItemForEmeralds(Items.iron_axe,
									new EntityPigmanVillager.PriceInfo(6, 8)) },
							{ new EntityPigmanVillager.EmeraldForItems(Items.iron_ingot,
									new EntityPigmanVillager.PriceInfo(7, 9)),
									new EntityPigmanVillager.ListEnchantedItemForEmeralds(Items.iron_sword,
											new EntityPigmanVillager.PriceInfo(9, 10)) },
							{ new EntityPigmanVillager.EmeraldForItems(Items.diamond,
									new EntityPigmanVillager.PriceInfo(3, 4)),
									new EntityPigmanVillager.ListEnchantedItemForEmeralds(Items.diamond_sword,
											new EntityPigmanVillager.PriceInfo(12, 15)),
									new EntityPigmanVillager.ListEnchantedItemForEmeralds(Items.diamond_axe,
											new EntityPigmanVillager.PriceInfo(9, 12)) } },
					{ { new EntityPigmanVillager.EmeraldForItems(Items.coal,
							new EntityPigmanVillager.PriceInfo(16, 24)),
							new EntityPigmanVillager.ListEnchantedItemForEmeralds(Items.iron_shovel,
									new EntityPigmanVillager.PriceInfo(5, 7)) },
							{ new EntityPigmanVillager.EmeraldForItems(Items.iron_ingot,
									new EntityPigmanVillager.PriceInfo(7, 9)),
									new EntityPigmanVillager.ListEnchantedItemForEmeralds(Items.iron_pickaxe,
											new EntityPigmanVillager.PriceInfo(9, 11)) },
							{ new EntityPigmanVillager.EmeraldForItems(Items.diamond,
									new EntityPigmanVillager.PriceInfo(3, 4)),
									new EntityPigmanVillager.ListEnchantedItemForEmeralds(Items.diamond_pickaxe,
											new EntityPigmanVillager.PriceInfo(12, 15)) } } },
			{ { { new EntityPigmanVillager.EmeraldForItems(Items.porkchop, new EntityPigmanVillager.PriceInfo(14, 18)),
					new EntityPigmanVillager.EmeraldForItems(Items.chicken,
							new EntityPigmanVillager.PriceInfo(14, 18)) },
					{ new EntityPigmanVillager.EmeraldForItems(Items.coal, new EntityPigmanVillager.PriceInfo(16, 24)),
							new EntityPigmanVillager.ListItemForEmeralds(Items.cooked_porkchop,
									new EntityPigmanVillager.PriceInfo(-7, -5)),
							new EntityPigmanVillager.ListItemForEmeralds(Items.cooked_chicken,
									new EntityPigmanVillager.PriceInfo(-8, -6)) } },
					{ { new EntityPigmanVillager.EmeraldForItems(Items.leather,
							new EntityPigmanVillager.PriceInfo(9, 12)),
							new EntityPigmanVillager.ListItemForEmeralds(Items.leather_leggings,
									new EntityPigmanVillager.PriceInfo(2, 4)) },
							{ new EntityPigmanVillager.ListEnchantedItemForEmeralds(Items.leather_chestplate,
									new EntityPigmanVillager.PriceInfo(7, 12)) },
							{ new EntityPigmanVillager.ListItemForEmeralds(Items.saddle,
									new EntityPigmanVillager.PriceInfo(8, 10)) } } } };
	private int randomTickDivider;
	private boolean isMating;
	private boolean isPlaying;
	Village villageObj;
	/** This villager's current customer. */
	private EntityPlayer buyingPlayer;
	/** Initializes the MerchantRecipeList.java */
	private MerchantRecipeList buyingList;
	private int timeUntilReset;
	/** addDefaultEquipmentAndRecipies is called if this is true */
	private boolean needsInitilization;
	private boolean isWillingToMate;

	private int wealth;

	/** Last player to trade with this villager, used for aggressivity. */
	private String lastBuyingPlayer;

	private int careerId;

	/** This is the EntityPigman's career level value */
	private int careerLevel;

	private boolean isLookingForHome;

	private InventoryBasic villagerInventory;

	public EntityPigmanVillager(World worldIn) {
		this(worldIn, 0);
	}

	public EntityPigmanVillager(World worldIn, int professionId) {
		super(worldIn);
		villagerInventory = new InventoryBasic("Items", false, 8);
		setProfession(professionId);
		setSize(0.6F, 1.8F);
		((PathNavigateGround) getNavigator()).setBreakDoors(true);
		((PathNavigateGround) getNavigator()).setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
		tasks.addTask(1, new EntityAITradePigman(this));
		tasks.addTask(1, new EntityAILookAtTradePigman(this));
		tasks.addTask(2, new EntityAIMoveIndoors(this));
		tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
		tasks.addTask(4, new EntityAIOpenDoor(this, true));
		tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
		tasks.addTask(6, new EntityAIPigmanMate(this));
		tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		tasks.addTask(9, new EntityAIWander(this, 0.6D));
		tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		setCanPickUpLoot(true);
	}

	@Override
	public boolean allowLeashing() {
		return false;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public EntityPigmanVillager createChild(EntityAgeable ageable) {
		EntityPigmanVillager entityvillager = new EntityPigmanVillager(worldObj);
		entityvillager.onInitialSpawn(worldObj.getDifficultyForLocation(new BlockPos(entityvillager)),
				(IEntityLivingData) null);
		return entityvillager;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
		int j = rand.nextInt(3) + 1 + rand.nextInt(1 + p_70628_2_);

		for (int k = 0; k < j; ++k) {
			if (isBurning()) {
				dropItem(Items.cooked_porkchop, 1);
			} else {
				dropItem(Items.porkchop, 1);
			}
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Integer.valueOf(0));
	}

	protected void func_175445_a(EntityItem p_175445_1_) {
		ItemStack itemstack = p_175445_1_.getEntityItem();
		Item item = itemstack.getItem();

		if (func_175558_a(item)) {
			ItemStack itemstack1 = villagerInventory.func_174894_a(itemstack);

			if (itemstack1 == null) {
				p_175445_1_.setDead();
			} else {
				itemstack.stackSize = itemstack1.stackSize;
			}
		}
	}

	public InventoryBasic func_175551_co() {
		return villagerInventory;
	}

	public boolean func_175553_cp() {
		return func_175559_s(1);
	}

	public boolean func_175555_cq() {
		return func_175559_s(2);
	}

	public boolean func_175556_cs() {
		for (int i = 0; i < villagerInventory.getSizeInventory(); ++i) {
			ItemStack itemstack = villagerInventory.getStackInSlot(i);

			if ((itemstack != null) && ((itemstack.getItem() == Items.wheat_seeds)
					|| (itemstack.getItem() == Items.potato) || (itemstack.getItem() == Items.carrot))) {
				return true;
			}
		}

		return false;
	}

	public boolean func_175557_cr() {
		boolean flag = getProfession() == 0;
		return flag ? !func_175559_s(5) : !func_175559_s(1);
	}

	private boolean func_175558_a(Item p_175558_1_) {
		return (p_175558_1_ == Items.bread) || (p_175558_1_ == Items.potato) || (p_175558_1_ == Items.carrot)
				|| (p_175558_1_ == Items.wheat) || (p_175558_1_ == Items.wheat_seeds);
	}

	private boolean func_175559_s(int p_175559_1_) {
		boolean flag = getProfession() == 0;

		for (int j = 0; j < villagerInventory.getSizeInventory(); ++j) {
			ItemStack itemstack = villagerInventory.getStackInSlot(j);

			if (itemstack != null) {
				if (((itemstack.getItem() == Items.bread) && (itemstack.stackSize >= (3 * p_175559_1_)))
						|| ((itemstack.getItem() == Items.potato) && (itemstack.stackSize >= (12 * p_175559_1_)))
						|| ((itemstack.getItem() == Items.carrot) && (itemstack.stackSize >= (12 * p_175559_1_)))) {
					return true;
				}

				if (flag && (itemstack.getItem() == Items.wheat) && (itemstack.stackSize >= (9 * p_175559_1_))) {
					return true;
				}
			}
		}

		return false;
	}

	@SideOnly(Side.CLIENT)
	private void func_180489_a(EnumParticleTypes p_180489_1_) {
		for (int i = 0; i < 5; ++i) {
			double d0 = rand.nextGaussian() * 0.02D;
			double d1 = rand.nextGaussian() * 0.02D;
			double d2 = rand.nextGaussian() * 0.02D;
			worldObj.spawnParticle(p_180489_1_, (posX + (rand.nextFloat() * width * 2.0F)) - width,
					posY + 1.0D + (rand.nextFloat() * height), (posZ + (rand.nextFloat() * width * 2.0F)) - width, d0,
					d1, d2, new int[0]);
		}
	}

	@Override
	public EntityPlayer getCustomer() {
		return buyingPlayer;
	}

	@Override
	protected String getDeathSound() {
		return "mob.pig.death";
	}

	@Override
	public IChatComponent getDisplayName() {
		String s = getCustomNameTag();

		if ((s != null) && (s.length() > 0)) {
			return new ChatComponentText(s);
		} else {
			if (buyingList == null) {
				populateBuyingList();
			}

			String s1 = null;

			switch (getProfession()) {
			case 0:
				if (careerId == 1) {
					s1 = "farmer";
				} else if (careerId == 2) {
					s1 = "fisherman";
				} else if (careerId == 3) {
					s1 = "shepherd";
				} else if (careerId == 4) {
					s1 = "fletcher";
				}

				break;
			case 1:
				s1 = "librarian";
				break;
			case 2:
				s1 = "cleric";
				break;
			case 3:
				if (careerId == 1) {
					s1 = "armor";
				} else if (careerId == 2) {
					s1 = "weapon";
				} else if (careerId == 3) {
					s1 = "tool";
				}

				break;
			case 4:
				if (careerId == 1) {
					s1 = "butcher";
				} else if (careerId == 2) {
					s1 = "leather";
				}
			}

			if (s1 != null) {
				ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("Pigman",
						new Object[0]);
				chatcomponenttranslation.getChatStyle().setChatHoverEvent(getHoverEvent());
				chatcomponenttranslation.getChatStyle().setInsertion(getUniqueID().toString());
				return chatcomponenttranslation;
			} else {
				return super.getDisplayName();
			}
		}
	}

	@Override
	protected Item getDropItem() {
		return isBurning() ? Items.cooked_porkchop : Items.porkchop;
	}

	@Override
	public float getEyeHeight() {
		float f = 1.62F;

		if (isChild()) {
			f = (float) (f - 0.81D);
		}

		return f;
	}

	@Override
	protected String getHurtSound() {
		return "mob.pig.say";
	}

	public boolean getIsWillingToMate(boolean updateFirst) {
		if (!isWillingToMate && updateFirst && func_175553_cp()) {
			boolean flag1 = false;

			for (int i = 0; i < villagerInventory.getSizeInventory(); ++i) {
				ItemStack itemstack = villagerInventory.getStackInSlot(i);

				if (itemstack != null) {
					if ((itemstack.getItem() == Items.bread) && (itemstack.stackSize >= 3)) {
						flag1 = true;
						villagerInventory.decrStackSize(i, 3);
					} else if (((itemstack.getItem() == Items.potato) || (itemstack.getItem() == Items.carrot))
							&& (itemstack.stackSize >= 12)) {
						flag1 = true;
						villagerInventory.decrStackSize(i, 12);
					}
				}

				if (flag1) {
					worldObj.setEntityState(this, (byte) 18);
					isWillingToMate = true;
					break;
				}
			}
		}
		return isWillingToMate;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.pig.say";
	}

	public int getProfession() {
		return Math.max(dataWatcher.getWatchableObjectInt(16) % 5, 0);
	}

	@Override
	public MerchantRecipeList getRecipes(EntityPlayer p_70934_1_) {
		if (buyingList == null) {
			populateBuyingList();
		}

		return buyingList;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte p_70103_1_) {
		if (p_70103_1_ == 12) {
			func_180489_a(EnumParticleTypes.HEART);
		} else if (p_70103_1_ == 13) {
			func_180489_a(EnumParticleTypes.VILLAGER_ANGRY);
		} else if (p_70103_1_ == 14) {
			func_180489_a(EnumParticleTypes.VILLAGER_HAPPY);
		} else {
			super.handleStatusUpdate(p_70103_1_);
		}
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer player) {
		ItemStack itemstack = player.inventory.getCurrentItem();
		boolean flag = (itemstack != null) && (itemstack.getItem() == Items.spawn_egg);

		if (!flag && isEntityAlive() && !isTrading() && !isChild() && !player.isSneaking()) {
			if (!worldObj.isRemote && ((buyingList == null) || (buyingList.size() > 0))) {
				setCustomer(player);
				player.displayVillagerTradeGui(this);
			}

			return true;
		} else {
			return super.interact(player);
		}
	}

	public boolean isMating() {
		return isMating;
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public boolean isTrading() {
		return buyingPlayer != null;
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource cause) {
		if (villageObj != null) {
			Entity entity = cause.getEntity();

			if (entity != null) {
				if (entity instanceof EntityPlayer) {
					villageObj.setReputationForPlayer(entity.getDisplayName().getUnformattedText(), -2);
				} else if (entity instanceof IMob) {
					villageObj.endMatingSeason();
				}
			} else {
				EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, 16.0D);

				if (entityplayer != null) {
					villageObj.endMatingSeason();
				}
			}
		}

		super.onDeath(cause);
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		PigmenRegistry.setRandomProfession(this, worldObj.rand);
		return livingdata;
	}

	/**
	 * Called when a lightning bolt hits the entity.
	 */
	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt) {
		if (!worldObj.isRemote) {
			EntityPigZombie entitywitch = new EntityPigZombie(worldObj);
			entitywitch.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
			entitywitch.onInitialSpawn(worldObj.getDifficultyForLocation(new BlockPos(entitywitch)),
					(IEntityLivingData) null);
			worldObj.spawnEntityInWorld(entitywitch);
			setDead();
		}
	}

	private void populateBuyingList() {
		EntityPigmanVillager.ITradeList[][][] aitradelist = DEFAULT_TRADE_LIST_MAP[rand.nextInt(5)];

		if ((careerId != 0) && (careerLevel != 0)) {
			++careerLevel;
		} else {
			careerId = rand.nextInt(aitradelist.length) + 1;
			careerLevel = 1;
		}

		if (buyingList == null) {
			buyingList = new MerchantRecipeList();
		}

		int i = careerId - 1;
		EntityPigmanVillager.ITradeList[][] aitradelist1 = aitradelist[i];
		careerLevel = rand.nextInt(aitradelist1.length) + 1;
		int j = careerLevel - 1;

		if (j < aitradelist1.length) {
			EntityPigmanVillager.ITradeList[] aitradelist2 = aitradelist1[j];
			EntityPigmanVillager.ITradeList[] aitradelist3 = aitradelist2;
			int k = aitradelist2.length;

			for (int l = 0; l < k; ++l) {
				EntityPigmanVillager.ITradeList itradelist = aitradelist3[l];
				itradelist.modifyMerchantRecipeList(buyingList, rand);
			}
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund) {
		super.readEntityFromNBT(tagCompund);
		setProfession(tagCompund.getInteger("Profession"));
		wealth = tagCompund.getInteger("Riches");
		careerId = tagCompund.getInteger("Career");
		careerLevel = tagCompund.getInteger("CareerLevel");
		isWillingToMate = tagCompund.getBoolean("Willing");

		if (tagCompund.hasKey("Offers", 10)) {
			NBTTagCompound nbttagcompound1 = tagCompund.getCompoundTag("Offers");
			buyingList = new MerchantRecipeList(nbttagcompound1);
		}

		NBTTagList nbttaglist = tagCompund.getTagList("Inventory", 10);

		for (int i = 0; i < nbttaglist.tagCount(); ++i) {
			ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));

			if (itemstack != null) {
				villagerInventory.func_174894_a(itemstack);
			}
		}

		setCanPickUpLoot(true);
	}

	@Override
	public boolean replaceItemInInventory(int p_174820_1_, ItemStack p_174820_2_) {
		if (super.replaceItemInInventory(p_174820_1_, p_174820_2_)) {
			return true;
		} else {
			int j = p_174820_1_ - 300;

			if ((j >= 0) && (j < villagerInventory.getSizeInventory())) {
				villagerInventory.setInventorySlotContents(j, p_174820_2_);
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public void setCustomer(EntityPlayer p_70932_1_) {
		buyingPlayer = p_70932_1_;
	}

	public void setIsWillingToMate(boolean willingToTrade) {
		isWillingToMate = willingToTrade;
	}

	public void setLookingForHome() {
		isLookingForHome = true;
	}

	public void setMating(boolean mating) {
		isMating = mating;
	}

	public void setPlaying(boolean playing) {
		isPlaying = playing;
	}

	public void setProfession(int professionId) {
		dataWatcher.updateObject(16, Integer.valueOf(professionId));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setRecipes(MerchantRecipeList p_70930_1_) {
	}

	@Override
	public void setRevengeTarget(EntityLivingBase livingBase) {
		super.setRevengeTarget(livingBase);

		if ((villageObj != null) && (livingBase != null)) {
			villageObj.addOrRenewAgressor(livingBase);

			if (livingBase instanceof EntityPlayer) {
				byte b0 = -1;

				if (isChild()) {
					b0 = -3;
				}

				villageObj.setReputationForPlayer(livingBase.getDisplayName().getUnformattedText(), b0);

				if (isEntityAlive()) {
					worldObj.setEntityState(this, (byte) 13);
				}
			}
		}
	}

	@Override
	protected void updateAITasks() {
		if (--randomTickDivider <= 0) {
			BlockPos blockpos = new BlockPos(this);
			worldObj.getVillageCollection().addToVillagerPositionList(blockpos);
			randomTickDivider = 70 + rand.nextInt(50);
			villageObj = worldObj.getVillageCollection().getNearestVillage(blockpos, 32);

			if (villageObj == null) {
				detachHome();
			} else {
				BlockPos blockpos1 = villageObj.getCenter();
				setHomePosAndDistance(blockpos1, (int) (villageObj.getVillageRadius() * 1.0F));

				if (isLookingForHome) {
					isLookingForHome = false;
					villageObj.setDefaultPlayerReputation(5);
				}
			}
		}

		if (!isTrading() && (timeUntilReset > 0)) {
			--timeUntilReset;

			if (timeUntilReset <= 0) {
				if (needsInitilization) {
					Iterator iterator = buyingList.iterator();

					while (iterator.hasNext()) {
						MerchantRecipe merchantrecipe = (MerchantRecipe) iterator.next();

						if (merchantrecipe.isRecipeDisabled()) {
							merchantrecipe.increaseMaxTradeUses(rand.nextInt(6) + rand.nextInt(6) + 2);
						}
					}

					populateBuyingList();
					needsInitilization = false;

					if ((villageObj != null) && (lastBuyingPlayer != null)) {
						worldObj.setEntityState(this, (byte) 14);
						villageObj.setReputationForPlayer(lastBuyingPlayer, 1);
					}
				}

				addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
			}
		}

		super.updateAITasks();
	}

	@Override
	public void useRecipe(MerchantRecipe p_70933_1_) {
		p_70933_1_.incrementToolUses();
		livingSoundTime = -getTalkInterval();
		playSound("mob.villager.yes", getSoundVolume(), getSoundPitch());
		int i = 3 + rand.nextInt(4);

		if ((p_70933_1_.getToolUses() == 1) || (rand.nextInt(5) == 0)) {
			timeUntilReset = 40;
			needsInitilization = true;
			isWillingToMate = true;

			if (buyingPlayer != null) {
				lastBuyingPlayer = buyingPlayer.getDisplayName().getUnformattedText();
			} else {
				lastBuyingPlayer = null;
			}

			i += 5;
		}

		if (p_70933_1_.getItemToBuy().getItem() == RediscoveredItemsManager.gemRuby) {
			wealth += p_70933_1_.getItemToBuy().stackSize;
		}

		if (p_70933_1_.getRewardsExp()) {
			worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY + 0.5D, posZ, i));
		}
	}

	/**
	 * Notifies the merchant of a possible merchantrecipe being fulfilled or
	 * not. Usually, this is just a sound byte being played depending if the
	 * suggested itemstack is not null.
	 */
	@Override
	public void verifySellingItem(ItemStack p_110297_1_) {
		if (!worldObj.isRemote && (livingSoundTime > (-getTalkInterval() + 20))) {
			livingSoundTime = -getTalkInterval();

			if (p_110297_1_ != null) {
				playSound("mob.pig.say", getSoundVolume(), getSoundPitch());
			} else {
				playSound("mob.pig.say", getSoundVolume(), getSoundPitch());
			}
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		super.writeEntityToNBT(tagCompound);
		tagCompound.setInteger("Profession", getProfession());
		tagCompound.setInteger("Riches", wealth);
		tagCompound.setInteger("Career", careerId);
		tagCompound.setInteger("CareerLevel", careerLevel);
		tagCompound.setBoolean("Willing", isWillingToMate);

		if (buyingList != null) {
			tagCompound.setTag("Offers", buyingList.getRecipiesAsTags());
		}

		NBTTagList nbttaglist = new NBTTagList();

		for (int i = 0; i < villagerInventory.getSizeInventory(); ++i) {
			ItemStack itemstack = villagerInventory.getStackInSlot(i);

			if (itemstack != null) {
				nbttaglist.appendTag(itemstack.writeToNBT(new NBTTagCompound()));
			}
		}

		tagCompound.setTag("Inventory", nbttaglist);
	}
}
