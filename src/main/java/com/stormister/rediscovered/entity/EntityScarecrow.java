package com.stormister.rediscovered.entity;

import com.stormister.rediscovered.mod_Rediscovered;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityScarecrow extends EntityAnimal {
	public float field_70886_e;
	public float field_70884_g;
	public float field_70888_h;
	public float field_70889_i = 1.0F;

	public EntityScarecrow(World par1World) {
		super(par1World);
		preventEntitySpawning = true;
		setSize(0.5F, 2.0F);
	}

	public EntityScarecrow(World par1World, EntityPlayer player) {
		super(par1World);
		rotationYaw = player.rotationYaw;
		rotationPitch = player.rotationPitch;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(2.0D);
		getEntityAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(1.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0D);
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2) {
		if (!worldObj.isRemote && !isDead) {
			setDead();
			dropItem(mod_Rediscovered.Scarecrow, 1);
		}
		return true;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through
	 * this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	/**
	 * Returns true if this entity should push and be pushed by other entities
	 * when colliding.
	 */
	@Override
	public boolean canBePushed() {
		return false;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they
	 * walk on. used for spiders and wolves to prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
		return spawnBabyAnimal(par1EntityAgeable);
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity
	 * has recently been hit by a player. @param par2 - Level of Looting used to
	 * kill this mob.
	 */
	@Override
	protected void dropFewItems(boolean par1, int par2) {
		dropItem(mod_Rediscovered.Scarecrow, 1);
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	protected void fall(float par1) {
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	@Override
	protected Item getDropItem() {
		return mod_Rediscovered.Scarecrow;
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	public void onLivingUpdate() {
		if (!worldObj.isRemote) {
			if (worldObj.handleMaterialAcceleration(getEntityBoundingBox().expand(0.0D, -0.6000000238418579D, 0.0D),
					Material.water, this)) {
				setDead();
				dropItem(mod_Rediscovered.Scarecrow, 1);
			}
		}
		motionX *= 0.0;
		motionZ *= 0.0;
		super.onLivingUpdate();
	}

	@Override
	public void onUpdate() {

	}

	/**
	 * This function is used when two same-species animals in 'love mode' breed
	 * to generate the new baby animal.
	 */
	public EntityScarecrow spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
		return new EntityScarecrow(worldObj);
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
	}
}
