package com.stormister.rediscovered;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGreenVillager extends RenderLiving {
	private static final ResourceLocation villagerTextures = new ResourceLocation(
			"textures/entity/villager/villager.png");

	public RenderGreenVillager(RenderManager p_i46132_1_) {
		super(p_i46132_1_, new ModelVillager(0.0F), 0.5F);
		addLayer(new LayerCustomHead(func_177134_g().villagerHead));
	}

	public ModelVillager func_177134_g() {
		return (ModelVillager) super.getMainModel();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return this.getEntityTexture((EntityGreenVillager) entity);
	}

	protected ResourceLocation getEntityTexture(EntityGreenVillager entity) {

		return net.minecraftforge.fml.common.registry.VillagerRegistry.getVillagerSkin(entity.getProfession(),
				villagerTextures);

	}

	@Override
	public ModelBase getMainModel() {
		return func_177134_g();
	}

	protected void preRenderCallback(EntityGreenVillager p_77041_1_, float p_77041_2_) {
		float f1 = 0.9375F;

		if (p_77041_1_.getGrowingAge() < 0) {
			f1 = (float) (f1 * 0.5D);
			shadowSize = 0.25F;
		} else {
			shadowSize = 0.5F;
		}

		GlStateManager.scale(f1, f1, f1);
	}

	@Override
	protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
		this.preRenderCallback((EntityGreenVillager) p_77041_1_, p_77041_2_);
	}
}