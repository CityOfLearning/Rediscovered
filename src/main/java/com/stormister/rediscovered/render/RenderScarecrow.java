package com.stormister.rediscovered.render;

import com.stormister.rediscovered.Rediscovered;
import com.stormister.rediscovered.entity.EntityScarecrow;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderScarecrow extends RenderLiving<EntityScarecrow> {
	private static final ResourceLocation field_110920_a = new ResourceLocation(
			Rediscovered.modid + ":" + "textures/models/Scarecrow.png");

	public RenderScarecrow(RenderManager p_i46173_1_, ModelBase par1ModelBase, float par2) {
		super(p_i46173_1_, par1ModelBase, par2);
	}

	@Override
	public void doRender(EntityScarecrow par1Entity, double par2, double par4, double par6, float par8, float par9) {
		super.doRender(par1Entity, par2, par4, par6, par8, par9);
	}

	@Override
	protected float getDeathMaxRotation(EntityScarecrow par1EntityLiving) {
		return 0.0F;
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityScarecrow par1Entity) {
		return field_110920_a;
	}
}
