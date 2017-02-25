package com.stormister.rediscovered.render;

import org.lwjgl.opengl.GL11;

import com.stormister.rediscovered.entity.EntityZombieHorse;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderZombieHorse extends RenderLiving<EntityZombieHorse> {
	private static final ResourceLocation field_110871_a = new ResourceLocation(
			"textures/entity/horse/horse_zombie.png");

	public RenderZombieHorse(RenderManager p_i46170_1_, ModelBase par1ModelBase, float par2) {
		super(p_i46170_1_, par1ModelBase, par2);
	}

	@Override
	public void doRender(EntityZombieHorse par1EntityLiving, double par2, double par4, double par6, float par8,
			float par9) {
		super.doRender(par1EntityLiving, par2, par4, par6, par8, par9);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityZombieHorse par1Entity) {
		return field_110871_a;
	}

	protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
		preRenderScale((EntityZombieHorse) par1EntityLiving, par2);
	}

	// Added these two functions, to allow for scaling.
	protected void preRenderScale(EntityZombieHorse par1EntityExampleH, float par2) {
		// These values are x,y,z scale. Where 1.0F = 100%
		GL11.glScalef(1.0F, 1.0F, 1.0F);
	}
}