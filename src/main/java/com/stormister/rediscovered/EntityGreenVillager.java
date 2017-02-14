package com.stormister.rediscovered;

import java.util.Iterator;
import java.util.Random;

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
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityWitch;
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
import net.minecraft.stats.StatList;
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

public class EntityGreenVillager extends EntityAgeable implements INpc, IMerchant {
	public static class EmeraldForItems implements EntityGreenVillager.ITradeList {
		public Item field_179405_a;
		public EntityGreenVillager.PriceInfo field_179404_b;

		public EmeraldForItems(Item p_i45815_1_, EntityGreenVillager.PriceInfo p_i45815_2_) {
			field_179405_a = p_i45815_1_;
			field_179404_b = p_i45815_2_;
		}

		@Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
			int i = 1;

			if (field_179404_b != null) {
				i = field_179404_b.func_179412_a(random);
			}

			recipeList.add(new MerchantRecipe(new ItemStack(field_179405_a, i, 0), Items.emerald));
		}
	}

	public static class ItemAndEmeraldToItem implements EntityGreenVillager.ITradeList {
		public ItemStack field_179411_a;
		public EntityGreenVillager.PriceInfo field_179409_b;
		public ItemStack field_179410_c;
		public EntityGreenVillager.PriceInfo field_179408_d;

		public ItemAndEmeraldToItem(Item p_i45813_1_, EntityGreenVillager.PriceInfo p_i45813_2_, Item p_i45813_3_,
				EntityGreenVillager.PriceInfo p_i45813_4_) {
			field_179411_a = new ItemStack(p_i45813_1_);
			field_179409_b = p_i45813_2_;
			field_179410_c = new ItemStack(p_i45813_3_);
			field_179408_d = p_i45813_4_;
		}

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
					new ItemStack(Items.emerald),
					new ItemStack(field_179410_c.getItem(), j, field_179410_c.getMetadata())));
		}
	}

	public interface ITradeList {
		void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random);
	}

	public static class ListEnchantedBookForEmeralds implements EntityGreenVillager.ITradeList {

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

			recipeList.add(new MerchantRecipe(new ItemStack(Items.book), new ItemStack(Items.emerald, j), itemstack));
		}
	}

	public static class ListEnchantedItemForEmeralds implements EntityGreenVillager.ITradeList {
		public ItemStack field_179407_a;
		public EntityGreenVillager.PriceInfo field_179406_b;

		public ListEnchantedItemForEmeralds(Item p_i45814_1_, EntityGreenVillager.PriceInfo p_i45814_2_) {
			field_179407_a = new ItemStack(p_i45814_1_);
			field_179406_b = p_i45814_2_;
		}

		@Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
			int i = 1;

			if (field_179406_b != null) {
				i = field_179406_b.func_179412_a(random);
			}

			ItemStack itemstack = new ItemStack(Items.emerald, i, 0);
			ItemStack itemstack1 = new ItemStack(field_179407_a.getItem(), 1, field_179407_a.getMetadata());
			itemstack1 = EnchantmentHelper.addRandomEnchantment(random, itemstack1, 5 + random.nextInt(15));
			recipeList.add(new MerchantRecipe(itemstack, itemstack1));
		}
	}

	public static class ListItemForEmeralds implements EntityGreenVillager.ITradeList {
		public ItemStack field_179403_a;
		public EntityGreenVillager.PriceInfo field_179402_b;

		public ListItemForEmeralds(Item p_i45811_1_, EntityGreenVillager.PriceInfo p_i45811_2_) {
			field_179403_a = new ItemStack(p_i45811_1_);
			field_179402_b = p_i45811_2_;
		}

		public ListItemForEmeralds(ItemStack p_i45812_1_, EntityGreenVillager.PriceInfo p_i45812_2_) {
			field_179403_a = p_i45812_1_;
			field_179402_b = p_i45812_2_;
		}

		@Override
		public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
			int i = 1;

			if (field_179402_b != null) {
				i = field_179402_b.func_179412_a(random);
			}

			ItemStack itemstack;
			ItemStack itemstack1;

			if (i < 0) {
				itemstack = new ItemStack(Items.emerald, 1, 0);
				itemstack1 = new ItemStack(field_179403_a.getItem(), -i, field_179403_a.getMetadata());
			} else {
				itemstack = new ItemStack(Items.emerald, i, 0);
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

	@Deprecated // Use VillagerRegistry
	private static final EntityGreenVillager.ITradeList[][][][] DEFAULT_TRADE_LIST_MAP = new EntityGreenVillager.ITradeList[][][][] {
			{ { { new EntityGreenVillager.EmeraldForItems(Items.wheat, new EntityGreenVillager.PriceInfo(18, 22)),
					new EntityGreenVillager.EmeraldForItems(Items.potato, new EntityGreenVillager.PriceInfo(15, 19)),
					new EntityGreenVillager.EmeraldForItems(Items.carrot, new EntityGreenVillager.PriceInfo(15, 19)),
					new EntityGreenVillager.ListItemForEmeralds(Items.bread,
							new EntityGreenVillager.PriceInfo(-4, -2)) },
					{ new EntityGreenVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.pumpkin),
							new EntityGreenVillager.PriceInfo(8, 13)),
							new EntityGreenVillager.ListItemForEmeralds(
									Items.pumpkin_pie, new EntityGreenVillager.PriceInfo(-3, -2)) },
					{ new EntityGreenVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.melon_block),
							new EntityGreenVillager.PriceInfo(7, 12)),
							new EntityGreenVillager.ListItemForEmeralds(
									Items.apple, new EntityGreenVillager.PriceInfo(-5, -7)) },
					{ new EntityGreenVillager.ListItemForEmeralds(Items.cookie,
							new EntityGreenVillager.PriceInfo(-6, -10)),
							new EntityGreenVillager.ListItemForEmeralds(
									Items.cake, new EntityGreenVillager.PriceInfo(1, 1)) } },
					{ { new EntityGreenVillager.EmeraldForItems(Items.string,
							new EntityGreenVillager.PriceInfo(15, 20)),
							new EntityGreenVillager.EmeraldForItems(
									Items.coal, new EntityGreenVillager.PriceInfo(16, 24)),
							new EntityGreenVillager.ItemAndEmeraldToItem(Items.fish,
									new EntityGreenVillager.PriceInfo(6, 6), Items.cooked_fish,
									new EntityGreenVillager.PriceInfo(6, 6)) },
							{ new EntityGreenVillager.ListEnchantedItemForEmeralds(Items.fishing_rod,
									new EntityGreenVillager.PriceInfo(7, 8)) } },
					{ { new EntityGreenVillager.EmeraldForItems(Item.getItemFromBlock(Blocks.wool),
							new EntityGreenVillager.PriceInfo(16, 22)),
							new EntityGreenVillager.ListItemForEmeralds(
									Items.shears, new EntityGreenVillager.PriceInfo(3, 4)) },
							{ new EntityGreenVillager.ListItemForEmeralds(
									new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 0),
									new EntityGreenVillager.PriceInfo(1, 2)),
									new EntityGreenVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 1),
											new EntityGreenVillager.PriceInfo(1, 2)),
									new EntityGreenVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 2),
											new EntityGreenVillager.PriceInfo(1, 2)),
									new EntityGreenVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 3),
											new EntityGreenVillager.PriceInfo(1, 2)),
									new EntityGreenVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 4),
											new EntityGreenVillager.PriceInfo(1, 2)),
									new EntityGreenVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 5),
											new EntityGreenVillager.PriceInfo(1, 2)),
									new EntityGreenVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 6),
											new EntityGreenVillager.PriceInfo(1, 2)),
									new EntityGreenVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 7),
											new EntityGreenVillager.PriceInfo(1, 2)),
									new EntityGreenVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 8),
											new EntityGreenVillager.PriceInfo(1, 2)),
									new EntityGreenVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 9),
											new EntityGreenVillager.PriceInfo(1, 2)),
									new EntityGreenVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 10),
											new EntityGreenVillager.PriceInfo(1, 2)),
									new EntityGreenVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 11),
											new EntityGreenVillager.PriceInfo(1, 2)),
									new EntityGreenVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 12),
											new EntityGreenVillager.PriceInfo(1, 2)),
									new EntityGreenVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 13),
											new EntityGreenVillager.PriceInfo(1, 2)),
									new EntityGreenVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 14),
											new EntityGreenVillager.PriceInfo(1, 2)),
									new EntityGreenVillager.ListItemForEmeralds(
											new ItemStack(Item.getItemFromBlock(Blocks.wool), 1, 15),
											new EntityGreenVillager.PriceInfo(1, 2)) } },
					{ { new EntityGreenVillager.EmeraldForItems(Items.string,
							new EntityGreenVillager.PriceInfo(15, 20)),
							new EntityGreenVillager.ListItemForEmeralds(Items.arrow,
									new EntityGreenVillager.PriceInfo(-12, -8)) },
							{ new EntityGreenVillager.ListItemForEmeralds(Items.bow,
									new EntityGreenVillager.PriceInfo(2, 3)),
									new EntityGreenVillager.ItemAndEmeraldToItem(Item.getItemFromBlock(Blocks.gravel),
											new EntityGreenVillager.PriceInfo(10, 10), Items.flint,
											new EntityGreenVillager.PriceInfo(6, 10)) } } },
			{ { { new EntityGreenVillager.EmeraldForItems(Items.paper, new EntityGreenVillager.PriceInfo(24, 36)),
					new EntityGreenVillager.ListEnchantedBookForEmeralds() },
					{ new EntityGreenVillager.EmeraldForItems(Items.book, new EntityGreenVillager.PriceInfo(8, 10)),
							new EntityGreenVillager.ListItemForEmeralds(Items.compass,
									new EntityGreenVillager.PriceInfo(10, 12)),
							new EntityGreenVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.bookshelf),
									new EntityGreenVillager.PriceInfo(3, 4)) },
					{ new EntityGreenVillager.EmeraldForItems(Items.written_book,
							new EntityGreenVillager.PriceInfo(2, 2)),
							new EntityGreenVillager.ListItemForEmeralds(Items.clock,
									new EntityGreenVillager.PriceInfo(10, 12)),
							new EntityGreenVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.glass),
									new EntityGreenVillager.PriceInfo(-5, -3)) },
					{ new EntityGreenVillager.ListEnchantedBookForEmeralds() },
					{ new EntityGreenVillager.ListEnchantedBookForEmeralds() },
					{ new EntityGreenVillager.ListItemForEmeralds(Items.name_tag,
							new EntityGreenVillager.PriceInfo(20, 22)) } } },
			{ { { new EntityGreenVillager.EmeraldForItems(Items.rotten_flesh,
					new EntityGreenVillager.PriceInfo(36, 40)),
					new EntityGreenVillager.EmeraldForItems(Items.gold_ingot,
							new EntityGreenVillager.PriceInfo(8, 10)) },
					{ new EntityGreenVillager.ListItemForEmeralds(Items.redstone,
							new EntityGreenVillager.PriceInfo(-4, -1)),
							new EntityGreenVillager.ListItemForEmeralds(
									new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()),
									new EntityGreenVillager.PriceInfo(-2, -1)) },
					{ new EntityGreenVillager.ListItemForEmeralds(Items.ender_eye,
							new EntityGreenVillager.PriceInfo(7, 11)),
							new EntityGreenVillager.ListItemForEmeralds(Item.getItemFromBlock(Blocks.glowstone),
									new EntityGreenVillager.PriceInfo(-3, -1)) },
					{ new EntityGreenVillager.ListItemForEmeralds(Items.experience_bottle,
							new EntityGreenVillager.PriceInfo(3, 11)) } } },
			{ { { new EntityGreenVillager.EmeraldForItems(Items.coal, new EntityGreenVillager.PriceInfo(16, 24)),
					new EntityGreenVillager.ListItemForEmeralds(Items.iron_helmet,
							new EntityGreenVillager.PriceInfo(4, 6)) },
					{ new EntityGreenVillager.EmeraldForItems(Items.iron_ingot,
							new EntityGreenVillager.PriceInfo(7, 9)),
							new EntityGreenVillager.ListItemForEmeralds(Items.iron_chestplate,
									new EntityGreenVillager.PriceInfo(10, 14)) },
					{ new EntityGreenVillager.EmeraldForItems(Items.diamond, new EntityGreenVillager.PriceInfo(3, 4)),
							new EntityGreenVillager.ListEnchantedItemForEmeralds(Items.diamond_chestplate,
									new EntityGreenVillager.PriceInfo(16, 19)) },
					{ new EntityGreenVillager.ListItemForEmeralds(Items.chainmail_boots,
							new EntityGreenVillager.PriceInfo(5, 7)),
							new EntityGreenVillager.ListItemForEmeralds(Items.chainmail_leggings,
									new EntityGreenVillager.PriceInfo(9, 11)),
							new EntityGreenVillager.ListItemForEmeralds(Items.chainmail_helmet,
									new EntityGreenVillager.PriceInfo(5, 7)),
							new EntityGreenVillager.ListItemForEmeralds(Items.chainmail_chestplate,
									new EntityGreenVillager.PriceInfo(11, 15)) } },
					{ { new EntityGreenVillager.EmeraldForItems(Items.coal, new EntityGreenVillager.PriceInfo(16, 24)),
							new EntityGreenVillager.ListItemForEmeralds(Items.iron_axe,
									new EntityGreenVillager.PriceInfo(6, 8)) },
							{ new EntityGreenVillager.EmeraldForItems(Items.iron_ingot,
									new EntityGreenVillager.PriceInfo(7, 9)),
									new EntityGreenVillager.ListEnchantedItemForEmeralds(Items.iron_sword,
											new EntityGreenVillager.PriceInfo(9, 10)) },
							{ new EntityGreenVillager.EmeraldForItems(Items.diamond,
									new EntityGreenVillager.PriceInfo(3, 4)),
									new EntityGreenVillager.ListEnchantedItemForEmeralds(Items.diamond_sword,
											new EntityGreenVillager.PriceInfo(12, 15)),
									new EntityGreenVillager.ListEnchantedItemForEmeralds(Items.diamond_axe,
											new EntityGreenVillager.PriceInfo(9, 12)) } },
					{ { new EntityGreenVillager.EmeraldForItems(Items.coal, new EntityGreenVillager.PriceInfo(16, 24)),
							new EntityGreenVillager.ListEnchantedItemForEmeralds(Items.iron_shovel,
									new EntityGreenVillager.PriceInfo(5, 7)) },
							{ new EntityGreenVillager.EmeraldForItems(Items.iron_ingot,
									new EntityGreenVillager.PriceInfo(7, 9)),
									new EntityGreenVillager.ListEnchantedItemForEmeralds(Items.iron_pickaxe,
											new EntityGreenVillager.PriceInfo(9, 11)) },
							{ new EntityGreenVillager.EmeraldForItems(Items.diamond,
									new EntityGreenVillager.PriceInfo(3, 4)),
									new EntityGreenVillager.ListEnchantedItemForEmeralds(Items.diamond_pickaxe,
											new EntityGreenVillager.PriceInfo(12, 15)) } } },
			{ { { new EntityGreenVillager.EmeraldForItems(Items.porkchop, new EntityGreenVillager.PriceInfo(14, 18)),
					new EntityGreenVillager.EmeraldForItems(Items.chicken, new EntityGreenVillager.PriceInfo(14, 18)) },
					{ new EntityGreenVillager.EmeraldForItems(Items.coal, new EntityGreenVillager.PriceInfo(16, 24)),
							new EntityGreenVillager.ListItemForEmeralds(Items.cooked_porkchop,
									new EntityGreenVillager.PriceInfo(-7, -5)),
							new EntityGreenVillager.ListItemForEmeralds(Items.cooked_chicken,
									new EntityGreenVillager.PriceInfo(-8, -6)) } },
					{ { new EntityGreenVillager.EmeraldForItems(Items.leather,
							new EntityGreenVillager.PriceInfo(9, 12)),
							new EntityGreenVillager.ListItemForEmeralds(Items.leather_leggings,
									new EntityGreenVillager.PriceInfo(2, 4)) },
							{ new EntityGreenVillager.ListEnchantedItemForEmeralds(Items.leather_chestplate,
									new EntityGreenVillager.PriceInfo(7, 12)) },
							{ new EntityGreenVillager.ListItemForEmeralds(Items.saddle,
									new EntityGreenVillager.PriceInfo(8, 10)) } } } };
	private int randomTickDivider;
	private boolean isMating;
	private boolean isPlaying;
	Village villageObj;
	private EntityPlayer buyingPlayer;
	private MerchantRecipeList buyingList;
	private int timeUntilReset;
	private boolean needsInitilization;
	private boolean isWillingToTrade;

	private int wealth;

	private String lastBuyingPlayer;

	private int careerId;

	private int careerLevel;

	private boolean isLookingForHome;

	private boolean field_175564_by;

	private InventoryBasic villagerInventory;

	public EntityGreenVillager(World worldIn) {
		this(worldIn, 0);
	}

	public EntityGreenVillager(World worldIn, int professionId) {
		super(worldIn);
		villagerInventory = new InventoryBasic("Items", false, 8);
		setProfession(professionId);
		setSize(0.6F, 1.8F);
		((PathNavigateGround) getNavigator()).setBreakDoors(true);
		((PathNavigateGround) getNavigator()).setAvoidsWater(true);
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
		tasks.addTask(1, new EntityAITradeGreen(this));
		tasks.addTask(1, new EntityAILookAtTradeGreen(this));
		tasks.addTask(2, new EntityAIMoveIndoors(this));
		tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
		tasks.addTask(4, new EntityAIOpenDoor(this, true));
		tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 0.6D));
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

	@Override
	protected boolean canDespawn() {
		return false;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return func_180488_b(ageable);
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

	public void func_175549_o(boolean p_175549_1_) {
		isWillingToTrade = p_175549_1_;
	}

	public boolean func_175550_n(boolean p_175550_1_) {
		if (!isWillingToTrade && p_175550_1_ && func_175553_cp()) {
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
					isWillingToTrade = true;
					break;
				}
			}
		}

		return isWillingToTrade;
	}

	public InventoryBasic func_175551_co() {
		return villagerInventory;
	}

	private void func_175552_ct() {
		if (!field_175564_by) {
			field_175564_by = true;
		}
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

	public EntityGreenVillager func_180488_b(EntityAgeable p_180488_1_) {
		EntityGreenVillager entityvillager = new EntityGreenVillager(worldObj);
		entityvillager.onInitialSpawn(worldObj.getDifficultyForLocation(new BlockPos(entityvillager)),
				(IEntityLivingData) null);
		return entityvillager;
	}

	@SideOnly(Side.CLIENT)
	private void func_180489_a(EnumParticleTypes p_180489_1_) {
		for (int i = 0; i < 5; ++i) {
			double d0 = rand.nextGaussian() * 0.02D;
			double d1 = rand.nextGaussian() * 0.02D;
			double d2 = rand.nextGaussian() * 0.02D;
			worldObj.spawnParticle(p_180489_1_, (posX + rand.nextFloat() * width * 2.0F) - width,
					posY + 1.0D + rand.nextFloat() * height, (posZ + rand.nextFloat() * width * 2.0F) - width, d0, d1,
					d2, new int[0]);
		}
	}

	@Override
	public EntityPlayer getCustomer() {
		return buyingPlayer;
	}

	@Override
	protected String getDeathSound() {
		return "mob.villager.death";
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
				ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation(
						"entity.Villager." + s1, new Object[0]);
				chatcomponenttranslation.getChatStyle().setChatHoverEvent(getHoverEvent());
				chatcomponenttranslation.getChatStyle().setInsertion(getUniqueID().toString());
				return chatcomponenttranslation;
			} else {
				return super.getDisplayName();
			}
		}
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
		return "mob.villager.hit";
	}

	@Override
	protected String getLivingSound() {
		return isTrading() ? "mob.villager.haggle" : "mob.villager.idle";
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

	@Override
	public boolean interact(EntityPlayer player) {
		ItemStack itemstack = player.inventory.getCurrentItem();
		boolean flag = (itemstack != null) && (itemstack.getItem() == Items.spawn_egg);

		if (!flag && isEntityAlive() && !isTrading() && !isChild() && !player.isSneaking()) {
			if (!worldObj.isRemote && ((buyingList == null) || (buyingList.size() > 0))) {
				setCustomer(player);
				player.displayVillagerTradeGui(this);
			}

			player.triggerAchievement(StatList.timesTalkedToVillagerStat);
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
		GreenRegistry.setRandomProfession(this, worldObj.rand);
		return livingdata;
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt) {
		if (!worldObj.isRemote) {
			EntityWitch entitywitch = new EntityWitch(worldObj);
			entitywitch.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
			entitywitch.onInitialSpawn(worldObj.getDifficultyForLocation(new BlockPos(entitywitch)),
					(IEntityLivingData) null);
			worldObj.spawnEntityInWorld(entitywitch);
			setDead();
		}
	}

	private void populateBuyingList() {
		EntityGreenVillager.ITradeList[][][] aitradelist = DEFAULT_TRADE_LIST_MAP[getProfession()];

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
		int j = careerLevel - 1;
		EntityGreenVillager.ITradeList[][] aitradelist1 = aitradelist[i];

		if (j < aitradelist1.length) {
			EntityGreenVillager.ITradeList[] aitradelist2 = aitradelist1[j];
			EntityGreenVillager.ITradeList[] aitradelist3 = aitradelist2;
			int k = aitradelist2.length;

			for (int l = 0; l < k; ++l) {
				EntityGreenVillager.ITradeList itradelist = aitradelist3[l];
				itradelist.modifyMerchantRecipeList(buyingList, rand);
			}
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund) {
		super.readEntityFromNBT(tagCompund);
		setProfession(tagCompund.getInteger("Profession"));
		wealth = tagCompund.getInteger("Riches");
		careerId = tagCompund.getInteger("Career");
		careerLevel = tagCompund.getInteger("CareerLevel");
		isWillingToTrade = tagCompund.getBoolean("Willing");

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
		func_175552_ct();
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
			isWillingToTrade = true;

			if (buyingPlayer != null) {
				lastBuyingPlayer = buyingPlayer.getDisplayName().getUnformattedText();
			} else {
				lastBuyingPlayer = null;
			}

			i += 5;
		}

		if (p_70933_1_.getItemToBuy().getItem() == Items.emerald) {
			wealth += p_70933_1_.getItemToBuy().stackSize;
		}

		if (p_70933_1_.getRewardsExp()) {
			worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY + 0.5D, posZ, i));
		}
	}

	@Override
	public void verifySellingItem(ItemStack p_110297_1_) {
		if (!worldObj.isRemote && (livingSoundTime > (-getTalkInterval() + 20))) {
			livingSoundTime = -getTalkInterval();

			if (p_110297_1_ != null) {
				playSound("mob.villager.yes", getSoundVolume(), getSoundPitch());
			} else {
				playSound("mob.villager.no", getSoundVolume(), getSoundPitch());
			}
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		super.writeEntityToNBT(tagCompound);
		tagCompound.setInteger("Profession", getProfession());
		tagCompound.setInteger("Riches", wealth);
		tagCompound.setInteger("Career", careerId);
		tagCompound.setInteger("CareerLevel", careerLevel);
		tagCompound.setBoolean("Willing", isWillingToTrade);

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