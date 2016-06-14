package tiffit.dungeondimension.items;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import tiffit.dungeondimension.DungeonDimension;
import tiffit.dungeondimension.teleporter.MonumentTeleporter;

public class ItemCreativeTP extends Item {

	public ItemCreativeTP(){
		setCreativeTab(CreativeTabs.MISC);
		setMaxStackSize(1);
		setUnlocalizedName("creativetp");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack is, World w, EntityPlayer p, EnumHand h) {
		if(!w.isRemote){
			EntityPlayerMP player = (EntityPlayerMP) p;
			if(!player.isCreative()){
				player.addChatMessage(new TextComponentString(ChatFormatting.RED + "You must be in creative to use this!"));
				return new ActionResult(EnumActionResult.PASS, is);
			}
			player.setPortal(player.getPosition());
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			PlayerList list = server.getPlayerList();
			BlockPos pos = player.getPosition();
			if(player.isSneaking())
				list.transferPlayerToDimension(player, 0, new MonumentTeleporter(server.worldServerForDimension(0)));	
			else{
				DungeonDimension.getNextDimensionForWorld(w);
				list.transferPlayerToDimension(player, DungeonDimension.current_dimension, new MonumentTeleporter(server.worldServerForDimension(DungeonDimension.current_dimension)));	
			}
			return new ActionResult(EnumActionResult.SUCCESS, is);
		}
		return new ActionResult(EnumActionResult.PASS, is);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add("RMB - Create and goto monument dimension");
		tooltip.add("Shift+RMB - Go to overworld");
	}
	
	
	
}
