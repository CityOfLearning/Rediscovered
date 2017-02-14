package com.stormister.rediscovered;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityParrow extends Entity implements IProjectile {
	private int xTile = -1;
	private int yTile = -1;
	private int zTile = -1;
	private Block inTile;
	private int inData;
	private boolean inGround;
	public int arrowShake;
	public Entity shootingEntity;
	private int ticksInGround;
	private int ticksInAir;
	private double damage = 2.0D;
	private int knockbackStrength;

	public EntityParrow(World worldIn) {
		super(worldIn);
		renderDistanceWeight = 10.0D;
		setSize(0.5F, 0.5F);
	}

	public EntityParrow(World worldIn, double x, double y, double z) {
		super(worldIn);
		renderDistanceWeight = 10.0D;
		setSize(0.5F, 0.5F);
		setPosition(x, y, z);
	}

	public EntityParrow(World worldIn, EntityLivingBase shooter, EntityLivingBase p_i1755_3_, float p_i1755_4_,
			float p_i1755_5_) {
		super(worldIn);
		renderDistanceWeight = 10.0D;
		shootingEntity = shooter;

		posY = (shooter.posY + shooter.getEyeHeight()) - 0.10000000149011612D;
		double d0 = p_i1755_3_.posX - shooter.posX;
		double d1 = (p_i1755_3_.getEntityBoundingBox().minY + p_i1755_3_.height / 3.0F) - posY;
		double d2 = p_i1755_3_.posZ - shooter.posZ;
		double d3 = MathHelper.sqrt_double((d0 * d0) + (d2 * d2));

		if (d3 >= 1.0E-7D) {
			float f2 = (float) ((Math.atan2(d2, d0) * 180.0D) / Math.PI) - 90.0F;
			float f3 = (float) (-((Math.atan2(d1, d3) * 180.0D) / Math.PI));
			double d4 = d0 / d3;
			double d5 = d2 / d3;
			setLocationAndAngles(shooter.posX + d4, posY, shooter.posZ + d5, f2, f3);
			float f4 = (float) (d3 * 0.20000000298023224D);
			setThrowableHeading(d0, d1 + f4, d2, p_i1755_4_, p_i1755_5_);
		}
	}

	public EntityParrow(World worldIn, EntityLivingBase shooter, float p_i1756_3_) {
		super(worldIn);
		renderDistanceWeight = 10.0D;
		shootingEntity = shooter;

		setSize(0.5F, 0.5F);
		setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw,
				shooter.rotationPitch);
		posX -= MathHelper.cos((rotationYaw / 180.0F) * (float) Math.PI) * 0.16F;
		posY -= 0.10000000149011612D;
		posZ -= MathHelper.sin((rotationYaw / 180.0F) * (float) Math.PI) * 0.16F;
		setPosition(posX, posY, posZ);
		motionX = -MathHelper.sin((rotationYaw / 180.0F) * (float) Math.PI)
				* MathHelper.cos((rotationPitch / 180.0F) * (float) Math.PI);
		motionZ = MathHelper.cos((rotationYaw / 180.0F) * (float) Math.PI)
				* MathHelper.cos((rotationPitch / 180.0F) * (float) Math.PI);
		motionY = (-MathHelper.sin((rotationPitch / 180.0F) * (float) Math.PI));
		setThrowableHeading(motionX, motionY, motionZ, p_i1756_3_ * 1.5F, 1.0F);
	}

	@Override
	public boolean canAttackWithItem() {
		return false;
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	@SideOnly(Side.CLIENT)
	public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_,
			float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
		setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
		setRotation(p_180426_7_, p_180426_8_);
	}

	public double getDamage() {
		return damage;
	}

	public boolean getIsCritical() {
		byte b0 = dataWatcher.getWatchableObjectByte(16);
		return (b0 & 1) != 0;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if ((prevRotationPitch == 0.0F) && (prevRotationYaw == 0.0F)) {
			float f = MathHelper.sqrt_double((motionX * motionX) + (motionZ * motionZ));
			prevRotationYaw = rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180.0D) / Math.PI);
			prevRotationPitch = rotationPitch = (float) ((Math.atan2(motionY, f) * 180.0D) / Math.PI);
		}

		BlockPos blockpos = new BlockPos(xTile, yTile, zTile);
		IBlockState iblockstate = worldObj.getBlockState(blockpos);
		Block block = iblockstate.getBlock();

		if (block.getMaterial() != Material.air) {
			block.setBlockBoundsBasedOnState(worldObj, blockpos);
			AxisAlignedBB axisalignedbb = block.getCollisionBoundingBox(worldObj, blockpos, iblockstate);

			if ((axisalignedbb != null) && axisalignedbb.isVecInside(new Vec3(posX, posY, posZ))) {
				inGround = true;
			}
		}

		if (arrowShake > 0) {
			--arrowShake;
		}

		if (inGround) {
			int j = block.getMetaFromState(iblockstate);

			if ((block == inTile) && (j == inData)) {
				++ticksInGround;

				if (ticksInGround >= 1200) {
					setDead();
				}
			} else {
				inGround = false;
				motionX *= rand.nextFloat() * 0.2F;
				motionY *= rand.nextFloat() * 0.2F;
				motionZ *= rand.nextFloat() * 0.2F;
				ticksInGround = 0;
				ticksInAir = 0;
			}
		} else {
			++ticksInAir;
			Vec3 vec31 = new Vec3(posX, posY, posZ);
			Vec3 vec3 = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);
			MovingObjectPosition movingobjectposition = worldObj.rayTraceBlocks(vec31, vec3, false, true, false);
			vec31 = new Vec3(posX, posY, posZ);
			vec3 = new Vec3(posX + motionX, posY + motionY, posZ + motionZ);

			if (movingobjectposition != null) {
				vec3 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord,
						movingobjectposition.hitVec.zCoord);
			}

			Entity entity = null;
			List list = worldObj.getEntitiesWithinAABBExcludingEntity(this,
					getEntityBoundingBox().addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
			double d0 = 0.0D;
			int i;
			float f1;

			for (i = 0; i < list.size(); ++i) {
				Entity entity1 = (Entity) list.get(i);

				if (entity1.canBeCollidedWith() && ((entity1 != shootingEntity) || (ticksInAir >= 5))) {
					f1 = 0.3F;
					AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expand(f1, f1, f1);
					MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

					if (movingobjectposition1 != null) {
						double d1 = vec31.distanceTo(movingobjectposition1.hitVec);

						if ((d1 < d0) || (d0 == 0.0D)) {
							entity = entity1;
							d0 = d1;
						}
					}
				}
			}

			if (entity != null) {
				movingobjectposition = new MovingObjectPosition(entity);
			}

			if ((movingobjectposition != null) && (movingobjectposition.entityHit != null)
					&& (movingobjectposition.entityHit instanceof EntityPlayer)) {
				EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;

				if (entityplayer.capabilities.disableDamage || ((shootingEntity instanceof EntityPlayer)
						&& !((EntityPlayer) shootingEntity).canAttackPlayer(entityplayer))) {
					movingobjectposition = null;
				}
			}

			float f2;
			float f3;
			float f4;

			if (movingobjectposition != null) {
				if (movingobjectposition.entityHit != null) {
					f2 = MathHelper.sqrt_double((motionX * motionX) + (motionY * motionY) + (motionZ * motionZ));
					int k = MathHelper.ceiling_double_int(f2 * damage);

					if (getIsCritical()) {
						k += rand.nextInt((k / 2) + 2);
					}

					DamageSource damagesource;

					if (shootingEntity == null) {
						damagesource = mod_Rediscovered.causeParrowDamage(this, this);
					} else {
						damagesource = mod_Rediscovered.causeParrowDamage(this, shootingEntity);
					}

					if (isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman)) {
						movingobjectposition.entityHit.setFire(5);
					}

					if (movingobjectposition.entityHit.attackEntityFrom(damagesource, k)) {
						if (movingobjectposition.entityHit instanceof EntityLivingBase) {
							EntityLivingBase entitylivingbase = (EntityLivingBase) movingobjectposition.entityHit;

							if (!worldObj.isRemote) {
								entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
							}

							if (knockbackStrength > 0) {
								f4 = MathHelper.sqrt_double((motionX * motionX) + (motionZ * motionZ));

								if (f4 > 0.0F) {
									movingobjectposition.entityHit.addVelocity(
											(motionX * knockbackStrength * 0.6000000238418579D) / f4, 0.1D,
											(motionZ * knockbackStrength * 0.6000000238418579D) / f4);
								}
							}

							if (shootingEntity instanceof EntityLivingBase) {
								EnchantmentHelper.applyThornEnchantments(entitylivingbase, shootingEntity);
								EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) shootingEntity,
										entitylivingbase);
							}

							if ((shootingEntity != null) && (movingobjectposition.entityHit != shootingEntity)
									&& (movingobjectposition.entityHit instanceof EntityPlayer)
									&& (shootingEntity instanceof EntityPlayerMP)) {
								((EntityPlayerMP) shootingEntity).playerNetServerHandler
										.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
							}
						}

						playSound("random.bowhit", 1.0F, 1.2F / ((rand.nextFloat() * 0.2F) + 0.9F));

						if (!(movingobjectposition.entityHit instanceof EntityEnderman)) {
							setDead();
						}
					} else {
						motionX *= -0.10000000149011612D;
						motionY *= -0.10000000149011612D;
						motionZ *= -0.10000000149011612D;
						rotationYaw += 180.0F;
						prevRotationYaw += 180.0F;
						ticksInAir = 0;
					}
				} else {
					BlockPos blockpos1 = movingobjectposition.getBlockPos();
					xTile = blockpos1.getX();
					yTile = blockpos1.getY();
					zTile = blockpos1.getZ();
					iblockstate = worldObj.getBlockState(blockpos1);
					inTile = iblockstate.getBlock();
					inData = inTile.getMetaFromState(iblockstate);
					motionX = ((float) (movingobjectposition.hitVec.xCoord - posX));
					motionY = ((float) (movingobjectposition.hitVec.yCoord - posY));
					motionZ = ((float) (movingobjectposition.hitVec.zCoord - posZ));
					f3 = MathHelper.sqrt_double((motionX * motionX) + (motionY * motionY) + (motionZ * motionZ));
					posX -= (motionX / f3) * 0.05000000074505806D;
					posY -= (motionY / f3) * 0.05000000074505806D;
					posZ -= (motionZ / f3) * 0.05000000074505806D;
					playSound("random.bowhit", 1.0F, 1.2F / ((rand.nextFloat() * 0.2F) + 0.9F));
					inGround = true;
					arrowShake = 7;
					setIsCritical(false);

					if (inTile.getMaterial() != Material.air) {
						inTile.onEntityCollidedWithBlock(worldObj, blockpos1, iblockstate, this);
					}
				}
			}

			if (getIsCritical()) {
				for (i = 0; i < 4; ++i) {
					worldObj.spawnParticle(EnumParticleTypes.CRIT, posX + ((motionX * i) / 4.0D),
							posY + ((motionY * i) / 4.0D), posZ + ((motionZ * i) / 4.0D), -motionX, -motionY + 0.2D,
							-motionZ, new int[0]);
				}
			}

			posX += motionX;
			posY += motionY;
			posZ += motionZ;
			f2 = MathHelper.sqrt_double((motionX * motionX) + (motionZ * motionZ));
			rotationYaw = (float) ((Math.atan2(motionX, motionZ) * 180.0D) / Math.PI);

			for (rotationPitch = (float) ((Math.atan2(motionY, f2) * 180.0D) / Math.PI); (rotationPitch
					- prevRotationPitch) < -180.0F; prevRotationPitch -= 360.0F) {
				;
			}

			while ((rotationPitch - prevRotationPitch) >= 180.0F) {
				prevRotationPitch += 360.0F;
			}

			while ((rotationYaw - prevRotationYaw) < -180.0F) {
				prevRotationYaw -= 360.0F;
			}

			while ((rotationYaw - prevRotationYaw) >= 180.0F) {
				prevRotationYaw += 360.0F;
			}

			rotationPitch = prevRotationPitch + ((rotationPitch - prevRotationPitch) * 0.2F);
			rotationYaw = prevRotationYaw + ((rotationYaw - prevRotationYaw) * 0.2F);
			f3 = 0.99F;
			f1 = 0.05F;

			if (isInWater()) {
				for (int l = 0; l < 4; ++l) {
					f4 = 0.25F;
					worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX - (motionX * f4), posY - (motionY * f4),
							posZ - (motionZ * f4), motionX, motionY, motionZ, new int[0]);
				}

				f3 = 0.6F;
			}

			if (isWet()) {
				extinguish();
			}

			motionX *= f3;
			motionY *= f3;
			motionZ *= f3;
			motionY -= f1;
			setPosition(posX, posY, posZ);
			doBlockCollisions();
		}
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund) {
		xTile = tagCompund.getShort("xTile");
		yTile = tagCompund.getShort("yTile");
		zTile = tagCompund.getShort("zTile");
		ticksInGround = tagCompund.getShort("life");

		if (tagCompund.hasKey("inTile", 8)) {
			inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
		} else {
			inTile = Block.getBlockById(tagCompund.getByte("inTile") & 255);
		}

		inData = tagCompund.getByte("inData") & 255;
		arrowShake = tagCompund.getByte("shake") & 255;
		inGround = tagCompund.getByte("inGround") == 1;

		if (tagCompund.hasKey("damage", 99)) {
			damage = tagCompund.getDouble("damage");
		}
	}

	public void setDamage(double p_70239_1_) {
		damage = p_70239_1_;
	}

	public void setIsCritical(boolean p_70243_1_) {
		byte b0 = dataWatcher.getWatchableObjectByte(16);

		if (p_70243_1_) {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 | 1)));
		} else {
			dataWatcher.updateObject(16, Byte.valueOf((byte) (b0 & -2)));
		}
	}

	public void setKnockbackStrength(int p_70240_1_) {
		knockbackStrength = p_70240_1_;
	}

	@Override
	public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
		float f2 = MathHelper.sqrt_double((x * x) + (y * y) + (z * z));
		x /= f2;
		y /= f2;
		z /= f2;
		x += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * inaccuracy;
		y += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * inaccuracy;
		z += rand.nextGaussian() * (rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * inaccuracy;
		x *= velocity;
		y *= velocity;
		z *= velocity;
		motionX = x;
		motionY = y;
		motionZ = z;
		float f3 = MathHelper.sqrt_double((x * x) + (z * z));
		prevRotationYaw = rotationYaw = (float) ((Math.atan2(x, z) * 180.0D) / Math.PI);
		prevRotationPitch = rotationPitch = (float) ((Math.atan2(y, f3) * 180.0D) / Math.PI);
		ticksInGround = 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {
		motionX = x;
		motionY = y;
		motionZ = z;

		if ((prevRotationPitch == 0.0F) && (prevRotationYaw == 0.0F)) {
			float f = MathHelper.sqrt_double((x * x) + (z * z));
			prevRotationYaw = rotationYaw = (float) ((Math.atan2(x, z) * 180.0D) / Math.PI);
			prevRotationPitch = rotationPitch = (float) ((Math.atan2(y, f) * 180.0D) / Math.PI);
			prevRotationPitch = rotationPitch;
			prevRotationYaw = rotationYaw;
			setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
			ticksInGround = 0;
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound) {
		tagCompound.setShort("xTile", (short) xTile);
		tagCompound.setShort("yTile", (short) yTile);
		tagCompound.setShort("zTile", (short) zTile);
		tagCompound.setShort("life", (short) ticksInGround);
		ResourceLocation resourcelocation = Block.blockRegistry.getNameForObject(inTile);
		tagCompound.setString("inTile", resourcelocation == null ? "" : resourcelocation.toString());
		tagCompound.setByte("inData", (byte) inData);
		tagCompound.setByte("shake", (byte) arrowShake);
		tagCompound.setByte("inGround", (byte) (inGround ? 1 : 0));
		tagCompound.setDouble("damage", damage);
	}
}