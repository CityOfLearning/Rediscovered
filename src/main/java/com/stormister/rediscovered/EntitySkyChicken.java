package com.stormister.rediscovered;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySkyChicken extends EntityCreature {
	private static String togglekey;
	private static int itogglekey;

	public static void assignToggleButton(String s) {
		togglekey = s;
		itogglekey = Keyboard.getKeyIndex(togglekey);
	}

	public float field_70886_e;
	public float destPos;
	@SideOnly(Side.CLIENT)
	private double velocityX;
	@SideOnly(Side.CLIENT)
	private double velocityY;
	@SideOnly(Side.CLIENT)
	private double velocityZ;

	public float field_70884_g;

	public float field_70888_h;

	public float field_70889_i = 1.0F;
	/** The time until the next egg is spawned. */
	public int timeUntilNextEgg;

	/** AI task for player control. */
	private final EntityAIControlledByPlayer aiControlledByPlayer;

	public EntitySkyChicken(World par1World) {
		super(par1World);
		setSize(0.3F, 0.7F);
		timeUntilNextEgg = rand.nextInt(6000) + 6000;
		togglekey = "SPACE";
		itogglekey = Keyboard.getKeyIndex(togglekey);
		tasks.addTask(0, new EntityAISwimming(this));
		// this.tasks.addTask(2, new EntityAIMate(this, 1.0D));
		tasks.addTask(3, aiControlledByPlayer = new EntityAIControlledByPlayer(this, 1.0F));
		tasks.addTask(4, new EntityAITempt(this, 1.2D, Items.wheat_seeds, false));
		// this.tasks.addTask(5, new EntityAIFollowParent(this, 1.1D));
		tasks.addTask(6, new EntityAIWander(this, 1.0D));
		tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		tasks.addTask(8, new EntityAILookIdle(this));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	/**
	 * returns true if all the conditions for steering the entity are met. For
	 * pigs, this is true if it is being ridden by a player and the player is
	 * holding a carrot-on-a-stick
	 */
	@Override
	public boolean canBeSteered() {
		ItemStack itemstack = ((EntityPlayer) riddenByEntity).getHeldItem();
		return (itemstack != null) && (itemstack.getItem() == Items.wheat_seeds);
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity
	 * has recently been hit by a player. @param par2 - Level of Looting used to
	 * kill this mob.
	 */
	@Override
	protected void dropFewItems(boolean par1, int par2) {
		int j = rand.nextInt(3) + rand.nextInt(1 + par2);

		for (int k = 0; k < j; ++k) {
			dropItem(Items.feather, 1);
		}

		if (isBurning()) {
			dropItem(Items.cooked_chicken, 1);
		} else {
			dropItem(Items.chicken, 1);
		}
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	public void fall(float distance, float damageMultiplier) {
	}

	/**
	 * Return the AI task for player control.
	 */
	public EntityAIControlledByPlayer getAIControlledByPlayer() {
		return aiControlledByPlayer;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound() {
		return "mob.chicken.hurt";
	}

	/**
	 * Returns the item ID for the item the mob drops on death.
	 */
	@Override
	protected Item getDropItem() {
		return Items.feather;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound() {
		return "mob.chicken.hurt";
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound() {
		return "mob.chicken.say";
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow,
	 * gets into the saddle on a pig.
	 */
	@Override
	public boolean interact(EntityPlayer par1EntityPlayer) {
		if (super.interact(par1EntityPlayer)) {
			return true;
		} else if (!worldObj.isRemote && ((riddenByEntity == null) || (riddenByEntity == par1EntityPlayer))) {
			par1EntityPlayer.mountEntity(this);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return true;
	}

	/**
	 * Checks if the parameter is an item which this animal can be fed to breed
	 * it (wheat, carrots or seeds depending on the animal type)
	 */
	public boolean isBreedingItem(ItemStack par1ItemStack) {
		return (par1ItemStack != null) && (par1ItemStack.getItem() instanceof ItemSeeds);
	}

	/**
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		field_70888_h = field_70886_e;
		field_70884_g = destPos;
		destPos = (float) (destPos + ((onGround ? -1 : 4) * 0.3D));

		if (destPos < 0.0F) {
			destPos = 0.0F;
		}

		if (destPos > 1.0F) {
			destPos = 1.0F;
		}

		// if(this.riddenByEntity != null){
		// this.motionX = this.riddenByEntity.motionX*6;
		// this.motionZ = this.riddenByEntity.motionZ*6;
		// super.moveEntity(motionX, motionY, motionZ);
		// }

		if (!onGround && (field_70889_i < 1.0F)) {
			field_70889_i = 1.0F;
		}

		field_70889_i = (float) (field_70889_i * 0.9D);

		if (Keyboard.getEventKeyState() && (Keyboard.getEventKey() == itogglekey) && (riddenByEntity != null)) {
			motionY = 0.05D;
			motionX *= 1.05;
			motionZ *= 1.05;
			super.moveEntity(motionX, motionY, motionZ);
			super.jump();
		}

		if (!onGround && (motionY < 0.0D)) {
			motionY *= 0.6D;
		}

		field_70886_e += field_70889_i * 2.0F;

		if (!isChild() && !worldObj.isRemote && (--timeUntilNextEgg <= 0)) {
			playSound("mob.chicken.plop", 1.0F, ((rand.nextFloat() - rand.nextFloat()) * 0.2F) + 1.0F);
			dropItem(Items.egg, 1);
			timeUntilNextEgg = rand.nextInt(6000) + 6000;
		}
	}

	/**
	 * Plays step sound at given x, y, z for the entity
	 */
	protected void playStepSound(int par1, int par2, int par3, int par4) {
		playSound("mob.chicken.step", 0.15F, 1.0F);
	}

	/**
	 * This function is used when two same-species animals in 'love mode' breed
	 * to generate the new baby animal.
	 */
	public EntitySkyChicken spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
		return new EntitySkyChicken(worldObj);
	}

	@Override
	protected void updateAITasks() {
		super.updateAITasks();
	}

	// public EntityAgeable createChild(EntityAgeable par1EntityAgeable)
	// {
	// return this.spawnBabyAnimal(par1EntityAgeable);
	// }
}
