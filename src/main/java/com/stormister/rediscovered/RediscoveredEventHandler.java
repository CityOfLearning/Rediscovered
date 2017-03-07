package com.stormister.rediscovered;

import java.util.Random;

import com.stormister.rediscovered.entity.EntityPigmanMob;
import com.stormister.rediscovered.entity.EntityScarecrow;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RediscoveredEventHandler {
	protected static Random itemRand = new Random();

	@SubscribeEvent
	public void onArrowLooseEvent(ArrowLooseEvent event) {
		EntityPlayer player = event.entityPlayer;
		InventoryPlayer inv = player.inventory;
		ItemStack par1ItemStack = inv.getStackInSlot(inv.currentItem);
		ItemStack blah = new ItemStack(Items.bow);
		ItemStack quiver = new ItemStack(Rediscovered.Quiver);
		ItemStack lquiver = new ItemStack(Rediscovered.LeatherQuiver);
		ItemStack cquiver = new ItemStack(Rediscovered.ChainQuiver);
		ItemStack gquiver = new ItemStack(Rediscovered.GoldQuiver);
		ItemStack iquiver = new ItemStack(Rediscovered.IronQuiver);
		ItemStack dquiver = new ItemStack(Rediscovered.DiamondQuiver);
		ItemStack lcquiver = new ItemStack(Rediscovered.LeatherChainQuiver);
		if (inv.getCurrentItem().equals(blah)) {

			EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack);
			ItemStack itemstack = player.inventory.armorInventory[2];

			if ((itemstack != null) && (itemstack.equals(quiver) || itemstack.equals(lquiver)
					|| itemstack.equals(cquiver) || itemstack.equals(iquiver) || itemstack.equals(gquiver)
					|| itemstack.equals(dquiver) || itemstack.equals(lcquiver))) {
				event.setCanceled(true);
			}

		}
	}

	// Quiver Bow
	@SubscribeEvent
	public void onArrowNockEvent(ArrowNockEvent event) {
		EntityPlayer player = event.entityPlayer;
		InventoryPlayer inv = player.inventory;
		ItemStack par1ItemStack = inv.getStackInSlot(inv.currentItem);

		boolean flag = player.capabilities.isCreativeMode
				|| (EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0);
		ItemStack itemstack = player.inventory.armorInventory[2];

		if ((itemstack != null) && ((itemstack.getItem() == Rediscovered.Quiver)
				|| (itemstack.getItem() == Rediscovered.LeatherQuiver)
				|| (itemstack.getItem() == Rediscovered.ChainQuiver) || (itemstack.getItem() == Rediscovered.GoldQuiver)
				|| (itemstack.getItem() == Rediscovered.IronQuiver)
				|| (itemstack.getItem() == Rediscovered.DiamondQuiver)
				|| (itemstack.getItem() == Rediscovered.LeatherChainQuiver))) {
			if (player.inventory.hasItem(Items.arrow) || flag) {
				EntityArrow entityarrow = new EntityArrow(player.worldObj, player, 1.0F);
				player.worldObj.playSoundAtEntity(player, "random.bow", 1.0F,
						1.0F / ((itemRand.nextFloat() * 0.4F) + 0.8F));
				entityarrow.setIsCritical(true);

				if (!player.worldObj.isRemote) {
					if (!flag) {
						player.inventory.consumeInventoryItem(Items.arrow);
					} else {
						entityarrow.canBePickedUp = 0;
					}
					player.worldObj.spawnEntityInWorld(entityarrow);
				}
				event.setCanceled(true);
			}
		}

	}

	// Mob AI
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityMob) {
			EntityMob entity = (EntityMob) event.entity;
			entity.targetTasks.addTask(2,
					new EntityAINearestAttackableTarget<EntityScarecrow>(entity, EntityScarecrow.class, true));
			if ((entity instanceof EntityZombie) || (entity instanceof EntitySilverfish)
					|| ((entity instanceof EntityPigmanMob) && (entity.getHeldItem() != null)
							&& (entity.getHeldItem().getItem() != Items.bow))) {
				entity.tasks.addTask(2, new EntityAIAttackOnCollide(entity, EntityScarecrow.class, 1.2D, false));
			}
		}
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		EntityPlayer player = event.entityPlayer;
		InventoryPlayer inv = player.inventory;
		ItemStack itemStack = inv.getStackInSlot(inv.currentItem);
		final World world = event.entityLiving.worldObj;

		// Bush Shearing
		if ((event.action == Action.RIGHT_CLICK_BLOCK)
				&& world.getBlockState(event.pos).getBlock().equals(Blocks.double_plant) && (itemStack != null)
				&& itemStack.getItem().equals(Items.shears) && (player instanceof EntityPlayerMP)) {
			event.setCanceled(true);
			if ((world.getBlockState(event.pos) == Blocks.double_plant.getStateFromMeta(4))
					|| (world.getBlockState(event.pos.down()) == Blocks.double_plant.getStateFromMeta(4))) {
				if (world.getBlockState(event.pos) == Blocks.double_plant.getStateFromMeta(4)) {
					world.setBlockState(event.pos, Rediscovered.EmptyRoseBush.getDefaultState());
					world.setBlockState(event.pos.up(), Rediscovered.EmptyRoseBushTop.getDefaultState());
				} else if ((world.getBlockState(event.pos.down()) == Blocks.double_plant.getStateFromMeta(4))) {
					world.setBlockState(event.pos.down(), Rediscovered.EmptyRoseBush.getDefaultState());
					world.setBlockState(event.pos, Rediscovered.EmptyRoseBushTop.getDefaultState());
				}
				ItemStack itemStack2 = new ItemStack(Rediscovered.Rose, world.rand.nextInt(3) + 1);
				EntityItem item = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(),
						itemStack2);
				world.spawnEntityInWorld(item);
			}
			if ((world.getBlockState(event.pos) == Blocks.double_plant.getStateFromMeta(5))
					|| (world.getBlockState(event.pos.down()) == Blocks.double_plant.getStateFromMeta(5))) {
				if (world.getBlockState(event.pos) == Blocks.double_plant.getStateFromMeta(5)) {
					world.setBlockState(event.pos, Rediscovered.EmptyPeonyBush.getDefaultState());
					world.setBlockState(event.pos.up(), Rediscovered.EmptyPeonyBushTop.getDefaultState());
				} else if ((world.getBlockState(event.pos.down()) == Blocks.double_plant.getStateFromMeta(5))) {
					world.setBlockState(event.pos.down(), Rediscovered.EmptyPeonyBush.getDefaultState());
					world.setBlockState(event.pos, Rediscovered.EmptyPeonyBushTop.getDefaultState());
				}
				ItemStack itemStack2 = new ItemStack(Rediscovered.Peony, world.rand.nextInt(3) + 1);
				EntityItem item = new EntityItem(world, event.pos.getX(), event.pos.getY(), event.pos.getZ(),
						itemStack2);
				world.spawnEntityInWorld(item);
			}
			itemStack.damageItem(1, player);
		}
	}
}
