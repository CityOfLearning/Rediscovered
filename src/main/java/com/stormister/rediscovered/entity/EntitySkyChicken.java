package com.stormister.rediscovered.entity;

import org.lwjgl.input.Keyboard;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntitySkyChicken extends EntityChicken {
	private static String togglekey;
	private static int itogglekey;

	public static void assignToggleButton(String s) {
		togglekey = s;
		itogglekey = Keyboard.getKeyIndex(togglekey);
	}

	/** AI task for player control. */
	private final EntityAIControlledByPlayer aiControlledByPlayer;

	public EntitySkyChicken(World par1World) {
		super(par1World);
		togglekey = "SPACE";
		itogglekey = Keyboard.getKeyIndex(togglekey);
		tasks.addTask(3, aiControlledByPlayer = new EntityAIControlledByPlayer(this, 1.0F));
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
	 * Return the AI task for player control.
	 */
	public EntityAIControlledByPlayer getAIControlledByPlayer() {
		return aiControlledByPlayer;
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
	 * Called frequently so the entity can update its state every tick as
	 * required. For example, zombies and skeletons use this to react to
	 * sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();

		if (Keyboard.getEventKeyState() && (Keyboard.getEventKey() == itogglekey) && (riddenByEntity != null)) {
			motionY = 0.05D;
			motionX *= 1.05;
			motionZ *= 1.05;
			super.moveEntity(motionX, motionY, motionZ);
			super.jump();
		}
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
}
