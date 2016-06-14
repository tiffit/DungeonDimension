package tiffit.dungeondimension.teleporter;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import tiffit.dungeondimension.DungeonDimension;

public class MonumentTeleporter extends Teleporter {

	private WorldServer world;
	
	public MonumentTeleporter(WorldServer world) {
		super(world);
		this.world = world;  
	}
	
	@Override
	public void placeInPortal(Entity entity, float f) {
	  if (entity instanceof EntityPlayer) {
	    makePortal(entity);
	    placeInExistingPortal(entity, f);
	  }
	}

	@Override
	public boolean placeInExistingPortal(Entity entity, float f) {
	  if (entity instanceof EntityPlayerMP) {
	    EntityPlayerMP player = (EntityPlayerMP)entity;
	    if(world.provider instanceof WorldProviderSurface){
	    	int x, y, z;
	    	BlockPos pos = player.getBedLocation(0);
	    	if(pos == null){
	    		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
	    		WorldServer overworld = server.worldServerForDimension(0);
	    		x = overworld.getSpawnPoint().getX();
	    		y = overworld.getTopSolidOrLiquidBlock(overworld.getSpawnPoint()).getY();
	    		z = overworld.getSpawnPoint().getZ();
	    	}else{
	    		x = pos.getX();
	    		y = pos.getY();
	    		z = pos.getZ();
	    	}
	    	player.setPosition(x, y, z);
	    }else{
	    	player.setPosition(75.5d, 63d, 75.5d);
	    }
	    return true;
	  }
	  return false;
	}

	@Override
	public boolean makePortal(Entity entity) {
	  if (entity instanceof EntityPlayerMP) {
		  for(int x = 75; x < 75+10; x++){
			  for(int z = 75; z < 75+10; z++){
				  this.world.setBlockState(new BlockPos(x-1, 62, z-1), Blocks.CLAY.getDefaultState());
			  }
		  }
		  this.world.setBlockState(new BlockPos(82, 63, 82), DungeonDimension.monumentteleporter.getDefaultState());
	    return true;
	  }
	  return false;
	}
}
