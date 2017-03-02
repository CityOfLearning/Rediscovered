package com.stormister.rediscovered.render;

import org.lwjgl.opengl.GL11;

import com.stormister.rediscovered.Rediscovered;
import com.stormister.rediscovered.models.ModelPigmanMob;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPigman extends RenderBiped {
	private static final ResourceLocation PIGMAN_TEXTURE = new ResourceLocation(
			Rediscovered.modid + ":" + "textures/models/Pigman.png");

	public RenderPigman(RenderManager renderManagerIn, ModelBiped modelBipedIn, float shadowSize) {
		super(renderManagerIn, modelBipedIn, shadowSize);
	}
	
	public RenderPigman(RenderManager renderManagerIn, ModelPigmanMob modelBipedIn, float shadowSize) {
		super(renderManagerIn, modelBipedIn, shadowSize);
	}

	public void transformHeldFull3DItemLayer() {
		GL11.glTranslatef(0.09375F, 0.1875F, 0.0F);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called
	 * unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityLiving par1Entity) {
		return PIGMAN_TEXTURE;
	}
}
