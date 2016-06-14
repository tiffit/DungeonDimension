package tiffit.dungeondimension.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import tiffit.dungeondimension.DungeonDimension;
import tiffit.dungeondimension.References;
import tiffit.dungeondimension.entity.EntityCorruptedSlime;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		RenderingRegistry.registerEntityRenderingHandler(EntityCorruptedSlime.class, new EntityCorruptedSlime.EntityRenderFactory());
	}

	@Override
	public void init() {
    	ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
    	mesher.register(DungeonDimension.creativetp, 0, new ModelResourceLocation(References.MODID + ":creativetp", "inventory"));
    	mesher.register(DungeonDimension.corruptionslime, 0, new ModelResourceLocation(References.MODID + ":corruptionslime", "inventory"));
    	mesher.register(Item.getItemFromBlock(DungeonDimension.corruption), 0, new ModelResourceLocation(References.MODID + ":corruptionblock", "inventory"));
    	mesher.register(Item.getItemFromBlock(DungeonDimension.monumentteleporter), 0, new ModelResourceLocation(References.MODID + ":monumentteleporter", "inventory"));
    	mesher.register(Item.getItemFromBlock(DungeonDimension.monumentteleporterbroken), 0, new ModelResourceLocation(References.MODID + ":monumentteleporterbroken", "inventory"));
	}

	@Override
	public void postInit() {
	}

}
