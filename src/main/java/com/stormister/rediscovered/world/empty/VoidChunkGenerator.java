package com.stormister.rediscovered.world.empty;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * Created by modmuss50 on 01/12/16.
 */
public class VoidChunkGenerator implements IChunkProvider {

	private final World world;

	public VoidChunkGenerator(World world) {
		this.world = world;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public boolean chunkExists(int x, int z) {
		return true;
	}

	@Override
	public int getLoadedChunkCount() {
		return 0;
	}

	@Override
	public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		return null;
	}

	@Nullable
	@Override
	public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
		return null;
	}

	@Override
	public String makeString() {
		return "VoidLevelSource";
	}

	@Override
	public void populate(IChunkProvider chunkProvider, int x, int z) {

	}

	@Override
	public boolean populateChunk(IChunkProvider chunkProvider, Chunk chunkIn, int x, int z) {
		return true;
	}

	@Override
	public Chunk provideChunk(BlockPos blockPosIn) {
		return this.provideChunk(blockPosIn.getX(), blockPosIn.getZ());
	}

	@Override
	public Chunk provideChunk(int x, int z) {
		ChunkPrimer chunkprimer = new ChunkPrimer();
		Chunk chunk = new Chunk(world, chunkprimer, x, z);
		chunk.generateSkylightMap();
		byte[] abyte = chunk.getBiomeArray();
		for (int i1 = 0; i1 < abyte.length; ++i1) {
			abyte[i1] = (byte) BiomeGenBase.plains.biomeID; // Plains
		}
		chunk.generateSkylightMap();
		return chunk;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) {

	}

	@Override
	public boolean saveChunks(boolean saveAllChunks, IProgressUpdate progressCallback) {
		return true;
	}

	@Override
	public void saveExtraData() {
	}

	@Override
	public boolean unloadQueuedChunks() {
		return false;
	}
}