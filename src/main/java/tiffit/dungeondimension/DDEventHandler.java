package tiffit.dungeondimension;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import tiffit.dungeondimension.entity.EntityCorruptedSlime;
import tiffit.dungeondimension.worldprovider.MonumentWorldProvider;

public class DDEventHandler {

	String[] elder_kill_msg = new String[]{"The ground starts to rumble", "You hear explosions in the distance", "You have a bad feeling about this", "Don't think that was a good idea"};
	
	@SubscribeEvent
	public void onEntityKill(LivingDeathEvent e){
		if(e.getEntityLiving() instanceof EntityGuardian && e.getEntityLiving().worldObj.provider instanceof MonumentWorldProvider){
			EntityGuardian ent = (EntityGuardian) e.getEntityLiving();
			Entity ds = e.getSource().getEntity();
			if(ent.isElder()){
				if(ds != null && ds instanceof EntityPlayerMP){
					EntityPlayerMP player = (EntityPlayerMP) ds;
					Random rand = new Random();
					String msg = elder_kill_msg[rand.nextInt(elder_kill_msg.length)];
					player.addChatMessage(new TextComponentString(ChatFormatting.GRAY + "" + ChatFormatting.ITALIC + msg + "..."));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent e){
		if(e.phase == Phase.START && e.side == Side.SERVER && e.world.provider instanceof MonumentWorldProvider){
			MonumentWorldProvider provider = (MonumentWorldProvider) e.world.provider;
			WorldServer world = (WorldServer) e.world;
			if(!provider.isCorrupt() && world.loadedEntityList.size() > 1){
				boolean elder_found = false;
				if(world.loadedEntityList.size() < 5) return;
				for(Entity ent : world.loadedEntityList){
					if(ent instanceof EntityGuardian){
						EntityGuardian guard = (EntityGuardian) ent;
						if(guard.isElder()){
							elder_found = true;
						}
					}
				}
				if(!elder_found){
					provider.corrupt();
				}
			}
			if(provider.isCorrupt()){
				int x = MathHelper.getRandomIntegerInRange(world.rand, -78, 94);
				int y = 78;
				int z = MathHelper.getRandomIntegerInRange(world.rand, -78, 94);
	            EntityCorruptedSlime slime = new EntityCorruptedSlime(world, x + .5, y + .5, z + .5);
	            world.spawnEntityInWorld(slime);
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save e){
		if(e.getWorld().provider instanceof MonumentWorldProvider){
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			ArrayList<Integer> dimensions = Lists.newArrayList();
			for(EntityPlayerMP player : server.getPlayerList().getPlayerList()){
				int dim = player.dimension;
				if(!dimensions.contains(dim)){
					dimensions.add(dim);
				}
			}
			File folder = e.getWorld().getSaveHandler().getWorldDirectory();
			NBTTagCompound tag = new NBTTagCompound();
			int[] intarr = new int[dimensions.size()];
			for(int i = 0; i < dimensions.size(); i++){
				intarr[i] = dimensions.get(i);
			}
			tag.setIntArray("dims", intarr);
			try {
				CompressedStreamTools.safeWrite(tag, new File(folder, "dimensiondungeon.dat"));
			} catch (IOException ex) {
				System.out.println("Error while writing world data... this could be really bad!");
				ex.printStackTrace();
			}
		}
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e){
		File folder = e.getWorld().getSaveHandler().getWorldDirectory();
		File saveDat = new File(folder, "dimensiondungeon.dat");
		try {
			NBTTagCompound tag = CompressedStreamTools.read(saveDat);
			if(tag != null){
				for(int i : tag.getIntArray("dims")){
					if(!DimensionManager.isDimensionRegistered(i)){
						DimensionManager.registerDimension(i, new MonumentWorldProvider().getDimensionType());
					}
				}
			}
		} catch (IOException ex) {
			System.out.println("Error while reading world data... this could be really bad!");
			ex.printStackTrace();
		}
	}
	
}
