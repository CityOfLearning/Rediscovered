package com.stormister.rediscovered.entity;

import java.util.List;

import com.stormister.rediscovered.Rediscovered;

import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityScarecrow extends EntityCreature {

	public EntityScarecrow(World par1World) {
		super(par1World);
		preventEntitySpawning = true;
		setSize(1F, 2.0F);
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
			dropItem(Rediscovered.Scarecrow, 1);
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
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn() {
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

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity
	 * has recently been hit by a player. @param par2 - Level of Looting used to
	 * kill this mob.
	 */
	@Override
	protected void dropFewItems(boolean par1, int par2) {
		dropItem(Rediscovered.Scarecrow, 1);
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
		return Rediscovered.Scarecrow;
	}

	public List<EntityMob> getEntitiesInRadius(World world, double x, double y, double z, int radius) {
		List<EntityMob> list = world.getEntitiesWithinAABB(EntityMob.class,
				AxisAlignedBB.fromBounds(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));
		return list;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.skeleton.say";
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return true;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!worldObj.isRemote) {
			if (worldObj.handleMaterialAcceleration(getEntityBoundingBox().expand(0.0D, -0.6000000238418579D, 0.0D),
					Material.water, this)) {
				setDead();
				dropItem(Rediscovered.Scarecrow, 1);
			}
		}
		motionX *= 0.0;
		motionZ *= 0.0;

		// for(EntityMob entity : getEntitiesInRadius(this.worldObj, this.posX,
		// this.posY, this.posZ, 64)){
		// entity.setAttackTarget(this);
		// }

	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
	}
}
