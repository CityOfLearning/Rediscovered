package com.stormister.rediscovered.entity.ai;

import com.stormister.rediscovered.entity.EntitySkyChicken;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAISkyChickenLookIdle extends EntityAIBase {
	/** The entity that is looking idle. */
	private EntityLiving idleEntity;

	/** X offset to look at */
	private double lookX;

	/** Z offset to look at */
	private double lookZ;

	/**
	 * A decrementing tick that stops the entity from being idle once it reaches
	 * 0.
	 */
	private int idleTime = 0;

	private EntitySkyChicken entity;

	public EntityAISkyChickenLookIdle(EntityLiving par1EntityLiving, EntitySkyChicken entityskychicken) {
		entity = entityskychicken;
		idleEntity = par1EntityLiving;
		setMutexBits(3);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		return idleTime >= 0;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (entity.riddenByEntity != null) {
			return false;
		} else {
			return idleEntity.getRNG().nextFloat() < 0.02F;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		double var1 = (Math.PI * 2D) * idleEntity.getRNG().nextDouble();
		lookX = Math.cos(var1);
		lookZ = Math.sin(var1);
		idleTime = 20 + idleEntity.getRNG().nextInt(20);
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask() {
		--idleTime;
		idleEntity.getLookHelper().setLookPosition(idleEntity.posX + lookX, idleEntity.posY + idleEntity.getEyeHeight(),
				idleEntity.posZ + lookZ, 10.0F, idleEntity.getVerticalFaceSpeed());
	}
}
