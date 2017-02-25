package com.stormister.rediscovered.items;

import com.stormister.rediscovered.Rediscovered;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemDreamPillow extends Item {
	private final String name = "DreamPillow";

	public ItemDreamPillow() {
		super();
		maxStackSize = 1;
		setCreativeTab(CreativeTabs.tabMisc);
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(Rediscovered.modid + "_" + name);
	}

	public String getName() {
		return name;
	}
}
