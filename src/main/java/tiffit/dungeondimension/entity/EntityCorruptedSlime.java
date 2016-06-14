package tiffit.dungeondimension.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import tiffit.dungeondimension.DungeonDimension;

public class EntityCorruptedSlime extends EntityThrowable {
	
    public EntityCorruptedSlime(World world){
        super(world);
    }

    public EntityCorruptedSlime(World world, EntityLivingBase thrower){
        super(world, thrower);
    }

    public EntityCorruptedSlime(World world, double x, double y, double z){
        super(world, x, y, z);
    }
	
	@Override
    protected void onImpact(RayTraceResult result){
		if(result.typeOfHit == Type.BLOCK && !worldObj.isRemote){
			if(worldObj.getBlockState(result.getBlockPos()) != null && worldObj.getBlockState(result.getBlockPos()).getBlock() == Blocks.GRAVEL){
				worldObj.setBlockState(result.getBlockPos(), DungeonDimension.corruption.getDefaultState());
			}
		}else if (result.entityHit != null){
            if(result.entityHit instanceof EntityLivingBase){
            	EntityLivingBase ent = (EntityLivingBase) result.entityHit;
            	if(ent instanceof EntityPlayer) ent.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 10*20, 1));
            	ent.addPotionEffect(new PotionEffect(MobEffects.POISON, 2*20, 1));
            }
        }
        for (int j = 0; j < 8; ++j){
            this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[]{Item.getIdFromItem(DungeonDimension.corruptionslime)});
        }
        if (!this.worldObj.isRemote){
            this.setDead();
        }
		setDead();
    }
	
	
	public static class EntityRenderFactory implements IRenderFactory{

		@Override
		public Render createRenderFor(RenderManager manager) {
			return new RenderSnowball(manager, DungeonDimension.corruptionslime, Minecraft.getMinecraft().getRenderItem());
		}
		
	}


}
