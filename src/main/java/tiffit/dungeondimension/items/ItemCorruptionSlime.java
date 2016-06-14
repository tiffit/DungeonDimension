package tiffit.dungeondimension.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import tiffit.dungeondimension.entity.EntityCorruptedSlime;

public class ItemCorruptionSlime extends Item {

	public ItemCorruptionSlime(){
		setCreativeTab(CreativeTabs.MISC);
		setMaxStackSize(16);
		setUnlocalizedName("corruptionslime");
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack is, World w, EntityPlayer p, EnumHand h) {
		if (!p.capabilities.isCreativeMode){
            is.stackSize--;
        }
		if(!w.isRemote){
	        w.playSound((EntityPlayer)null, p.posX, p.posY, p.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
			EntityPlayerMP player = (EntityPlayerMP) p;
			EntityCorruptedSlime slime = new EntityCorruptedSlime(w, p);
			slime.setHeadingFromThrower(p, p.rotationPitch, p.rotationYaw, 0.0F, 1.5F, 1.0F);
            w.spawnEntityInWorld(slime);
			return new ActionResult(EnumActionResult.SUCCESS, is);
		}
		return new ActionResult(EnumActionResult.PASS, is);
	}
	
	
	
}
