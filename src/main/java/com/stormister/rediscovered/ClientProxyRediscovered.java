package com.stormister.rediscovered;

import com.stormister.rediscovered.blocks.tiles.TileEntityLectern;
import com.stormister.rediscovered.blocks.tiles.TileEntityLecternOpen;
import com.stormister.rediscovered.entity.EntityFish;
import com.stormister.rediscovered.entity.EntityGiant;
import com.stormister.rediscovered.entity.EntityGoodDragon;
import com.stormister.rediscovered.entity.EntityGreenVillager;
import com.stormister.rediscovered.entity.EntityParrow;
import com.stormister.rediscovered.entity.EntityPigmanMob;
import com.stormister.rediscovered.entity.EntityPigmanVillager;
import com.stormister.rediscovered.entity.EntityRediscoveredPotion;
import com.stormister.rediscovered.entity.EntityScarecrow;
import com.stormister.rediscovered.entity.EntitySkeletonHorse;
import com.stormister.rediscovered.entity.EntitySkyChicken;
import com.stormister.rediscovered.entity.EntityZombieHorse;
import com.stormister.rediscovered.models.ModelFish;
import com.stormister.rediscovered.models.ModelPigmanMob;
import com.stormister.rediscovered.models.ModelScarecrow;
import com.stormister.rediscovered.models.ModelSkeletonHorse;
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
import com.stormister.rediscovered.render.RenderTileEntityLectern;
import com.stormister.rediscovered.render.RenderTileEntityLecternOpen;
import com.stormister.rediscovered.render.RenderZombieHorse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxyRediscovered extends CommonProxyRediscovered {
	@Override
	public void registerRenderThings() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLectern.class, new RenderTileEntityLectern());

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLecternOpen.class, new RenderTileEntityLecternOpen());

		MinecraftForge.EVENT_BUS.register(Rediscovered.config);

		ModelBakery.registerItemVariants(Rediscovered.RediscoveredPotion,
				new ResourceLocation("rediscovered:Nausea"),
				new ResourceLocation("rediscovered:Blindness"),
				new ResourceLocation("rediscovered:Dullness"),
				new ResourceLocation("rediscovered:NauseaSplash"),
				new ResourceLocation("rediscovered:BlindnessSplash"),
				new ResourceLocation("rediscovered:DullnessSplash"));

		RenderingRegistry.registerEntityRenderingHandler(EntityPigmanVillager.class,
				new IRenderFactory<EntityPigmanVillager>() {
					@Override
					public Render<? super EntityPigmanVillager> createRenderFor(RenderManager manager) {
						return new RenderPigman(manager, new ModelBiped(), 0.5F);
					}
				});
		RenderingRegistry.registerEntityRenderingHandler(EntityPigmanMob.class,
				new IRenderFactory<EntityPigmanMob>() {
					@Override
					public Render<? super EntityPigmanMob> createRenderFor(RenderManager manager) {
						return new RenderPigman(manager, new ModelPigmanMob(), 0.5F);
					}
				});
		RenderingRegistry.registerEntityRenderingHandler(EntityGreenVillager.class,
				new IRenderFactory<EntityGreenVillager>() {
					@Override
					public Render<? super EntityGreenVillager> createRenderFor(RenderManager manager) {
						return new RenderGreenVillager(manager);
					}
				});
		RenderingRegistry.registerEntityRenderingHandler(EntitySkyChicken.class, new IRenderFactory<EntityChicken>() {
			@Override
			public Render<? super EntityChicken> createRenderFor(RenderManager manager) {
				return new RenderChicken(manager, new ModelChicken(), 0.5F);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityFish.class, new IRenderFactory<EntityFish>() {
			@Override
			public Render<? super EntityFish> createRenderFor(RenderManager manager) {
				return new RenderFishMob(manager, new ModelFish(), 0.5F);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieHorse.class,
				new IRenderFactory<EntityZombieHorse>() {
					@Override
					public Render<? super EntityZombieHorse> createRenderFor(RenderManager manager) {
						return new RenderZombieHorse(manager, new ModelZombieHorse(), 0.5F);
					}
				});
		RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonHorse.class,
				new IRenderFactory<EntitySkeletonHorse>() {
					@Override
					public Render<? super EntitySkeletonHorse> createRenderFor(RenderManager manager) {
						return new RenderSkeletonHorse(manager, new ModelSkeletonHorse(), 0.5F);
					}
				});
		RenderingRegistry.registerEntityRenderingHandler(EntityGoodDragon.class,
				new IRenderFactory<EntityGoodDragon>() {
					@Override
					public Render<? super EntityGoodDragon> createRenderFor(RenderManager manager) {
						return new RenderRedDragon(manager);
					}
				});
		RenderingRegistry.registerEntityRenderingHandler(EntityParrow.class, new IRenderFactory<EntityParrow>() {
			@Override
			public Render<? super EntityParrow> createRenderFor(RenderManager manager) {
				return new RenderParrow(manager);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityGiant.class, new IRenderFactory<EntityGiant>() {
			@Override
			public Render<? super EntityGiant> createRenderFor(RenderManager manager) {
				return new RenderGiant(manager, new ModelZombie(), 0.5F, 6.0F);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityScarecrow.class, new IRenderFactory<EntityScarecrow>() {
			@Override
			public Render<? super EntityScarecrow> createRenderFor(RenderManager manager) {
				return new RenderScarecrow(manager, new ModelScarecrow(), 0.5F);
			}
		});
		RenderingRegistry.registerEntityRenderingHandler(EntityRediscoveredPotion.class,
				new IRenderFactory<EntityRediscoveredPotion>() {
					@Override
					public Render<? super EntityRediscoveredPotion> createRenderFor(RenderManager manager) {
						return new RenderRediscoveredPotion(manager, Minecraft.getMinecraft().getRenderItem());
					}
				});
	}

	@Override
	public void registerTileEntitySpecialRenderer() {

	}

}
