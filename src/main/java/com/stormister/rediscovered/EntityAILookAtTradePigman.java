package com.stormister.rediscovered;

import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtTradePigman extends EntityAIWatchClosest {
	private final EntityPigman theMerchant;

	public EntityAILookAtTradePigman(EntityPigman par1EntityVillager) {
		super(par1EntityVillager, EntityPlayer.class, 8.0F);
		theMerchant = par1EntityVillager;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (theMerchant.isTrading()) {
			closestEntity = theMerchant.getCustomer();
			return true;
		} else {
			return false;
		}
	}
}