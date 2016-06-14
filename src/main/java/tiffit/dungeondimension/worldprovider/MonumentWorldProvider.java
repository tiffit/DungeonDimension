package tiffit.dungeondimension.worldprovider;

import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;
import tiffit.dungeondimension.DungeonDimension;
import tiffit.dungeondimension.chunkprovider.MonumentChunkProvider;

public class MonumentWorldProvider extends WorldProvider {

	private DimensionType type;
	private boolean isCorrupt = false;

	@Override
	public DimensionType getDimensionType() {
		if(type == null)type = DimensionType.register("monument", "_monument", DungeonDimension.current_dimension, this.getClass(), false);
		return type;
		
	}
	
	@Override
    public boolean canRespawnHere(){
        return false;
    }
	
    public IChunkGenerator createChunkGenerator(){
        return new MonumentChunkProvider(worldObj);
    }
    
    public void corrupt(){
    	isCorrupt = true;
    }
    
    public boolean isCorrupt(){
    	return isCorrupt;
    }
    
	@Override
	public boolean canDropChunk(int x, int z) {
		int s = MonumentChunkProvider.world_size;
		return !((x < s || x > -s) && (z < s && z > -s));
	}

}
