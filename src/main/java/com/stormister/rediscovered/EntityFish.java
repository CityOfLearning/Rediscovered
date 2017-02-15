package com.stormister.rediscovered.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

	static class AIMoveRandom extends EntityAIBase {
		private EntityFish fish;

		public AIMoveRandom(EntityFish fish) {
			this.fish = fish;
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		@Override
		public boolean shouldExecute() {
			return true;
		}

		/**
		 * Updates the task
		 */
		@Override
		public void updateTask() {
			int i = fish.getAge();

			if (i > 100) {
				fish.func_175568_b(0.0F, 0.0F, 0.0F);
			} else if ((fish.getRNG().nextInt(50) == 0) || !fish.inWater || !fish.func_175567_n()) {
				float f = fish.getRNG().nextFloat() * (float) Math.PI * 2.0F;
				float f1 = MathHelper.cos(f) * 0.2F;
				float f2 = -0.1F + (fish.getRNG().nextFloat() * 0.2F);
				float f3 = MathHelper.sin(f) * 0.2F;
				fish.func_175568_b(f1, f2, f3);
			}
		}
	}

	public float squidPitch;
	public float prevSquidPitch;
	public float squidYaw;
	public float prevSquidYaw;
	/**
	 * appears to be rotation in radians; we already have pitch & yaw, so this
	 * completes the triumvirate.
	 */
	public float squidRotation;
	/** previous squidRotation in radians */
	public float prevSquidRotation;
	/** angle of the tentacles in radians */
	public float tentacleAngle;
	/** the last calculated angle of the tentacles in radians */
	public float lastTentacleAngle;
	private float randomMotionSpeed;
	/** change in squidRotation in radians. */
	private float rotationVelocity;
	private float field_70871_bB;
	private float randomMotionVecX;
	private float randomMotionVecY;
	private float randomMotionVecZ;

	public EntityFish(World worldIn) {
		super(worldIn);
		setSize(1F, 1F);
		rand.setSeed(1 + getEntityId());
		rotationVelocity = (1.0F / (rand.nextFloat() + 1.0F)) * 0.2F;
		tasks.addTask(0, new EntityFish.AIMoveRandom(this));
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
		entityDropItem(new ItemStack(isBurning() ? Items.cooked_fish : Items.fish), 0.0F);
	}

	public boolean func_175567_n() {
		return (randomMotionVecX != 0.0F) || (randomMotionVecY != 0.0F) || (randomMotionVecZ != 0.0F);
	}

	public void func_175568_b(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn) {
		randomMotionVecX = randomMotionVecXIn;
		randomMotionVecY = randomMotionVecYIn;
		randomMotionVecZ = randomMotionVecZIn;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this
	 * entity.
	 */
	@Override
	public boolean getCanSpawnHere() {
		return (posY > 45.0D) && (posY < worldObj.getSeaLevel()) && super.getCanSpawnHere();
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return null;
	}

	@Override
	protected Item getDropItem() {
		return isBurning() ? Items.cooked_fish : Items.fish;
	}

	@Override
	public float getEyeHeight() {
		return height * 0.5F;
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
	public void handleStatusUpdate(byte id) {
		if (id == 19) {
			squidRotation = 0.0F;
		} else {
			super.handleStatusUpdate(id);
		}
	}

	/**
	 * Checks if this entity is inside water (if inWater field is true as a
	 * result of handleWaterMovement() returning true)
	 */
	@Override
	public boolean isInWater() {
		return worldObj.handleMaterialAcceleration(getEntityBoundingBox().offset(0, 0.5F, 0),
				Material.water, this);
	}

	/**
	 * Moves the entity based on the specified heading. Args: strafe, forward
	 */
	@Override
	public void moveEntityWithHeading(float strafe, float forward) {
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
		prevSquidRotation = squidRotation;
		lastTentacleAngle = tentacleAngle;
		squidRotation += rotationVelocity;

		if (squidRotation > (Math.PI * 2D)) {
			if (worldObj.isRemote) {
				squidRotation = ((float) Math.PI * 2F);
			} else {
				squidRotation = (float) (squidRotation - (Math.PI * 2D));

				if (rand.nextInt(10) == 0) {
					rotationVelocity = (1.0F / (rand.nextFloat() + 1.0F)) * 0.2F;
				}

				worldObj.setEntityState(this, (byte) 19);
			}
		}

		if (inWater) {
			if (squidRotation < (float) Math.PI) {
				float f = squidRotation / (float) Math.PI;
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

			float f1 = MathHelper.sqrt_double((motionX * motionX) + (motionZ * motionZ));
			renderYawOffset += (((-((float) MathHelper.atan2(motionX, motionZ)) * 180.0F) / (float) Math.PI)
					- renderYawOffset) * 0.1F;
			rotationYaw = renderYawOffset;
			squidYaw = (float) (squidYaw + (Math.PI * field_70871_bB * 1.5D));
			squidPitch += (((-((float) MathHelper.atan2(f1, motionY)) * 180.0F) / (float) Math.PI) - squidPitch) * 0.1F;
		} else {
			tentacleAngle = MathHelper.abs(MathHelper.sin(squidRotation)) * (float) Math.PI * 0.25F;

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
