package com.stormister.rediscovered;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityZombieHorse extends EntityMob {
	private float field_110284_bK;
	private float field_110283_bJ;
	private float field_110281_bL;
	private float field_110282_bM;
	private float field_110287_bN;
	private float field_110288_bO;
	public int field_110278_bp;

	public EntityZombieHorse(World par1World) {
		super(par1World);
		setSize(1.4F, 1.6F);
		getNavigator();
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityHorse.class, 1.0D, true));
		tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
		tasks.addTask(5, new EntityAIMoveThroughVillage(this, 1.0D, false));
		tasks.addTask(6, new EntityAIWander(this, 1.0D));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(7, new EntityAILookIdle(this));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		applyEntityAI();
	}

	protected void applyEntityAI() {
		targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityHorse.class, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) {
		boolean flag = super.attackEntityAsMob(p_70652_1_);

		if (flag) {
			int i = worldObj.getDifficulty().getDifficultyId();

			if ((getHeldItem() == null) && isBurning() && (rand.nextFloat() < (i * 0.3F))) {
				p_70652_1_.setFire(2 * i);
			}
		}

		return flag;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (super.attackEntityFrom(source, amount)) {
			EntityLivingBase entitylivingbase = getAttackTarget();

			if ((entitylivingbase == null) && (source.getEntity() instanceof EntityLivingBase)) {
				entitylivingbase = (EntityLivingBase) source.getEntity();
			}

			MathHelper.floor_double(posX);
			MathHelper.floor_double(posY);
			MathHelper.floor_double(posZ);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn() {
		return true;
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity
	 * has recently been hit by a player. @param par2 - Level of Looting used to
	 * kill this mob.
	 */
	@Override
	protected void dropFewItems(boolean par1, int par2) {
		int j;
		int k;
		j = rand.nextInt(3 + par2);

		for (k = 0; k < j; ++k) {
			dropItem(Items.rotten_flesh, 1);
		}

		j = rand.nextInt(3 + par2);

		for (k = 0; k < j; ++k) {
			dropItem(Items.leather, 1);
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(16, Integer.valueOf(0));
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	public void fall(float distance, float damageMultiplier) {
		if (distance > 1.0F) {
			playSound("mob.horse.land", 0.4F, 1.0F);
		}

		int i = MathHelper.ceiling_float_int(((distance * 0.5F) - 3.0F) * damageMultiplier);

		if (i > 0) {
			attackEntityFrom(DamageSource.fall, i);

			if (riddenByEntity != null) {
				riddenByEntity.attackEntityFrom(DamageSource.fall, i);
			}

			Block block = worldObj.getBlockState(new BlockPos(posX, posY - 0.2D - prevRotationYaw, posZ)).getBlock();

			if ((block.getMaterial() != Material.air) && !isSilent()) {
				Block.SoundType soundtype = block.stepSound;
				worldObj.playSoundAtEntity(this, soundtype.getStepSound(), soundtype.getVolume() * 0.5F,
						soundtype.getFrequency() * 0.75F);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public float func_110201_q(float par1) {
		return field_110288_bO + ((field_110287_bN - field_110288_bO) * par1);
	}

	public boolean func_110204_cc() {
		return func_110233_w(32);
	}

	public boolean func_110209_cd() {
		return func_110233_w(64);
	}

	@SideOnly(Side.CLIENT)
	public float func_110223_p(float par1) {
		return field_110282_bM + ((field_110281_bL - field_110282_bM) * par1);
	}

	private boolean func_110233_w(int par1) {
		return (dataWatcher.getWatchableObjectInt(16) & par1) != 0;
	}

	public float func_110254_bY() {
		int i = 1;
		return i >= 0 ? 1.0F : 0.5F + (((-24000 - i) / -24000.0F) * 0.5F);
	}

	public boolean func_110257_ck() {
		return func_110233_w(4);
	}

	@SideOnly(Side.CLIENT)
	public float func_110258_o(float par1) {
		return field_110284_bK + ((field_110283_bJ - field_110284_bK) * par1);
	}

	public boolean func_110261_ca() {
		return func_110233_w(8);
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	@Override
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.horse.zombie.death";
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	@Override
	protected Item getDropItem() {
		return Items.rotten_flesh;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.horse.zombie.hit";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.horse.zombie.idle";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte par1) {
		if (par1 == 16) {
			worldObj.playSound(posX + 0.5D, posY + 0.5D, posZ + 0.5D, "mob.zombie.remedy", 1.0F + rand.nextFloat(),
					(rand.nextFloat() * 0.7F) + 0.3F, false);
		} else {
			super.handleStatusUpdate(par1);
		}
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	protected boolean isAIEnabled() {
		return true;
	}

	/**
	 * This method gets called when the entity kills another one.
	 */
	@Override
	public void onKillEntity(EntityLivingBase par1EntityLivingBase) {
		super.onKillEntity(par1EntityLivingBase);

		if (par1EntityLivingBase instanceof EntityHorse) {
			if (rand.nextBoolean()) {
				return;
			}

			EntityZombieHorse entityzombie = new EntityZombieHorse(worldObj);
			entityzombie.copyLocationAndAnglesFrom(par1EntityLivingBase);
			worldObj.removeEntity(par1EntityLivingBase);

			worldObj.spawnEntityInWorld(entityzombie);
			worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1016, new BlockPos((int) posX, (int) posY, (int) posZ), 0);
		}
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		if (worldObj.isDaytime() && !worldObj.isRemote && !isChild()) {
			float f = getBrightness(1.0F);
			BlockPos blockpos = new BlockPos(posX, Math.round(posY), posZ);

			if ((f > 0.5F) && ((rand.nextFloat() * 30.0F) < ((f - 0.4F) * 2.0F)) && worldObj.canSeeSky(blockpos)) {
				boolean flag = true;
				ItemStack itemstack = getEquipmentInSlot(4);

				if (itemstack != null) {
					if (itemstack.isItemStackDamageable()) {
						itemstack.setItemDamage(itemstack.getItemDamage() + rand.nextInt(2));

						if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
							renderBrokenItemStack(itemstack);
							setCurrentItemOrArmor(4, (ItemStack) null);
						}
					}

					flag = false;
				}

				if (flag) {
					setFire(8);
				}
			}
		}
		super.onLivingUpdate();
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();

		if (worldObj.isRemote && dataWatcher.hasObjectChanged()) {
			dataWatcher.func_111144_e();
		}

		if ((field_110278_bp > 0) && (++field_110278_bp > 8)) {
			field_110278_bp = 0;
		}

		field_110284_bK = field_110283_bJ;

		if (func_110204_cc()) {
			field_110283_bJ += ((1.0F - field_110283_bJ) * 0.4F) + 0.05F;

			if (field_110283_bJ > 1.0F) {
				field_110283_bJ = 1.0F;
			}
		} else {
			field_110283_bJ += ((0.0F - field_110283_bJ) * 0.4F) - 0.05F;

			if (field_110283_bJ < 0.0F) {
				field_110283_bJ = 0.0F;
			}
		}

		field_110282_bM = field_110281_bL;

		if (func_110209_cd()) {
			field_110284_bK = field_110283_bJ = 0.0F;
			field_110281_bL += ((1.0F - field_110281_bL) * 0.4F) + 0.05F;

			if (field_110281_bL > 1.0F) {
				field_110281_bL = 1.0F;
			}
		} else {
			field_110281_bL += (((0.8F * field_110281_bL * field_110281_bL * field_110281_bL) - field_110281_bL) * 0.6F)
					- 0.05F;

			if (field_110281_bL < 0.0F) {
				field_110281_bL = 0.0F;
			}
		}

		field_110288_bO = field_110287_bN;

		if (func_110233_w(128)) {
			field_110287_bN += ((1.0F - field_110287_bN) * 0.7F) + 0.05F;

			if (field_110287_bN > 1.0F) {
				field_110287_bN = 1.0F;
			}
		} else {
			field_110287_bN += ((0.0F - field_110287_bN) * 0.7F) - 0.05F;

			if (field_110287_bN < 0.0F) {
				field_110287_bN = 0.0F;
			}
		}
	}

	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	@Override
	protected void playStepSound(BlockPos p_180429_1_, Block p_180429_2_) {
		Block.SoundType soundtype = p_180429_2_.stepSound;

		if (worldObj.getBlockState(p_180429_1_.up()).getBlock() == Blocks.snow_layer) {
			soundtype = Blocks.snow_layer.stepSound;
		}

		if (!p_180429_2_.getMaterial().isLiquid()) {
			if (soundtype == Block.soundTypeWood) {
				playSound("mob.horse.wood", soundtype.getVolume() * 0.15F, soundtype.getFrequency());
			} else {
				playSound("mob.horse.soft", soundtype.getVolume() * 0.15F, soundtype.getFrequency());
			}
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
	}
}
