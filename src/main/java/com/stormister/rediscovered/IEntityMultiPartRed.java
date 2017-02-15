package com.stormister.rediscovered;

import com.stormister.rediscovered.entity.EntityGoodDragonPart;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public interface IEntityMultiPartRed {
	boolean attackEntityFromPart(EntityGoodDragonPart var1, DamageSource var2, float var3);

	World func_82194_d();

	public boolean interactSpecial(EntityPlayer par1EntityPlayer);

	boolean mount(EntityPlayer entity);
}
