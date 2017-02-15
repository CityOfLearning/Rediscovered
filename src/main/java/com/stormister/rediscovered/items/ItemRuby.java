package com.stormister.rediscovered.items;

import com.stormister.rediscovered.mod_Rediscovered;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRuby extends Item {
	private final String name = "gemRuby";

	public ItemRuby() {
		super();
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(mod_Rediscovered.modid + "_" + name);
	}

	public String getName() {
		return name;
	}
}