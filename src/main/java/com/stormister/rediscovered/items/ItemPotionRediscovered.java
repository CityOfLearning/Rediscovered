package com.stormister.rediscovered.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.stormister.rediscovered.Rediscovered;
import com.stormister.rediscovered.entity.EntityRediscoveredPotion;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPotionRediscovered extends Item {
	private static final Map SUB_ITEMS_CACHE = Maps.newLinkedHashMap();

	public static String getType(ItemStack itemStack) {
		if ((itemStack.getItemDamage() == 0) || (itemStack.getItemDamage() == 100)) {
			return "Nausea";
		} else if ((itemStack.getItemDamage() == 1) || (itemStack.getItemDamage() == 101)) {
			return "Blindness";
		} else if ((itemStack.getItemDamage() == 2) || (itemStack.getItemDamage() == 102)) {
			return "Dullness";
		} else {
			return "";
		}
	}

	public static boolean isSplash(int meta) {
		return (meta & 100) != 0;
	}

	private Map effectCache = Maps.newHashMap();

	private final String name = "RediscoveredPotion";

	public ItemPotionRediscovered() {
		super();
		setHasSubtypes(true);
		setMaxDamage(0);
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.tabBrewing);
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(Rediscovered.modid + "_" + name);
	}

	/**
	 * allows items to add custom lines of information to the mouseover
	 * description
	 */
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		if (getType(itemStack) == "Nausea") {
			par3List.add("\u00a77" + "Dizziness (0:30)");
		} else if (getType(itemStack) == "Blindness") {
			par3List.add("\u00a77" + "Blindness (0:30)");
		} else if (getType(itemStack) == "Dullness") {
			par3List.add("\u00a77" + "Slow Mining (0:30)");
		}

	}

	/**
	 * Returns a list of effects for the specified potion damage value.
	 */
	public List getEffects(int meta) {
		List list = (List) effectCache.get(Integer.valueOf(meta));

		if (list == null) {
			list = PotionHelper.getPotionEffects(meta, false);
			effectCache.put(Integer.valueOf(meta), list);
		}

		return list;
	}

	public List getEffects(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("CustomPotionEffects", 9)) {
			ArrayList arraylist = Lists.newArrayList();
			NBTTagList nbttaglist = stack.getTagCompound().getTagList("CustomPotionEffects", 10);

			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
				PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);

				if (potioneffect != null) {
					arraylist.add(potioneffect);
				}
			}

			return arraylist;
		} else {
			List list = (List) effectCache.get(Integer.valueOf(stack.getMetadata()));

			if (list == null) {
				list = PotionHelper.getPotionEffects(stack.getMetadata(), false);
				effectCache.put(Integer.valueOf(stack.getMetadata()), list);
			}

			return list;
		}
	}

	/**
	 * returns the action that specifies what animation to play when the items
	 * is being used
	 */
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.DRINK;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 32;
	}

	public String getName() {
		return name;
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		subItems.add(new ItemStack(itemIn, 1, 0));
		subItems.add(new ItemStack(itemIn, 1, 1));
		subItems.add(new ItemStack(itemIn, 1, 2));

		subItems.add(new ItemStack(itemIn, 1, 100));
		subItems.add(new ItemStack(itemIn, 1, 101));
		subItems.add(new ItemStack(itemIn, 1, 102));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + getType(stack) + (isSplash(stack.getMetadata()) ? ".Splash" : "");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack) {
		return true;
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (isSplash(itemStackIn.getMetadata())) {
			if (!playerIn.capabilities.isCreativeMode) {
				--itemStackIn.stackSize;
			}

			worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / ((itemRand.nextFloat() * 0.4F) + 0.8F));

			if (!worldIn.isRemote) {
				worldIn.spawnEntityInWorld(new EntityRediscoveredPotion(worldIn, playerIn, itemStackIn.getMetadata()));
			}

			return itemStackIn;
		} else {
			playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
			return itemStackIn;
		}
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		if (getType(itemStack) == "Nausea") {
			entityPlayer.addPotionEffect(new PotionEffect(Potion.confusion.id, 30 * 20, 6));
		} else if (getType(itemStack) == "Blindness") {
			entityPlayer.addPotionEffect(new PotionEffect(Potion.blindness.id, 30 * 20, 6));
		} else if (getType(itemStack) == "Dullness") {
			entityPlayer.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 30 * 20, 6));
		}

		if (!entityPlayer.capabilities.isCreativeMode) {
			itemStack.stackSize--;
			if (itemStack.stackSize <= 0) {
				return new ItemStack(Items.glass_bottle);
			}

			entityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
		}

		return itemStack;
	}

}
