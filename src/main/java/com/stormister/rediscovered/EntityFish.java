package com.stormister.rediscovered;

import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityFish extends EntityWaterMob {
	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	// public boolean getCanSpawnHere()
	// {
	// return this.posY > 45.0D && this.posY < 63.0D && super.getCanSpawnHere();
	// }
	class AIMoveRandom extends EntityAIBase {
		private EntityFish field_179476_a = EntityFish.this;

		@Override
		public boolean shouldExecute() {
			return true;
		}

		@Override
		public void updateTask() {
			int i = field_179476_a.getAge();

			if (i > 100) {
				field_179476_a.func_175568_b(0.0F, 0.0F, 0.0F);
			} else if ((field_179476_a.getRNG().nextInt(50) == 0) || !field_179476_a.inWater
					|| !field_179476_a.func_175567_n()) {
				float f = field_179476_a.getRNG().nextFloat() * (float) Math.PI * 2.0F;
				float f1 = MathHelper.cos(f) * 0.2F;
				float f2 = -0.1F + (field_179476_a.getRNG().nextFloat() * 0.2F);
				float f3 = MathHelper.sin(f) * 0.2F;
				field_179476_a.func_175568_b(f1, f2, f3);
			}
		}
	}

	public float squidPitch;
	public float prevSquidPitch;
	public float squidYaw;
	public float prevSquidYaw;
	public float field_70867_h;

	public float field_70868_i;

	/** angle of the tentacles in radians */
	public float tentacleAngle = 0.0F;
	/** the last calculated angle of the tentacles in radians */
	public float lastTentacleAngle = 0.0F;
	private float randomMotionSpeed = 0.0F;
	private float field_70864_bA = 0.0F;
	private float field_70871_bB = 0.0F;
	private float randomMotionVecX = 0.0F;
	private float randomMotionVecY = 0.0F;

	private float randomMotionVecZ = 0.0F;

	public EntityFish(World par1World) {
		super(par1World);
		setSize(0.5F, 0.5F);
		field_70864_bA = (1.0F / (rand.nextFloat() + 1.0F)) * 0.2F;
		tasks.addTask(0, new EntityFish.AIMoveRandom());
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
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
		Item j = getDropItem();

		dropItem(j, 1);

	}

	public boolean func_175567_n() {
		return (randomMotionVecX != 0.0F) || (randomMotionVecY != 0.0F) || (randomMotionVecZ != 0.0F);
	}

	public void func_175568_b(float p_175568_1_, float p_175568_2_, float p_175568_3_) {
		randomMotionVecX = p_175568_1_;
		randomMotionVecY = p_175568_2_;
		randomMotionVecZ = p_175568_3_;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return null;
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	@Override
	protected Item getDropItem() {
		return isBurning() ? Items.cooked_fish : Items.fish;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return null;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return null;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte p_70103_1_) {
		super.handleStatusUpdate(p_70103_1_);
	}

	/**
	 * Checks if this entity is inside water (if inWater field is true as a
	 * result of handleWaterMovement() returning true)
	 */
	@Override
	public boolean isInWater() {
		return worldObj.handleMaterialAcceleration(getEntityBoundingBox().expand(0.0D, -0.6000000238418579D, 0.0D),
				Material.water, this);
	}

	/**
	 * Moves the entity based on the specified heading. Args: strafe, forward
	 */
	@Override
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
		moveEntity(motionX, motionY, motionZ);
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		prevSquidPitch = squidPitch;
		prevSquidYaw = squidYaw;
		field_70868_i = field_70867_h;
		lastTentacleAngle = tentacleAngle;
		field_70867_h += field_70864_bA;

		if (field_70867_h > ((float) Math.PI * 2F)) {
			field_70867_h -= ((float) Math.PI * 2F);

			if (rand.nextInt(10) == 0) {
				field_70864_bA = (1.0F / (rand.nextFloat() + 1.0F)) * 0.2F;
			}
		}

		if (isInWater()) {
			float f;

			if (field_70867_h < (float) Math.PI) {
				f = field_70867_h / (float) Math.PI;
				tentacleAngle = MathHelper.sin(f * f * (float) Math.PI) * (float) Math.PI * 0.25F;

				if (f > 0.75D) {
					randomMotionSpeed = 1.0F;
					field_70871_bB = 1.0F;
				} else {
					field_70871_bB *= 0.8F;
				}
			} else {
				tentacleAngle = 0.0F;
				randomMotionSpeed *= 0.9F;
				field_70871_bB *= 0.99F;
			}

			if (!worldObj.isRemote) {
				motionX = randomMotionVecX * randomMotionSpeed;
				motionY = randomMotionVecY * randomMotionSpeed;
				motionZ = randomMotionVecZ * randomMotionSpeed;
			}

			f = MathHelper.sqrt_double((motionX * motionX) + (motionZ * motionZ));
			renderYawOffset += (((-((float) Math.atan2(motionX, motionZ)) * 180.0F) / (float) Math.PI)
					- renderYawOffset) * 0.1F;
			rotationYaw = renderYawOffset;
			squidYaw += (float) Math.PI * field_70871_bB * 1.5F;
			squidPitch += (((-((float) Math.atan2(f, motionY)) * 180.0F) / (float) Math.PI) - squidPitch) * 0.1F;
		} else {
			tentacleAngle = MathHelper.abs(MathHelper.sin(field_70867_h)) * (float) Math.PI * 0.25F;

			if (!worldObj.isRemote) {
				motionX = 0.0D;
				motionY -= 0.08D;
				motionY *= 0.9800000190734863D;
				motionZ = 0.0D;
			}

			squidPitch = (float) (squidPitch + ((-90.0F - squidPitch) * 0.02D));
		}
	}
}
