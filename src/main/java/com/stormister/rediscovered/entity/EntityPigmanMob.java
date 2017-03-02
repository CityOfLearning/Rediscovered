package com.stormister.rediscovered.entity;

import java.util.Calendar;

import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityPigmanMob extends EntityMob implements IRangedAttackMob {
	private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
	private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D,
			false);
	private Village villageObj;
	private int randomTickDivider;

	public EntityPigmanMob(World worldIn) {
		super(worldIn);
		tasks.addTask(1, new EntityAISwimming(this));
		tasks.addTask(3, new EntityAIAvoidEntity(this, EntityTNTPrimed.class, 8.0F, 0.6D, 0.6D));
		tasks.addTask(3, new EntityAIAvoidEntity(this, EntityCreeper.class, 8.0F, 0.6D, 0.6D));
		tasks.addTask(3, new EntityAIAvoidEntity(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
		tasks.addTask(4, new EntityAIWander(this, 1.0D));
		tasks.addTask(5, new EntityAIMoveThroughVillage(this, 0.16F, true));
		tasks.addTask(6, new EntityAIMoveTowardsRestriction(this, 0.16F));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		tasks.addTask(6, new EntityAILookIdle(this));
		tasks.addTask(14, new EntityAIRestrictOpenDoor(this));
		tasks.addTask(15, new EntityAIOpenDoor(this, true));
		targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));

		if ((worldIn != null) && !worldIn.isRemote) {
			setCombatTask();
		}
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		if (super.attackEntityAsMob(entityIn)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) {
		EntityArrow entityarrow = new EntityArrow(worldObj, this, target, 1.6F,
				14 - (worldObj.getDifficulty().getDifficultyId() * 4));
		int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, getHeldItem());
		int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, getHeldItem());
		entityarrow.setDamage((p_82196_2_ * 2.0F) + (rand.nextGaussian() * 0.25D)
				+ (worldObj.getDifficulty().getDifficultyId() * 0.11F));

		if (i > 0) {
			entityarrow.setDamage(entityarrow.getDamage() + (i * 0.5D) + 0.5D);
		}

		if (j > 0) {
			entityarrow.setKnockbackStrength(j);
		}

		if ((EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, getHeldItem()) > 0)) {
			entityarrow.setFire(100);
		}

		playSound("random.bow", 1.0F, 1.0F / ((getRNG().nextFloat() * 0.4F) + 0.8F));
		worldObj.spawnEntityInWorld(entityarrow);
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn() {
		return false;
	}

	/**
	 * Drop 0-2 items of this living's type
	 *
	 * @param wasRecentlyHit
	 *            true if this this entity was recently hit by appropriate
	 *            entity (generally only if player or tameable)
	 * @param lootingModifier
	 *            level of enchanment to be applied to this drop
	 */
	@Override
	protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
		int j = rand.nextInt(3) + 1 + rand.nextInt(1 + lootingModifier);

		ItemStack itemstack = getHeldItem();

		if ((itemstack != null) && (itemstack.getItem() == Items.bow)) {
			for (int k = 0; k < j; ++k) {
				dropItem(Items.arrow, 1);
			}
		}

		j = rand.nextInt(2 + lootingModifier);

		for (int k = 0; k < j; ++k) {
			if (isBurning()) {
				dropItem(Items.cooked_porkchop, 1);
			} else {
				dropItem(Items.porkchop, 1);
			}
		}
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(13, new Byte((byte) 0));
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
		return "mob.pig.death";
	}

	@Override
	protected Item getDropItem() {
		return isBurning() ? Items.cooked_porkchop : Items.porkchop;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.pig.hurt";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.pig.say";
	}

	public Village getVillage() {
		return villageObj;
	}

	/**
	 * Returns the Y Offset of this entity.
	 */
	@Override
	public double getYOffset() {
		return isChild() ? 0.0D : -0.35D;
	}

	/**
	 * Called only once on an entity when first time spawned, via egg, mob
	 * spawner, natural spawning etc, but not called when entity is reloaded
	 * from nbt. Mainly used for initializing attributes and inventory
	 */
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);

		if ((getRNG().nextInt(2) > 0)) {
			tasks.addTask(4, aiAttackOnCollide);
			setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
			getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
		} else {
			tasks.addTask(4, aiArrowAttack);
			setEquipmentBasedOnDifficulty(difficulty);
			setEnchantmentBasedOnDifficulty(difficulty);
		}

		setCanPickUpLoot(rand.nextFloat() < (0.55F * difficulty.getClampedAdditionalDifficulty()));

		if (getEquipmentInSlot(4) == null) {
			Calendar calendar = worldObj.getCurrentDate();

			if (((calendar.get(2) + 1) == 10) && (calendar.get(5) == 31) && (rand.nextFloat() < 0.25F)) {
				setCurrentItemOrArmor(4, new ItemStack(rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
				equipmentDropChances[4] = 0.0F;
			}
		}

		return livingdata;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
		playSound("mob.pig.step", 0.15F, 1.0F);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund) {
		super.readEntityFromNBT(tagCompund);
		setCombatTask();
	}

	/**
	 * sets this entity's combat AI.
	 */
	public void setCombatTask() {
		tasks.removeTask(aiAttackOnCollide);
		tasks.removeTask(aiArrowAttack);
		ItemStack itemstack = getHeldItem();

		if ((itemstack != null) && (itemstack.getItem() == Items.bow)) {
			tasks.addTask(4, aiArrowAttack);
		} else {
			tasks.addTask(4, aiAttackOnCollide);
		}
	}

	/**
	 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is
	 * armor. Params: Item, slot
	 */
	@Override
	public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {
		super.setCurrentItemOrArmor(slotIn, stack);

		if (!worldObj.isRemote && (slotIn == 0)) {
			setCombatTask();
		}
	}

	/**
	 * Gives armor or weapon for entity based on given DifficultyInstance
	 */
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		super.setEquipmentBasedOnDifficulty(difficulty);
		setCurrentItemOrArmor(0, new ItemStack(Items.bow));
	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	@Override
	protected void updateAITasks() {
		if (--randomTickDivider <= 0) {
			BlockPos blockpos = new BlockPos(this);
			worldObj.getVillageCollection().addToVillagerPositionList(blockpos);
			randomTickDivider = 70 + rand.nextInt(50);
			villageObj = worldObj.getVillageCollection().getNearestVillage(blockpos, 32);

			if (villageObj == null) {
				detachHome();
			} else {
				BlockPos blockpos1 = villageObj.getCenter();
				setHomePosAndDistance(blockpos1, (int) (villageObj.getVillageRadius() * 1.0F));
			}
		}

		super.updateAITasks();
	}

	/**
	 * Handles updating while being ridden by an entity
	 */
	@Override
	public void updateRidden() {
		super.updateRidden();

		if (ridingEntity instanceof EntityCreature) {
			EntityCreature entitycreature = (EntityCreature) ridingEntity;
			renderYawOffset = entitycreature.renderYawOffset;
		}
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		super.writeEntityToNBT(tagCompound);
	}
}
