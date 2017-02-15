package com.stormister.rediscovered.entity;

import java.util.Iterator;
import java.util.List;

import com.stormister.rediscovered.mod_Rediscovered;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityRediscoveredPotion extends EntityThrowable implements IEntityAdditionalSpawnData {
	private ItemStack potionDamage;
	int color = 16388, randomTilt;
	public int metadata;
	PotionEffect potioneffect;

	public EntityRediscoveredPotion(World worldIn) {
		super(worldIn);
	}

	@SideOnly(Side.CLIENT)
	public EntityRediscoveredPotion(World worldIn, double p_i1791_2_, double p_i1791_4_, double p_i1791_6_,
			int p_i1791_8_) {
		this(worldIn, p_i1791_2_, p_i1791_4_, p_i1791_6_,
				new ItemStack(mod_Rediscovered.RediscoveredPotion, 1, p_i1791_8_));
	}

	public EntityRediscoveredPotion(World worldIn, double p_i1792_2_, double p_i1792_4_, double p_i1792_6_,
			ItemStack p_i1792_8_) {
		super(worldIn, p_i1792_2_, p_i1792_4_, p_i1792_6_);
		potionDamage = p_i1792_8_;
	}

	public EntityRediscoveredPotion(World worldIn, EntityLivingBase p_i1789_2_, int p_i1789_3_) {
		this(worldIn, p_i1789_2_, new ItemStack(mod_Rediscovered.RediscoveredPotion, 1, p_i1789_3_));
	}

	public EntityRediscoveredPotion(World worldIn, EntityLivingBase p_i1790_2_, ItemStack p_i1790_3_) {
		super(worldIn, p_i1790_2_);
		potionDamage = p_i1790_3_;
		randomTilt = rand.nextInt(360);
		metadata = potionDamage.getMetadata();

		if (metadata == 100) {
			potioneffect = new PotionEffect(9, 720, 0);
			color = 16388;
		} else if (metadata == 101) {
			potioneffect = new PotionEffect(15, 720, 0);
			color = 16393;
		} else if (metadata == 102) {
			potioneffect = new PotionEffect(4, 720, 0);
			color = 16398;
		} else {
			potioneffect = new PotionEffect(0, 0, 0);
			color = 0;
		}
	}

	/**
	 * Gets the amount of gravity to apply to the thrown entity with each tick.
	 */
	@Override
	protected float getGravityVelocity() {
		return 0.05F;
	}

	@Override
	protected float getInaccuracy() {
		return -20.0F;
	}

	public int getPotionDamage() {
		if (potionDamage == null) {
			potionDamage = new ItemStack(Items.potionitem, 1, 0);
		}
		return metadata;
	}

	public int getRandomTilt() {
		return randomTilt;
	}

	@Override
	protected float getVelocity() {
		return 0.5F;
	}

	/**
	 * Called when this EntityThrowable hits a block or entity.
	 */
	@Override
	protected void onImpact(MovingObjectPosition p_70184_1_) {
		if (!worldObj.isRemote) {

			AxisAlignedBB axisalignedbb = getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D);
			List list1 = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);

			if (!list1.isEmpty()) {
				Iterator iterator = list1.iterator();

				while (iterator.hasNext()) {
					EntityLivingBase entitylivingbase = (EntityLivingBase) iterator.next();
					double d0 = getDistanceSqToEntity(entitylivingbase);

					if (d0 < 16.0D) {
						double d1 = 1.0D - (Math.sqrt(d0) / 4.0D);

						if (entitylivingbase == p_70184_1_.entityHit) {
							d1 = 1.0D;
						}

						int i = potioneffect.getPotionID();
						int j = (int) ((d1 * potioneffect.getDuration()) + 0.5D);

						if (j > 20) {
							entitylivingbase.addPotionEffect(new PotionEffect(i, j, potioneffect.getAmplifier()));
						}
					}
				}
			}

			worldObj.playAuxSFX(2002, new BlockPos(this), color);
			setDead();
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund) {
		super.readEntityFromNBT(tagCompund);

		if (tagCompund.hasKey("Potion", 10)) {
			potionDamage = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("Potion"));
		} else {
			setPotionDamage(tagCompund.getInteger("potionValue"));
		}

		if (potionDamage == null) {
			setDead();
		}
	}

	@Override
	public void readSpawnData(ByteBuf buffer) {
		metadata = buffer.readInt();
	}

	/**
	 * Sets the PotionEffect by the given id of the potion effect.
	 */
	public void setPotionDamage(int potionId) {
		if (potionDamage == null) {
			potionDamage = new ItemStack(Items.potionitem, 1, 0);
		}

		potionDamage.setItemDamage(potionId);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		super.writeEntityToNBT(tagCompound);

		if (potionDamage != null) {
			tagCompound.setTag("Potion", potionDamage.writeToNBT(new NBTTagCompound()));
		}
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(metadata);
	}
}