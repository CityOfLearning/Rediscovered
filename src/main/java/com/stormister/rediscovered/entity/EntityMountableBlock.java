package com.stormister.rediscovered.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/*ITOS:
 *This class acts as a bridge between the mountable block and the player.
 *This entity is what the player actually mounts instead of the block.
 *An entity of this class is created by the mountable block upon activation
 *and is killed when it's no longer used.
*/

public class EntityMountableBlock extends Entity {

	// These variables keep track of the block that created the entity.
	private int orgBlockPosX;
	private int orgBlockPosY;
	private int orgBlockPosZ;
	protected IBlockState orgBlockID;
	protected float blahx, blahy, blahz;

	public EntityMountableBlock(World world) {
		super(world);
		noClip = true;
		preventEntitySpawning = true;
		width = 0F;
		height = 0F;
	}

	public EntityMountableBlock(World world, double d, double d1, double d2) {
		super(world);
		noClip = true;
		preventEntitySpawning = true;
		width = 0F;
		height = 0F;
		setPosition(d, d1, d2);
	}

	// This constructor is called by the mountable block.
	public EntityMountableBlock(World world, EntityPlayer entityplayer, int i, int j, int k, float mountingX,
			float mountingY, float mountingZ) {
		super(world);
		noClip = true;
		preventEntitySpawning = true;
		width = 0.0F;
		height = 0.0F;

		setOrgBlockPosX(i);
		setOrgBlockPosY(j);
		setOrgBlockPosZ(k);
		orgBlockID = world.getBlockState(new BlockPos(i, j, k));

		blahx = mountingX;
		blahy = mountingY;
		blahz = mountingZ;
		setPosition(mountingX, mountingY, mountingZ);
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	// The following methods are required by the Entity class but I don't know
	// what they are for.
	@Override
	public void entityInit() {
	}

	// This method handles mounting and dismounting.
	public boolean interact(EntityPlayer entityplayer) {
		if ((riddenByEntity != null) && (riddenByEntity instanceof EntityPlayer) && (riddenByEntity != entityplayer)) {
			return true;
		} else {
			if (!worldObj.isRemote) {
				entityplayer.mountEntity(this);
			}
			return true;
		}
	}

	// This method is mostly a simplified version of the one in Entity but it
	// also deletes unused EMBs.
	@Override
	public void onEntityUpdate() {
		worldObj.theProfiler.startSection("entityBaseTick");
		if ((riddenByEntity == null) || riddenByEntity.isDead) {
			setDead();
		}
		if ((posY == Math.floor(posY)) && !Double.isInfinite(posY)) {
			posY -= 0.5F;
		}
		ticksExisted++;
		worldObj.theProfiler.endSection();
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
	}

	public int getOrgBlockPosZ() {
		return orgBlockPosZ;
	}

	public void setOrgBlockPosZ(int orgBlockPosZ) {
		this.orgBlockPosZ = orgBlockPosZ;
	}

	public int getOrgBlockPosX() {
		return orgBlockPosX;
	}

	public void setOrgBlockPosX(int orgBlockPosX) {
		this.orgBlockPosX = orgBlockPosX;
	}

	public int getOrgBlockPosY() {
		return orgBlockPosY;
	}

	public void setOrgBlockPosY(int orgBlockPosY) {
		this.orgBlockPosY = orgBlockPosY;
	}

}
