package com.stormister.rediscovered.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class EntityAISkyChickenWander extends EntityAIBase {
	private EntityCreature entity;
	private double xPosition;
	private double yPosition;
	private double zPosition;
	private float speed;

	public EntityAISkyChickenWander(EntityCreature par1EntityCreature, float par2) {
		entity = par1EntityCreature;
		speed = par2;
		setMutexBits(1);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting() {
		if (entity.riddenByEntity != null) {
			return false;
		} else {
			return !entity.getNavigator().noPath();
		}
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute() {
		if (entity.getAge() >= 100) {
			return false;
		} else if (entity.getRNG().nextInt(120) != 0) {
			return false;
		} else {
			if (entity.riddenByEntity != null) {
				return false;
			}

			Vec3 var1 = RandomPositionGenerator.findRandomTarget(entity, 10, 7);

			if (var1 == null) {
				return false;
			} else {
				xPosition = var1.xCoord;
				yPosition = var1.yCoord;
				zPosition = var1.zCoord;
				return true;
			}
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		entity.getNavigator().tryMoveToXYZ(xPosition, yPosition, zPosition, speed);
	}
}
