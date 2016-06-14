package tiffit.dungeondimension.chunkprovider;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.ChunkProviderOverworld;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import tiffit.dungeondimension.CustomMonumentStruct;

public class MonumentChunkProvider implements IChunkGenerator {

	private World world;
	public static final int world_size = 5;
    private CustomMonumentStruct oceanMonumentGenerator = new CustomMonumentStruct();

	
	public MonumentChunkProvider(World world) {
		this.world = world;
	}
	
	@Override
	public Chunk provideChunk(int cx, int cz) {
		ChunkProviderOverworld e;
		ChunkPrimer chunkprimer = new ChunkPrimer();
		if(cx > world_size || cx < -world_size || cz > world_size || cz < -world_size){
			return new Chunk(world, chunkprimer, cx, cz);
		}else{
			this.oceanMonumentGenerator.generate(this.world, cx, cz, chunkprimer);
			for(int x = 0; x < 16; x++){
				for(int z = 0; z < 16; z++){
					for(int y = 0; y < 63; y++){
						if(y == 0) chunkprimer.setBlockState(x, y, z, Blocks.BEDROCK.getDefaultState());
						else if(y < 4 && y > 0) chunkprimer.setBlockState(x, y, z, Blocks.GRAVEL.getDefaultState());
						else chunkprimer.setBlockState(x, y, z, Blocks.WATER.getDefaultState());
						if(cx == world_size){
							for(int i = 0; i < 16; i++){
								for(int wy = 1; wy < 64; wy++){
									chunkprimer.setBlockState(15, wy, i, Blocks.BARRIER.getDefaultState());
								}
							}
						}
						if(cx == -world_size){
							for(int i = 0; i < 16; i++){
								for(int wy = 1; wy < 64; wy++){
									chunkprimer.setBlockState(0, wy, i, Blocks.BARRIER.getDefaultState());
								}
							}
						}
						if(cz == world_size){
							for(int i = 0; i < 16; i++){
								for(int wy = 1; wy < 64; wy++){
									chunkprimer.setBlockState(i, wy, 15, Blocks.BARRIER.getDefaultState());
								}
							}
						}
						if(cz == -world_size){
							for(int i = 0; i < 16; i++){
								for(int wy = 1; wy < 63; wy++){
									chunkprimer.setBlockState(i, wy, 0, Blocks.BARRIER.getDefaultState());
								}
							}
						}
					}
				}
			}
		}
		Chunk chunk = new Chunk(world, chunkprimer, cx, cz);
		byte[] biomeArr = new byte[256];
		for(int i = 0; i < 256; i++){
			biomeArr[i] = (byte) Biome.getIdForBiome(Biomes.DEEP_OCEAN);
		}
		chunk.setBiomeArray(biomeArr);
		chunk.generateSkylightMap();
		return chunk;
	}

	@Override
	public void populate(int cx, int cz) {
		this.oceanMonumentGenerator.generateStructure(this.world, world.rand, new ChunkPos(cx, cz));
	}

	@Override
	public boolean generateStructures(Chunk chunk, int cx, int cz) {
		boolean flag = true;
        flag |= this.oceanMonumentGenerator.generateStructure(this.world, world.rand, new ChunkPos(cx, cz));
		return flag;
	}

	@Override
	public List<SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        if (creatureType == EnumCreatureType.MONSTER && this.oceanMonumentGenerator.isPositionInStructure(this.world, pos)){
            return this.oceanMonumentGenerator.getScatteredFeatureSpawnList();
        }
		return Lists.newArrayList();
	}

	@Override
	public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunk, int cx, int cz) {
		this.oceanMonumentGenerator.generate(this.world, cx, cz, (ChunkPrimer)null);
	}


}
