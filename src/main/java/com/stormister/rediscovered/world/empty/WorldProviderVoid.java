package com.stormister.rediscovered.world.empty;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * Created by modmuss50 on 01/12/16.
 */
public class WorldProviderVoid extends WorldProvider {

	@Nullable
	@Override
	public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks) {
		return super.calcSunriseSunsetColors(celestialAngle, partialTicks);
	}

	@Override
	public boolean canRespawnHere() {
		return true;
	}

	@Override
	public IChunkProvider createChunkGenerator() {
		return new VoidChunkGenerator(worldObj);
	}

	@Override
	public String getDimensionName() {
		return "Void";
	}

	@Override
	public Vec3 getFogColor(float p_76562_1_, float p_76562_2_) {
		return super.getFogColor(p_76562_1_, p_76562_2_);
	}

	@Override
	public String getInternalNameSuffix() {
		return null;
	}

	@Override
	public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
		return super.getSkyColor(cameraEntity, partialTicks);
	}

	@Override
	public long getWorldTime() {
		return super.getWorldTime();
	}

	@Override
	public boolean isDaytime() {
		return super.isDaytime();
	}

}