package com.stormister.rediscovered;

import java.util.HashMap;
import java.util.Map;

import com.stormister.rediscovered.entity.EntityFish;
import com.stormister.rediscovered.entity.EntityGiant;
import com.stormister.rediscovered.entity.EntityGoodDragon;
import com.stormister.rediscovered.entity.EntityGreenVillager;
import com.stormister.rediscovered.entity.EntityMountableBlock;
import com.stormister.rediscovered.entity.EntityParrow;
import com.stormister.rediscovered.entity.EntityPigmanMob;
import com.stormister.rediscovered.entity.EntityPigmanVillager;
import com.stormister.rediscovered.entity.EntityRediscoveredPotion;
import com.stormister.rediscovered.entity.EntityScarecrow;
import com.stormister.rediscovered.entity.EntitySkeletonHorse;
import com.stormister.rediscovered.entity.EntitySkyChicken;
import com.stormister.rediscovered.entity.EntityZombieHorse;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class CommonProxyRediscovered {
	/**
	 * Used to store IExtendedEntityProperties data temporarily between player
	 * death and respawn
	 */
	private static final Map<String, NBTTagCompound> extendedEntityData = new HashMap<String, NBTTagCompound>();

	/**
	 * Removes the compound from the map and returns the NBT tag stored for name
	 * or null if none exists
	 */
	public static NBTTagCompound getEntityData(String name) {
		return extendedEntityData.remove(name);
	};

	// Block Renders
	public static void registerBlockRenders(Block block, String name) {
		Item item = Item.getItemFromBlock(block);
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(item, 0,
				new ModelResourceLocation(Rediscovered.modid + ":" + name, "inventory"));
	}

	public static void registerItemRenders(Item item, int meta, String name) {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(item, meta,
				new ModelResourceLocation(Rediscovered.modid + ":" + name, "inventory"));
	}

	// Item Renders
	public static void registerItemRenders(Item item, String name) {
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(item, 0,
				new ModelResourceLocation(Rediscovered.modid + ":" + name, "inventory"));
	}

	// Entity Renders
	public static void registerRediscoveredMob(Class<? extends Entity> entityClass, String entityName) {
		EntityRegistry.registerModEntity(entityClass, entityName, Rediscovered.nextInternalID(), Rediscovered.instance,
				80, 3, true);
	}

	public static void registerRediscoveredMob(Class<? extends Entity> entityClass, String entityName, int color1,
			int color2) {
		EntityRegistry.registerModEntity(entityClass, entityName, Rediscovered.nextInternalID(), Rediscovered.instance,
				80, 3, true, color1, color2);
	}

	/**
	 * Adds an entity's custom data to the map for temporary storage
	 *
	 * @param compound
	 *            An NBT Tag Compound that stores the IExtendedEntityProperties
	 *            data only
	 */
	public static void storeEntityData(String name, NBTTagCompound compound) {
		extendedEntityData.put(name, compound);
	}

	public int getBlockRender(Block blockID) {
		return -1;
	}

	public void init() {
		RediscoveredItemsManager.registerRecipes();
	}

	public void preInit() {
		RediscoveredItemsManager.init();
		RediscoveredItemsManager.registerGameRegistryThings();

		registerRediscoveredMob(EntityParrow.class, "ParrowRediscovered");
		registerRediscoveredMob(EntityRediscoveredPotion.class, "PotionRediscovered");
		registerRediscoveredMob(EntityMountableBlock.class, "EntityMountableBlockRediscovered");
		registerRediscoveredMob(EntityPigmanVillager.class, "PigmanRediscovered", 0xf0a5a2, 0xa1a1a1);
		registerRediscoveredMob(EntityPigmanMob.class, "PigmanMobRediscovered", 0xf0a5a2, 0xa1a1a1);
		registerRediscoveredMob(EntityGreenVillager.class, "GreenVillagerRediscovered", 5651507, 7969893);
		registerRediscoveredMob(EntitySkyChicken.class, "SkyChickenRediscovered");
		registerRediscoveredMob(EntityGiant.class, "GiantZombieRediscovered", 2243405, 7969893);
		registerRediscoveredMob(EntityFish.class, "FishRediscovered", 44975, 2243405);
		registerRediscoveredMob(EntityZombieHorse.class, "ZombieHorseRediscovered", 0x4c7129, 15656192);
		registerRediscoveredMob(EntitySkeletonHorse.class, "SkeletonHorseRediscovered", 12698049, 15656192);
		registerRediscoveredMob(EntityScarecrow.class, "ScarecrowRediscovered");
		registerRediscoveredMob(EntityGoodDragon.class, "RedDragonRediscovered");

	}

	public void registerEntityRenderers() {
	}

	public void registerItemRenderers() {

	}
}
