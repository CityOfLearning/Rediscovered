package com.stormister.rediscovered.render;

import com.stormister.rediscovered.mod_Rediscovered;
import com.stormister.rediscovered.entity.EntityFish;
import com.stormister.rediscovered.models.ModelFish;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderFishMob extends RenderLiving {
	private static final ResourceLocation field_110871_a = new ResourceLocation(
			mod_Rediscovered.modid + ":" + "textures/models/Fish.png");

	public RenderFishMob(RenderManager p_i46173_1_, ModelFish modelFish, float f) {
		super(p_i46173_1_, new ModelFish(), 0.3F);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method,
	 * always casting down its argument and then handing it off to a worker
	 * function which does the actual work. In all probabilty, the class Render
	 * is generic (Render<T extends Entity) and this method has signature public
	 * void doRender(T entity, double d, double d1, double d2, float f, float
	 * f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
		renderFishMob((EntityFish) par1Entity, par2, par4, par6, par8, par9);
	}

	@Override
	public void doRender(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
		renderFishMob((EntityFish) par1EntityLiving, par2, par4, par6, par8, par9);
	}

	protected float getDeathMaxRotation(EntityLiving par1EntityLiving) {
		return getFishDeathRotation((EntityFish) par1EntityLiving);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity) {
		return getSquidTextures((EntityFish) par1Entity);
	}

	/**
	 * Return the silverfish's maximum death rotation.
	 */
	protected float getFishDeathRotation(EntityFish par1EntitySilverfish) {
		return 180.0F;
	}

	protected ResourceLocation getSquidTextures(EntityFish par1EntitySquid) {
		return field_110871_a;
	}

	/**
	 * Renders the silverfish.
	 */
	public void renderFishMob(EntityFish par1EntitySilverfish, double par2, double par4, double par6, float par8,
			float par9) {
		super.doRender(par1EntitySilverfish, par2, par4, par6, par8, par9);
	}

	/**
	 * Disallows the silverfish to render the renderPassModel.
	 */
	protected int shouldFishRenderPass(EntityFish par1EntitySilverfish, int par2, float par3) {
		return -1;
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
		return shouldFishRenderPass((EntityFish) par1EntityLiving, par2, par3);
	}
}
