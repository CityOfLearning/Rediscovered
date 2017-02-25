package com.stormister.rediscovered.entity.ai;

import com.stormister.rediscovered.entity.EntityPigmanVillager;

import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtTradePigman extends EntityAIWatchClosest {
	private final EntityPigmanVillager theMerchant;

	public EntityAILookAtTradePigman(EntityPigmanVillager par1EntityVillager) {
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