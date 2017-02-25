package com.stormister.rediscovered.render;

import com.stormister.rediscovered.Rediscovered;
import com.stormister.rediscovered.entity.EntityGoodDragon;
import com.stormister.rediscovered.models.ModelRedDragon;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderRedDragon extends RenderLiving<EntityGoodDragon> {
	private static final ResourceLocation enderDragonExplodingTextures = new ResourceLocation(
			"textures/entity/enderdragon/dragon_exploding.png");
	private static final ResourceLocation enderDragonCrystalBeamTextures = new ResourceLocation(
			"textures/entity/endercrystal/endercrystal_beam.png");
	private static final ResourceLocation field_110845_h = new ResourceLocation(
			Rediscovered.modid + ":" + "textures/models/reddragon/red_eyes.png");
	private static final ResourceLocation enderDragonTextures = new ResourceLocation(
			Rediscovered.modid + ":" + "textures/models/reddragon/red.png");

	/** An instance of the dragon model in RenderDragon */
	protected ModelRedDragon modelDragon;

	public RenderRedDragon(RenderManager p_i46183_1_) {
		super(p_i46183_1_, new ModelRedDragon(0.0F), 0.5F);
		modelDragon = (ModelRedDragon) mainModel;
		// this.setRenderPassModel(this.mainModel);
	}

	@Override
	public void doRender(EntityGoodDragon entity, double x, double y, double z, float entityYaw, float partialTicks) {
		// BossStatus.setBossStatus(entity, false);
		super.doRender(entity, x, y, z, entityYaw, partialTicks);

		if (entity.healingEnderCrystal != null) {
			drawRechargeRay(entity, x, y, z, partialTicks);
		}
	}

	protected void drawRechargeRay(EntityGoodDragon dragon, double p_180574_2_, double p_180574_4_, double p_180574_6_,
			float p_180574_8_) {
		float f = dragon.healingEnderCrystal.innerRotation + p_180574_8_;
		float f1 = (MathHelper.sin(f * 0.2F) / 2.0F) + 0.5F;
		f1 = ((f1 * f1) + f1) * 0.2F;
		float f2 = (float) (dragon.healingEnderCrystal.posX - dragon.posX
				- ((dragon.prevPosX - dragon.posX) * (1.0F - p_180574_8_)));
		float f3 = (float) ((f1 + dragon.healingEnderCrystal.posY) - 1.0D - dragon.posY
				- ((dragon.prevPosY - dragon.posY) * (1.0F - p_180574_8_)));
		float f4 = (float) (dragon.healingEnderCrystal.posZ - dragon.posZ
				- ((dragon.prevPosZ - dragon.posZ) * (1.0F - p_180574_8_)));
		float f5 = MathHelper.sqrt_float((f2 * f2) + (f4 * f4));
		float f6 = MathHelper.sqrt_float((f2 * f2) + (f3 * f3) + (f4 * f4));
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) p_180574_2_, (float) p_180574_4_ + 2.0F, (float) p_180574_6_);
		GlStateManager.rotate((((float) (-Math.atan2(f4, f2)) * 180.0F) / (float) Math.PI) - 90.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate((((float) (-Math.atan2(f5, f3)) * 180.0F) / (float) Math.PI) - 90.0F, 1.0F, 0.0F, 0.0F);
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableCull();
		bindTexture(enderDragonCrystalBeamTextures);
		GlStateManager.shadeModel(7425);
		float f7 = 0.0F - ((dragon.ticksExisted + p_180574_8_) * 0.01F);
		float f8 = (MathHelper.sqrt_float((f2 * f2) + (f3 * f3) + (f4 * f4)) / 32.0F)
				- ((dragon.ticksExisted + p_180574_8_) * 0.01F);
		worldrenderer.begin(5, DefaultVertexFormats.POSITION_TEX_COLOR);
		for (int j = 0; j <= 8; ++j) {
			float f9 = MathHelper.sin(((j % 8) * (float) Math.PI * 2.0F) / 8.0F) * 0.75F;
			float f10 = MathHelper.cos(((j % 8) * (float) Math.PI * 2.0F) / 8.0F) * 0.75F;
			float f11 = ((j % 8) * 1.0F) / 8.0F;
			worldrenderer.pos(f9 * 0.2F, f10 * 0.2F, 0.0D).tex(f11, f8).color(0, 0, 0, 255).endVertex();
			worldrenderer.pos(f9, f10, f6).tex(f11, f7).color(255, 255, 255, 255).endVertex();
		}

		tessellator.draw();
		GlStateManager.enableCull();
		GlStateManager.shadeModel(7424);
		RenderHelper.enableStandardItemLighting();
		GlStateManager.popMatrix();
	}

	/**
	 * Used to rotate the dragon as a whole in RenderDragon. It's called in the
	 * rotateCorpse method.
	 */
	protected void func_180575_a(EntityGoodDragon p_180575_1_, float p_180575_2_, float p_180575_3_,
			float p_180575_4_) {
		float f3 = (float) p_180575_1_.getMovementOffsets(7, p_180575_4_)[0];
		float f4 = (float) (p_180575_1_.getMovementOffsets(5, p_180575_4_)[1]
				- p_180575_1_.getMovementOffsets(10, p_180575_4_)[1]);
		GlStateManager.rotate(-f3, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(f4 * 10.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.translate(0.0F, 0.0F, 1.0F);

		if (p_180575_1_.deathTime > 0) {
			float f5 = (((p_180575_1_.deathTime + p_180575_4_) - 1.0F) / 20.0F) * 1.6F;
			f5 = MathHelper.sqrt_float(f5);

			if (f5 > 1.0F) {
				f5 = 1.0F;
			}

			GlStateManager.rotate(f5 * getDeathMaxRotation(p_180575_1_), 0.0F, 0.0F, 1.0F);
		}
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityGoodDragon entity) {
		return enderDragonTextures;
	}

	@Override
	protected void renderModel(EntityGoodDragon p_77036_1_, float p_77036_2_, float p_77036_3_, float p_77036_4_,
			float p_77036_5_, float p_77036_6_, float p_77036_7_) {
		if (p_77036_1_.deathTicks > 0) {
			float f6 = p_77036_1_.deathTicks / 200.0F;
			GlStateManager.depthFunc(515);
			GlStateManager.enableAlpha();
			GlStateManager.alphaFunc(516, f6);
			bindTexture(enderDragonExplodingTextures);
			mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
			GlStateManager.alphaFunc(516, 0.1F);
			GlStateManager.depthFunc(514);
		}

		bindEntityTexture(p_77036_1_);
		mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);

		if (p_77036_1_.hurtTime > 0) {
			GlStateManager.depthFunc(514);
			GlStateManager.disableTexture2D();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
			GlStateManager.color(1.0F, 0.0F, 0.0F, 0.5F);
			mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
			GlStateManager.depthFunc(515);
		}
	}

	@Override
	protected void rotateCorpse(EntityGoodDragon p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
		func_180575_a(p_77043_1_, p_77043_2_, p_77043_3_, p_77043_4_);
	}
}
