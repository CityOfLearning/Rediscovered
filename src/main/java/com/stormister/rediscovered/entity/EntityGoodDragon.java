package com.stormister.rediscovered.entity;

import java.util.Iterator;
import java.util.List;

import com.stormister.rediscovered.IEntityMultiPartRed;
import com.stormister.rediscovered.mod_Rediscovered;

import net.minecraft.block.BlockTorch;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityGoodDragon extends EntityTameable implements IEntityMultiPartRed, IMob {
	public double targetX;
	public double targetY;
	public double targetZ;

	/**
	 * Ring buffer array for the last 64 Y-positions and yaw rotations. Used to
	 * calculate offsets for the animations.
	 */
	public double[][] ringBuffer = new double[64][3];

	/**
	 * Index into the ring buffer. Incremented once per tick and restarts at 0
	 * once it reaches the end of the buffer.
	 */
	public int ringBufferIndex = -1;

	/** An array containing all body parts of this dragon */
	public EntityGoodDragonPart[] dragonPartArray;

	/** The head bounding box of a dragon */
	public EntityGoodDragonPart dragonPartHead;

	/** The body bounding box of a dragon */
	public EntityGoodDragonPart dragonPartBody;
	public EntityGoodDragonPart dragonPartTail1;
	public EntityGoodDragonPart dragonPartTail2;
	public EntityGoodDragonPart dragonPartTail3;
	public EntityGoodDragonPart dragonPartTailSpike1;
	public EntityGoodDragonPart dragonPartTailSpike2;
	public EntityGoodDragonPart dragonPartTailSpike3;
	public EntityGoodDragonPart dragonPartTailSpike4;
	public EntityGoodDragonPart dragonPartTailSpike5;
	public EntityGoodDragonPart dragonPartWing1;
	public EntityGoodDragonPart dragonPartWing2;

	/** Animation time at previous tick. */
	public float prevAnimTime = 0.0F;

	/**
	 * Animation time, used to control the speed of the animation cycles (wings
	 * flapping, jaw opening, etc.)
	 */
	public float animTime = 0.0F;

	/** Force selecting a new flight target at next tick if set to true. */
	public boolean forceNewTarget = false;
	public boolean angry = false;
	public boolean renderTailSpike = false;

	public int health = 100;

	/**
	 * Activated if the dragon is flying though obsidian, white stone or
	 * bedrock. Slows movement and animation speed.
	 */
	public boolean slowed = false;
	private Entity target;
	public int deathTicks = 0;

	/** The current endercrystal that is healing this dragon */
	public EntityEnderCrystal healingEnderCrystal = null;

	public EntityGoodDragon(World par1World) {
		super(par1World);
		tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
		targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
		targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
		dragonPartArray = new EntityGoodDragonPart[] {
				dragonPartHead = new EntityGoodDragonPart(this, "head", 6.0F, 6.0F),
				dragonPartBody = new EntityGoodDragonPart(this, "body", 8.0F, 8.0F),
				dragonPartTail1 = new EntityGoodDragonPart(this, "tail", 4.0F, 4.0F),
				dragonPartTail2 = new EntityGoodDragonPart(this, "tail", 4.0F, 4.0F),
				dragonPartTail3 = new EntityGoodDragonPart(this, "tail", 4.0F, 4.0F),
				dragonPartWing1 = new EntityGoodDragonPart(this, "wing", 4.0F, 4.0F),
				dragonPartWing2 = new EntityGoodDragonPart(this, "wing", 4.0F, 4.0F),
				dragonPartTailSpike1 = new EntityGoodDragonPart(this, "tail", 2.0F, 2.0F),
				dragonPartTailSpike2 = new EntityGoodDragonPart(this, "tail", 2.0F, 2.0F),
				dragonPartTailSpike3 = new EntityGoodDragonPart(this, "tail", 2.0F, 2.0F),
				dragonPartTailSpike4 = new EntityGoodDragonPart(this, "tail", 2.0F, 2.0F),
				dragonPartTailSpike5 = new EntityGoodDragonPart(this, "tail", 2.0F, 2.0F) };
		setHealth(getMaxHealth());
		setSize(16.0F, 8.0F);
		noClip = true;
		isImmuneToFire = true;
		targetY = 100.0D;
		ignoreFrustumCheck = true;
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
	}

	/**
	 * Attacks all entities inside this list, dealing 5 hearts of damage.
	 */
	private void attackEntitiesInList(List par1List) {
		if (angry) {
			for (int var2 = 0; var2 < par1List.size(); ++var2) {
				Entity var3 = (Entity) par1List.get(var2);

				if (var3 instanceof EntityLivingBase) {
					var3.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0F);
				}
			}
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
		return false;
	}

	@Override
	public boolean attackEntityFromPart(EntityGoodDragonPart par1EntityGoodDragonPart, DamageSource par2DamageSource,
			float par3) {
		if (par1EntityGoodDragonPart != dragonPartHead) {
			par3 = (par3 / 4) + 1;
		}

		float var4 = (rotationYaw * (float) Math.PI) / 180.0F;
		float var5 = MathHelper.sin(var4);
		float var6 = MathHelper.cos(var4);
		targetX = posX + var5 * 5.0F + (rand.nextFloat() - 0.5F) * 2.0F;
		targetY = posY + rand.nextFloat() * 3.0F + 1.0D;
		targetZ = (posZ - var6 * 5.0F) + (rand.nextFloat() - 0.5F) * 2.0F;
		target = null;

		if ((par2DamageSource.getEntity() instanceof EntityPlayer) || par2DamageSource.isExplosion()) {
			angry = true;
			func_82195_e(par2DamageSource, par3);
		}

		return true;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through
	 * this Entity.
	 */
	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	/**
	 * Pushes all entities inside the list away from the enderdragon.
	 */
	private void collideWithEntities(List par1List) {
		double var2 = (dragonPartBody.getEntityBoundingBox().minX + dragonPartBody.getEntityBoundingBox().maxX) / 2.0D;
		double var4 = (dragonPartBody.getEntityBoundingBox().minZ + dragonPartBody.getEntityBoundingBox().maxZ) / 2.0D;
		Iterator var6 = par1List.iterator();

		while (var6.hasNext()) {
			Entity var7 = (Entity) var6.next();

			if (var7 instanceof EntityLiving) {
				double var8 = var7.posX - var2;
				double var10 = var7.posZ - var4;
				double var12 = (var8 * var8) + (var10 * var10);
				var7.addVelocity((var8 / var12) * 4.0D, 0.20000000298023224D, (var10 / var12) * 4.0D);
			}
		}
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return null;
	}

	/**
	 * Makes the entity despawn if requirements are reached
	 */
	@Override
	protected void despawnEntity() {
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(20, new Byte((byte) 0));
	}

	/**
	 * Creates the ender portal leading back to the normal world after defeating
	 * the enderdragon.
	 */
	private void func_175499_a(BlockPos p_175499_1_) {
		for (int i = -1; i <= 32; ++i) {
			for (int j = -4; j <= 4; ++j) {
				for (int k = -4; k <= 4; ++k) {
					double d2 = (j * j) + (k * k);

					if (d2 <= 12.25D) {
						BlockPos blockpos1 = p_175499_1_.add(j, i, k);

						if (i < 0) {
							if (d2 <= 6.25D) {
								worldObj.setBlockState(blockpos1, mod_Rediscovered.CryingObsidian.getDefaultState());
							}
						} else if (i > 0) {
							worldObj.setBlockState(blockpos1, Blocks.air.getDefaultState());
						} else if (d2 > 6.25D) {
							worldObj.setBlockState(blockpos1, mod_Rediscovered.CryingObsidian.getDefaultState());
						} else {
							worldObj.setBlockState(blockpos1, Blocks.fire.getDefaultState());
						}
					}
				}
			}
		}

		worldObj.setBlockState(p_175499_1_, mod_Rediscovered.CryingObsidian.getDefaultState());
		worldObj.setBlockState(p_175499_1_.up(), mod_Rediscovered.CryingObsidian.getDefaultState());
		BlockPos blockpos2 = p_175499_1_.up(2);
		worldObj.setBlockState(blockpos2, mod_Rediscovered.CryingObsidian.getDefaultState());
		worldObj.setBlockState(blockpos2.west(),
				Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.EAST));
		worldObj.setBlockState(blockpos2.east(),
				Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.WEST));
		worldObj.setBlockState(blockpos2.north(),
				Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.SOUTH));
		worldObj.setBlockState(blockpos2.south(),
				Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING, EnumFacing.NORTH));
		worldObj.setBlockState(p_175499_1_.up(3), mod_Rediscovered.CryingObsidian.getDefaultState());
		worldObj.setBlockState(p_175499_1_.up(4), mod_Rediscovered.DragonEggRed.getDefaultState());
	}

	@Override
	public World func_82194_d() {
		return worldObj;
	}

	protected boolean func_82195_e(DamageSource par1DamageSource, float par2) {
		return super.attackEntityFrom(par1DamageSource, par2);
	}

	@Override
	public float getBlockPathWeight(BlockPos p_180484_1_) {
		return worldObj.getLightBrightness(p_180484_1_) - 0.5F;
	}

	@Override
	public boolean getCanSpawnHere() {
		// int i = MathHelper.floor_double(this.posX);
		// int j = MathHelper.floor_double(this.posY);
		// int k = MathHelper.floor_double(this.posZ);
		// return
		// this.getBlockPathWeight(new BlockPos(i, j, k)) >= 0.0F &&
		// this.worldObj.getCollidingBoundingBoxes(this,
		// this.getEntityBoundingBox()).isEmpty() &&
		// !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
		return true;
	}

	@SideOnly(Side.CLIENT)

	/**
	 * Returns the health points of the dragon.
	 */
	public int getDragonHealth() {
		return dataWatcher.getWatchableObjectInt(16);
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.enderdragon.hit";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.enderdragon.growl";
	}

	/**
	 * Returns a double[3] array with movement offsets, used to calculate
	 * trailing tail/neck positions. [0] = yaw offset, [1] = y offset, [2] =
	 * unused, always 0. Parameters: buffer index offset, partial ticks.
	 */
	public double[] getMovementOffsets(int par1, float par2) {
		if (getMaxHealth() <= 0) {
			par2 = 0.0F;
		}

		par2 = 1.0F - par2;
		int var3 = (ringBufferIndex - (par1 * 1)) & 63;
		int var4 = (ringBufferIndex - (par1 * 1) - 1) & 63;
		double[] var5 = new double[3];
		double var6 = ringBuffer[var3][0];
		double var8 = MathHelper.wrapAngleTo180_double(ringBuffer[var4][0] - var6);
		var5[0] = var6 + (var8 * par2);
		var6 = ringBuffer[var3][1];
		var8 = ringBuffer[var4][1] - var6;
		var5[1] = var6 + (var8 * par2);
		var5[2] = ringBuffer[var3][2] + ((ringBuffer[var4][2] - ringBuffer[var3][2]) * par2);
		return var5;
	}

	/**
	 * Return the Entity parts making up this Entity (currently only for
	 * dragons)
	 */
	@Override
	public Entity[] getParts() {
		return dragonPartArray;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume() {
		return 5.0F;
	}

	@Override
	public boolean interactSpecial(EntityPlayer par1EntityPlayer) {
		ItemStack itemstack = par1EntityPlayer.getCurrentEquippedItem();
		if (!isTamed() && (itemstack != null) && (itemstack.getItem() == Items.bone)) {
			if (!par1EntityPlayer.capabilities.isCreativeMode) {
				--itemstack.stackSize;
			}

			if (itemstack.stackSize <= 0) {
				par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem,
						(ItemStack) null);
			}

			if (!worldObj.isRemote) {
				if (rand.nextInt(3) == 0) {
					par1EntityPlayer
							.addChatComponentMessage(new ChatComponentTranslation("You tamed me! Ah!", new Object[0]));
					setTamed(true);
					navigator.clearPathEntity();
					setAttackTarget((EntityLivingBase) null);
					// this.aiSit.setSitting(true);
					setOwnerId(par1EntityPlayer.getUniqueID().toString());
					playTameEffect(true);
					worldObj.setEntityState(this, (byte) 7);
				} else {
					par1EntityPlayer.addChatComponentMessage(new ChatComponentTranslation("Try Again!", new Object[0]));
					playTameEffect(false);
					worldObj.setEntityState(this, (byte) 6);
				}
			}

			return true;
		} else {
			if (riddenByEntity == null) {
				mount(par1EntityPlayer);
			}
		}
		return true;
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean mount(EntityPlayer par1EntityPlayer) {
		par1EntityPlayer.rotationYaw = rotationYaw;
		par1EntityPlayer.rotationPitch = rotationPitch;

		if (!worldObj.isRemote && ((riddenByEntity == null) || (riddenByEntity == par1EntityPlayer))) {
			par1EntityPlayer.mountEntity(this);
		}
		return true;
	}

	/**
	 * handles entity death timer, experience orb and particle creation
	 */
	@Override
	protected void onDeathUpdate() {
		++deathTicks;

		if ((deathTicks >= 180) && (deathTicks <= 200)) {
			float var1 = (rand.nextFloat() - 0.5F) * 8.0F;
			float var2 = (rand.nextFloat() - 0.5F) * 4.0F;
			float var3 = (rand.nextFloat() - 0.5F) * 8.0F;
			worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX + var1, posY + 2.0D + var2, posZ + var3, 0.0D,
					0.0D, 0.0D);
		}

		int var4;
		int var5;

		if (!worldObj.isRemote) {
			if ((deathTicks > 150) && ((deathTicks % 5) == 0)) {
				var4 = 1000;

				while (var4 > 0) {
					var5 = EntityXPOrb.getXPSplit(var4);
					var4 -= var5;
					worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, var5));
				}
			}

			if (deathTicks == 1) {
				worldObj.playBroadcastSound(1018, new BlockPos(this), 0);
			}
		}

		moveEntity(0.0D, 0.10000000149011612D, 0.0D);
		renderYawOffset = rotationYaw += 20.0F;

		if ((deathTicks == 200) && !worldObj.isRemote) {
			var4 = 2000;

			while (var4 > 0) {
				var5 = EntityXPOrb.getXPSplit(var4);
				var4 -= var5;
				worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, var5));
			}

			func_175499_a(new BlockPos(posX, 64.0D, posZ));
			setDead();
		}
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		float f;
		float f1;

		if (worldObj.isRemote) {
			f = MathHelper.cos(animTime * (float) Math.PI * 2.0F);
			f1 = MathHelper.cos(prevAnimTime * (float) Math.PI * 2.0F);

			if ((f1 <= -0.3F) && (f >= -0.3F)) {
				worldObj.playSound(posX, posY, posZ, "mob.enderdragon.wings", 5.0F, 0.8F + (rand.nextFloat() * 0.3F),
						false);
			}
		}

		prevAnimTime = animTime;
		float f2;

		if (getHealth() <= 0.0F) {
			f = (rand.nextFloat() - 0.5F) * 8.0F;
			f1 = (rand.nextFloat() - 0.5F) * 4.0F;
			f2 = (rand.nextFloat() - 0.5F) * 8.0F;
			worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX + f, posY + 2.0D + f1, posZ + f2, 0.0D, 0.0D,
					0.0D);
		} else {
			updateDragonEnderCrystal();
			f = 0.2F / ((MathHelper.sqrt_double((motionX * motionX) + (motionZ * motionZ)) * 10.0F) + 1.0F);
			f *= (float) Math.pow(2.0D, motionY);

			if (slowed) {
				animTime += f * 0.5F;
			} else {
				animTime += f;
			}

			rotationYaw = MathHelper.wrapAngleTo180_float(rotationYaw);

			if (ringBufferIndex < 0) {
				for (int i = 0; i < ringBuffer.length; ++i) {
					ringBuffer[i][0] = rotationYaw;
					ringBuffer[i][1] = posY;
				}
			}

			if (++ringBufferIndex == ringBuffer.length) {
				ringBufferIndex = 0;
			}

			ringBuffer[ringBufferIndex][0] = rotationYaw;
			ringBuffer[ringBufferIndex][1] = posY;
			double d0;
			double d1;
			double d2;
			double d3;
			float f3;

			if (worldObj.isRemote) {
				if (newPosRotationIncrements > 0) {
					d3 = posX + ((newPosX - posX) / newPosRotationIncrements);
					d0 = posY + ((newPosY - posY) / newPosRotationIncrements);
					d1 = posZ + ((newPosZ - posZ) / newPosRotationIncrements);
					d2 = MathHelper.wrapAngleTo180_double(newRotationYaw - rotationYaw);
					rotationYaw = (float) (rotationYaw + (d2 / newPosRotationIncrements));
					rotationPitch = (float) (rotationPitch
							+ ((newRotationPitch - rotationPitch) / newPosRotationIncrements));
					--newPosRotationIncrements;
					setPosition(d3, d0, d1);
					setRotation(rotationYaw, rotationPitch);
				}
			} else {
				d3 = targetX - posX;
				d0 = targetY - posY;
				d1 = targetZ - posZ;
				d2 = (d3 * d3) + (d0 * d0) + (d1 * d1);

				if (target != null) {
					targetX = target.posX;
					targetZ = target.posZ;
					double d4 = targetX - posX;
					double d5 = targetZ - posZ;
					double d6 = Math.sqrt((d4 * d4) + (d5 * d5));
					double d7 = (0.4000000059604645D + (d6 / 80.0D)) - 1.0D;

					if (d7 > 10.0D) {
						d7 = 10.0D;
					}

					targetY = target.posY + d7;
				} else {
					targetX += rand.nextGaussian() * 2.0D;
					targetZ += rand.nextGaussian() * 2.0D;
				}

				if (forceNewTarget || (d2 < 100.0D) || (d2 > 22500.0D) || isCollidedHorizontally
						|| isCollidedVertically) {
					setNewTarget();
				}

				d0 /= MathHelper.sqrt_double((d3 * d3) + (d1 * d1));
				f3 = 0.6F;

				if (d0 < (-f3)) {
					d0 = (-f3);
				}

				if (d0 > f3) {
					d0 = f3;
				}

				motionY += d0 * 0.10000000149011612D;
				rotationYaw = MathHelper.wrapAngleTo180_float(rotationYaw);
				double d8 = 180.0D - ((Math.atan2(d3, d1) * 180.0D) / Math.PI);
				double d9 = MathHelper.wrapAngleTo180_double(d8 - rotationYaw);

				if (d9 > 50.0D) {
					d9 = 50.0D;
				}

				if (d9 < -50.0D) {
					d9 = -50.0D;
				}
				Vec3 vec3 = (new Vec3(targetX - posX, targetY - posY, targetZ - posZ)).normalize();
				d8 = (-MathHelper.cos((rotationYaw * (float) Math.PI) / 180.0F));
				Vec3 vec31 = (new Vec3(MathHelper.sin((rotationYaw * (float) Math.PI) / 180.0F), motionY, d8))
						.normalize();
				float f4 = (float) (vec31.dotProduct(vec3) + 0.5D) / 1.5F;

				if (f4 < 0.0F) {
					f4 = 0.0F;
				}

				randomYawVelocity *= 0.8F;
				float f5 = (MathHelper.sqrt_double((motionX * motionX) + (motionZ * motionZ)) * 1.0F) + 1.0F;
				double d10 = (Math.sqrt((motionX * motionX) + (motionZ * motionZ)) * 1.0D) + 1.0D;

				if (d10 > 40.0D) {
					d10 = 40.0D;
				}

				randomYawVelocity = (float) (randomYawVelocity + (d9 * (0.699999988079071D / d10 / f5)));
				rotationYaw += randomYawVelocity * 0.1F;
				float f6 = (float) (2.0D / (d10 + 1.0D));
				float f7 = 0.06F;
				moveFlying(0.0F, -1.0F, f7 * ((f4 * f6) + (1.0F - f6)));

				if (slowed) {
					moveEntity(motionX * 0.800000011920929D, motionY * 0.800000011920929D,
							motionZ * 0.800000011920929D);
				} else {
					moveEntity(motionX, motionY, motionZ);
				}

				Vec3 vec32 = (new Vec3(motionX, motionY, motionZ)).normalize();
				float f8 = (float) (vec32.dotProduct(vec31) + 1.0D) / 2.0F;
				f8 = 0.8F + (0.15F * f8);
				motionX *= f8;
				motionZ *= f8;
				motionY *= 0.9100000262260437D;
			}

			renderYawOffset = rotationYaw;
			dragonPartHead.width = dragonPartHead.height = 3.0F;
			dragonPartTail1.width = dragonPartTail1.height = 2.0F;
			dragonPartTail2.width = dragonPartTail2.height = 2.0F;
			dragonPartTail3.width = dragonPartTail3.height = 2.0F;
			dragonPartBody.height = 3.0F;
			dragonPartBody.width = 5.0F;
			dragonPartWing1.height = 2.0F;
			dragonPartWing1.width = 4.0F;
			dragonPartWing2.height = 3.0F;
			dragonPartWing2.width = 4.0F;
			f1 = (((float) (getMovementOffsets(5, 1.0F)[1] - getMovementOffsets(10, 1.0F)[1]) * 10.0F) / 180.0F)
					* (float) Math.PI;
			f2 = MathHelper.cos(f1);
			float f9 = -MathHelper.sin(f1);
			float f10 = (rotationYaw * (float) Math.PI) / 180.0F;
			float f11 = MathHelper.sin(f10);
			float f12 = MathHelper.cos(f10);
			dragonPartBody.onUpdate();
			dragonPartBody.setLocationAndAngles(posX + f11 * 0.5F, posY, posZ - f12 * 0.5F, 0.0F, 0.0F);
			dragonPartWing1.onUpdate();
			dragonPartWing1.setLocationAndAngles(posX + f12 * 4.5F, posY + 2.0D, posZ + f11 * 4.5F, 0.0F, 0.0F);
			dragonPartWing2.onUpdate();
			dragonPartWing2.setLocationAndAngles(posX - f12 * 4.5F, posY + 2.0D, posZ - f11 * 4.5F, 0.0F, 0.0F);

			if (!worldObj.isRemote && (hurtTime == 0)) {
				collideWithEntities(worldObj.getEntitiesWithinAABBExcludingEntity(this,
						dragonPartWing1.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
				collideWithEntities(worldObj.getEntitiesWithinAABBExcludingEntity(this,
						dragonPartWing2.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
				attackEntitiesInList(worldObj.getEntitiesWithinAABBExcludingEntity(this,
						dragonPartHead.getEntityBoundingBox().expand(1.0D, 1.0D, 1.0D)));
			}

			double[] adouble = getMovementOffsets(5, 1.0F);
			double[] adouble1 = getMovementOffsets(0, 1.0F);
			f3 = MathHelper.sin(((rotationYaw * (float) Math.PI) / 180.0F) - (randomYawVelocity * 0.01F));
			float f13 = MathHelper.cos(((rotationYaw * (float) Math.PI) / 180.0F) - (randomYawVelocity * 0.01F));
			dragonPartHead.onUpdate();
			dragonPartHead.setLocationAndAngles(posX + f3 * 5.5F * f2,
					posY + ((adouble1[1] - adouble[1]) * 1.0D) + f9 * 5.5F, posZ - f13 * 5.5F * f2, 0.0F, 0.0F);

			for (int j = 0; j < 3; ++j) {
				EntityGoodDragonPart entitydragonpart = null;

				if (j == 0) {
					entitydragonpart = dragonPartTail1;
				}

				if (j == 1) {
					entitydragonpart = dragonPartTail2;
				}

				if (j == 2) {
					entitydragonpart = dragonPartTail3;
				}

				double[] adouble2 = getMovementOffsets(12 + (j * 2), 1.0F);
				float f14 = ((rotationYaw * (float) Math.PI) / 180.0F)
						+ (((simplifyAngle(adouble2[0] - adouble[0]) * (float) Math.PI) / 180.0F) * 1.0F);
				float f15 = MathHelper.sin(f14);
				float f16 = MathHelper.cos(f14);
				float f17 = 1.5F;
				float f18 = (j + 1) * 2.0F;
				entitydragonpart.onUpdate();
				entitydragonpart.setLocationAndAngles(posX - ((f11 * f17) + (f15 * f18)) * f2,
						((posY + ((adouble2[1] - adouble[1]) * 1.0D)) - (f18 + f17) * f9) + 1.5D,
						posZ + ((f12 * f17) + (f16 * f18)) * f2, 0.0F, 0.0F);
			}

			// if (!this.worldObj.isRemote)
			// {
			// this.slowed =
			// this.destroyBlocksInAABB(this.dragonPartHead.boundingBox) |
			// this.destroyBlocksInAABB(this.dragonPartBody.boundingBox);
			// }
		}
	}

	/**
	 * Sets a new target for the flight AI. It can be a random coordinate or a
	 * nearby player.
	 */
	private void setNewTarget() {
		forceNewTarget = false;

		if ((rand.nextInt(3) == 0) && !worldObj.playerEntities.isEmpty()) {
			target = worldObj.playerEntities.get(rand.nextInt(worldObj.playerEntities.size()));
		} else {
			boolean flag = false;

			do {
				// this.targetX = 0.0D;
				targetY = 70.0F + (rand.nextFloat() * 50.0F);
				// this.targetZ = 0.0D;
				targetX += (rand.nextFloat() * 120.0F) - 60.0F;
				targetZ += (rand.nextFloat() * 120.0F) - 60.0F;
				double d0 = posX - targetX;
				double d1 = posY - targetY;
				double d2 = posZ - targetZ;
				flag = ((d0 * d0) + (d1 * d1) + (d2 * d2)) > 100.0D;
			} while (!flag);

			target = null;
		}
	}

	/**
	 * Simplifies the value of a number by adding/subtracting 180 to the point
	 * that the number is between -180 and 180.
	 */
	private float simplifyAngle(double par1) {
		return (float) MathHelper.wrapAngleTo180_double(par1);
	}

	/**
	 * Updates the state of the enderdragon's current endercrystal.
	 */
	private void updateDragonEnderCrystal() {
		if (healingEnderCrystal != null) {
			if (healingEnderCrystal.isDead) {
				if (!worldObj.isRemote) {
					attackEntityFromPart(dragonPartHead, DamageSource.generic, 10);
				}

				healingEnderCrystal = null;
			} else if (((ticksExisted % 10) == 0) && (getMaxHealth() < getMaxHealth())) {
				++health;
			}
		}

		if (rand.nextInt(10) == 0) {
			float f = 32.0F;
			List var2 = worldObj.getEntitiesWithinAABB(EntityEnderCrystal.class,
					getEntityBoundingBox().expand(f, f, f));
			EntityEnderCrystal var3 = null;
			double var4 = Double.MAX_VALUE;
			Iterator var6 = var2.iterator();

			while (var6.hasNext()) {
				EntityEnderCrystal var7 = (EntityEnderCrystal) var6.next();
				double var8 = var7.getDistanceSqToEntity(this);

				if (var8 < var4) {
					var4 = var8;
					var3 = var7;
				}
			}

			healingEnderCrystal = var3;
		}
	}

	@Override
	public void updateRiderPosition() {
		if (riddenByEntity != null) {
			riddenByEntity.setPosition(posX, (posY + getMountedYOffset() + riddenByEntity.getYOffset()) - 2.5, posZ);
		}
	}
}
