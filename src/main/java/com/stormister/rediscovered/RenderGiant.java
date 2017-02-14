package com.stormister.rediscovered;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGiant extends RenderLiving {
	private static final ResourceLocation zombieTextures = new ResourceLocation("textures/entity/zombie/zombie.png");

	/** Scale of the model to use */
	private float scale;

	public RenderGiant(RenderManager p_i46173_1_, ModelBase par1ModelBase, float par2, float par3) {
		super(p_i46173_1_, par1ModelBase, par2 * par3);
		scale = par3;
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return getZombieTextures((EntityGiant) par1Entity);
	}

	protected ResourceLocation getZombieTextures(EntityGiant par1EntityGiantZombie) {
		return zombieTextures;
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before
	 * the model is rendered. Args: entityLiving, partialTickTime
	 */
	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2) {
		preRenderScale((EntityGiant) par1EntityLivingBase, par2);
	}

	/**
	 * Applies the scale to the transform matrix
	 */
	protected void preRenderScale(EntityGiant par1EntityGiantZombie, float par2) {
		GL11.glScalef(scale, scale, scale);
	}
}
