package com.stormister.rediscovered.entity.ai;

import com.stormister.rediscovered.entity.EntityGreenVillager;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class EntityAITradeGreen extends EntityAIBase {
	private EntityGreenVillager villager;

	public EntityAITradeGreen(EntityGreenVillager par1EntityVillager) {
		villager = par1EntityVillager;
		setMutexBits(5);
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask() {
		villager.setCustomer((EntityPlayer) null);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (!villager.isEntityAlive()) {
			return false;
		} else if (villager.isInWater()) {
			return false;
		} else if (!villager.onGround) {
			return false;
		} else if (villager.velocityChanged) {
			return false;
		} else {
			EntityPlayer entityplayer = villager.getCustomer();
			return entityplayer == null ? false
					: (villager.getDistanceSqToEntity(entityplayer) > 16.0D ? false
							: entityplayer.openContainer instanceof Container);
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		villager.getNavigator().clearPathEntity();
	}
}