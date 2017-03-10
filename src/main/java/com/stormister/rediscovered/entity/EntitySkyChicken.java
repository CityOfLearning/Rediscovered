package com.stormister.rediscovered.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntitySkyChicken extends EntityChicken {

	/** AI task for player control. */

	public EntitySkyChicken(World par1World) {
		super(par1World);
	}

	/**
	 * returns true if all the conditions for steering the entity are met. For
	 * pigs, this is true if it is being ridden by a player and the player is
	 * holding a carrot-on-a-stick
	 */
	@Override
	public boolean canBeSteered() {
		ItemStack itemstack = ((EntityPlayer) riddenByEntity).getHeldItem();
		return (itemstack != null) && (itemstack.getItem() == Items.wheat_seeds);
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer par1EntityPlayer) {
		if (super.interact(par1EntityPlayer)) {
			return true;
		} else if (!worldObj.isRemote && ((riddenByEntity == null) || (riddenByEntity == par1EntityPlayer))) {
			par1EntityPlayer.mountEntity(this);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return true;
	}

	public void makeJump() {
		jump();
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
	}

	/**
	 * This function is used when two same-species animals in 'love mode' breed
	 * to generate the new baby animal.
	 */
	public EntitySkyChicken spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
		return new EntitySkyChicken(worldObj);
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
	}
}
