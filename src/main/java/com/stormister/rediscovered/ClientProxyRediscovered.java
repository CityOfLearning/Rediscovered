package com.stormister.rediscovered;

import com.stormister.rediscovered.blocks.tiles.TileEntityLectern;
import com.stormister.rediscovered.blocks.tiles.TileEntityLecternOpen;
import com.stormister.rediscovered.models.ModelFish;
import com.stormister.rediscovered.models.ModelRangedPigman;
import com.stormister.rediscovered.models.ModelScarecrow;
import com.stormister.rediscovered.models.ModelSkeletonHorse;
import com.stormister.rediscovered.models.ModelSkyChicken;
import com.stormister.rediscovered.models.ModelZombieHorse;
import com.stormister.rediscovered.render.RenderFishMob;
import com.stormister.rediscovered.render.RenderGiant;
import com.stormister.rediscovered.render.RenderGreenVillager;
import com.stormister.rediscovered.render.RenderParrow;
import com.stormister.rediscovered.render.RenderPigman;
import com.stormister.rediscovered.render.RenderRedDragon;
import com.stormister.rediscovered.render.RenderRediscoveredPotion;
import com.stormister.rediscovered.render.RenderScarecrow;
import com.stormister.rediscovered.render.RenderSkeletonHorse;
import com.stormister.rediscovered.render.RenderSkyChicken;
import com.stormister.rediscovered.render.RenderTileEntityLectern;
import com.stormister.rediscovered.render.RenderTileEntityLecternOpen;
import com.stormister.rediscovered.render.RenderZombieHorse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ClientProxyRediscovered extends CommonProxyRediscovered {
	@Override
	public void registerRenderThings() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLectern.class, new RenderTileEntityLectern());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLecternOpen.class, new RenderTileEntityLecternOpen());

		FMLCommonHandler.instance().bus().register(mod_Rediscovered.c);

		ModelBakery.addVariantName(mod_Rediscovered.RediscoveredPotion, "rediscovered:RediscoveredPotion_Nausea",
				"rediscovered:RediscoveredPotion_Blindness", "rediscovered:RediscoveredPotion_Dullness",
				"rediscovered:RediscoveredPotion_NauseaSplash", "rediscovered:RediscoveredPotion_BlindnessSplash",
				"rediscovered:RediscoveredPotion_DullnessSplash");

		RenderingRegistry.registerEntityRenderingHandler(com.stormister.rediscovered.entity.EntityPigman.class,
				new RenderPigman(Minecraft.getMinecraft().getRenderManager(), new ModelBiped(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(com.stormister.rediscovered.entity.EntityMeleePigman.class,
				new RenderPigman(Minecraft.getMinecraft().getRenderManager(), new ModelBiped(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(com.stormister.rediscovered.entity.EntityRangedPigman.class,
				new RenderPigman(Minecraft.getMinecraft().getRenderManager(), new ModelRangedPigman(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(com.stormister.rediscovered.entity.EntityGreenVillager.class,
				new RenderGreenVillager(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(com.stormister.rediscovered.entity.EntitySkyChicken.class,
				new RenderSkyChicken(Minecraft.getMinecraft().getRenderManager(), new ModelSkyChicken(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(com.stormister.rediscovered.entity.EntityFish.class,
				new RenderFishMob(Minecraft.getMinecraft().getRenderManager(), new ModelFish(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(com.stormister.rediscovered.entity.EntityZombieHorse.class,
				new RenderZombieHorse(Minecraft.getMinecraft().getRenderManager(), new ModelZombieHorse(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(com.stormister.rediscovered.entity.EntitySkeletonHorse.class,
				new RenderSkeletonHorse(Minecraft.getMinecraft().getRenderManager(), new ModelSkeletonHorse(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(com.stormister.rediscovered.entity.EntityGoodDragon.class,
				new RenderRedDragon(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(com.stormister.rediscovered.entity.EntityParrow.class,
				new RenderParrow(Minecraft.getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(com.stormister.rediscovered.entity.EntityGiant.class,
				new RenderGiant(Minecraft.getMinecraft().getRenderManager(), new ModelZombie(), 0.5F, 6.0F));
		RenderingRegistry.registerEntityRenderingHandler(com.stormister.rediscovered.entity.EntityScarecrow.class,
				new RenderScarecrow(Minecraft.getMinecraft().getRenderManager(), new ModelScarecrow(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(com.stormister.rediscovered.entity.EntityRediscoveredPotion.class,
				new RenderRediscoveredPotion(Minecraft.getMinecraft().getRenderManager(),
						Minecraft.getMinecraft().getRenderItem()));
	}

	@Override
	public void registerTileEntitySpecialRenderer() {

	}

}
