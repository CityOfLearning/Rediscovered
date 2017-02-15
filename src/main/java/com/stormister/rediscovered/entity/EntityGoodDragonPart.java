package com.stormister.rediscovered.entity;

import com.stormister.rediscovered.IEntityMultiPartRed;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;

public class EntityGoodDragonPart extends Entity {
	/** The dragon entity this dragon part belongs to */
	public final IEntityMultiPartRed entityDragonObj;

	/** The name of the Dragon Part */
	public final String name;

	public EntityGoodDragonPart(IEntityMultiPartRed par1, String par2, float par3, float par4) {
		super(par1.func_82194_d());
		setSize(par3, par4);
		entityDragonObj = par1;
		name = par2;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return isEntityInvulnerable(source) ? false : entityDragonObj.attackEntityFromPart(this, source, amount);
	}

	/**
	 * Returns true if other Entities should be prevented from moving through
	 * this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	protected void entityInit() {
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interactFirst(EntityPlayer par1EntityPlayer) {
		return entityDragonObj.interactSpecial(par1EntityPlayer);
	}

	/**
	 * Returns true if Entity argument is equal to this Entity
	 */
	@Override
	public boolean isEntityEqual(Entity par1Entity) {
		return (this == par1Entity) || (entityDragonObj == par1Entity);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
	}
}
