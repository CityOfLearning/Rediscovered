package com.stormister.rediscovered.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelPigmanMob extends ModelBiped {
	public ModelPigmanMob() {
		this(0.0F);
	}

	public ModelPigmanMob(float par1) {
		super(par1, 0.0F, 64, 32);
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third
	 * float params here are the same second and third as in the
	 * setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4) {
		ItemStack itemstack = par1EntityLivingBase.getHeldItem();

		if ((itemstack != null) && (itemstack.getItem() == Items.bow)) {
			aimedBow = true;
		}

		super.setLivingAnimations(par1EntityLivingBase, par2, par3, par4);
	}
}
