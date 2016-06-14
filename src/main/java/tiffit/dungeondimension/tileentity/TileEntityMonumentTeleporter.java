package tiffit.dungeondimension.tileentity;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import tiffit.dungeondimension.DungeonDimension;
import tiffit.dungeondimension.teleporter.MonumentTeleporter;
import tiffit.dungeondimension.worldprovider.MonumentWorldProvider;

public class TileEntityMonumentTeleporter extends TileEntity implements ITickable {
	private int worldID = -1;
	private boolean broken;
	private HashMap<EntityPlayerMP, Integer> abovePlayers = new HashMap<EntityPlayerMP, Integer>();
	
	public TileEntityMonumentTeleporter() {
		this.broken = false;
	}
	
	public TileEntityMonumentTeleporter(boolean broken) {
		this.broken = broken;
	}
	
	public void teleport(EntityPlayer ent){
		if(!worldObj.isRemote && !broken){
			ent.setPortal(getPos());
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			PlayerList list = server.getPlayerList();
			if(worldID == -1){
				if(worldObj.provider instanceof WorldProviderSurface){
					worldID = DungeonDimension.getNextDimensionForWorld(worldObj);
				}else{
					worldID = 0;
				}
			}
			list.transferPlayerToDimension((EntityPlayerMP) ent, worldID, new MonumentTeleporter(server.worldServerForDimension(worldID)));	
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("worldID", worldID);
		compound.setBoolean("broken", broken);
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		worldID = compound.getInteger("worldID");
		broken = compound.getBoolean("broken");
	}
	
	@Override
	public void update() {
		Random rand = worldObj.rand;
		broken = worldObj.getBlockState(getPos()).getBlock() == DungeonDimension.monumentteleporterbroken;
		if(!broken){
			worldObj.spawnParticle(EnumParticleTypes.PORTAL, getPos().getX() + rand.nextDouble(), getPos().getY()+1, getPos().getZ() + rand.nextDouble(), 0, -1.25, 0);
		}
		if(worldID > 0 && !broken && !worldObj.isRemote)breakCheck();
		handleAbovePlayers(rand);
	}
	
	private void handleAbovePlayers(Random rand){
		if(!broken && !worldObj.isRemote){
			List<EntityPlayerMP> players = getAbovePlayers(EntityPlayerMP.class);
			for(EntityPlayerMP player : abovePlayers.keySet()){
				if(!players.contains(player)){
					abovePlayers.remove(player);
				}
			}
			for(EntityPlayerMP player : players){
				if(!abovePlayers.containsKey(player)){
					abovePlayers.put(player, 20);
				}
				abovePlayers.put(player, abovePlayers.get(player) - 1);
				if(abovePlayers.get(player) <= 0){
					teleport(player);
				}
			}
		}else if(broken){
			List<EntityPlayer> players = getAbovePlayers(EntityPlayer.class);
			if(players.size() > 0){
				worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, getPos().getX() + rand.nextDouble(), getPos().getY(), getPos().getZ() + rand.nextDouble(), 0, 0, 0);
			}
		}
	}
	
	public <T extends EntityLivingBase> List<T> getAbovePlayers(Class<T> clazz){
		return worldObj.getEntitiesWithinAABB(clazz, new AxisAlignedBB(getPos().getX(), getPos().getY(), getPos().getZ(), getPos().getX()+1, getPos().getY()+2, getPos().getZ()+1));
	}
	
	private void breakCheck(){
		if(!DimensionManager.isDimensionRegistered(worldID)){
			DungeonDimension.current_dimension = worldID;
	    	DimensionManager.registerDimension(worldID, new MonumentWorldProvider().getDimensionType());
		}
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		WorldServer world = server.worldServerForDimension(worldID);
		if(world != null && world.provider instanceof MonumentWorldProvider){
			MonumentWorldProvider provider = (MonumentWorldProvider) world.provider;
			if(provider.isCorrupt()){
				worldObj.setBlockState(getPos(), DungeonDimension.monumentteleporterbroken.getDefaultState());
			}
		}
	}

}
