package tiffit.dungeondimension;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import tiffit.dungeondimension.blocks.CorruptionBlock;
import tiffit.dungeondimension.blocks.MonumentTeleporterBlock;
import tiffit.dungeondimension.entity.EntityCorruptedSlime;
import tiffit.dungeondimension.items.ItemCorruptionSlime;
import tiffit.dungeondimension.items.ItemCreativeTP;
import tiffit.dungeondimension.proxy.CommonProxy;
import tiffit.dungeondimension.tileentity.TileEntityMonumentTeleporter;
import tiffit.dungeondimension.worldprovider.MonumentWorldProvider;

@Mod(modid = References.MODID, version = References.VERSION, name = References.NAME)
public class DungeonDimension {

	public static Block corruption;
	public static Block monumentteleporter;
	public static Block monumentteleporterbroken;
	public static Item creativetp;
	public static Item corruptionslime;
	
	public static int current_dimension = 660;
	
	@SidedProxy(clientSide = References.CLIENT_PROXY, serverSide = References.SERVER_PROXY, modId = References.MODID)
	public static CommonProxy proxy;
	
	@Instance(References.MODID)
	public static DungeonDimension instance;
	
    @EventHandler
    public void preinit(FMLPreInitializationEvent e){
    	corruption = new CorruptionBlock();
    	creativetp = new ItemCreativeTP();
    	monumentteleporter = new MonumentTeleporterBlock(false);
    	monumentteleporterbroken = new MonumentTeleporterBlock(true);
    	corruptionslime = new ItemCorruptionSlime();
    	GameRegistry.registerBlock(corruption, "corruptionblock");
    	GameRegistry.registerBlock(monumentteleporter, "monumentteleporter");
    	GameRegistry.registerBlock(monumentteleporterbroken, "monumentteleporterbroken");
    	GameRegistry.registerItem(creativetp, "creativetp");
    	GameRegistry.registerItem(corruptionslime, "corruptionslime");
    	GameRegistry.registerTileEntity(TileEntityMonumentTeleporter.class, "TileEntityMonumentTeleporter");
    	EntityRegistry.registerModEntity(EntityCorruptedSlime.class, "dd_throwable_slime", 0, "dungeondimension", 128, 1, true);
    	proxy.preInit();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent e){
    	GameRegistry.addRecipe(new ItemStack(monumentteleporter),
    			   "t t",
    			   "isi",
    			   "epe",
    			   't', Items.GHAST_TEAR, 'i', Items.IRON_INGOT, 's', Items.NETHER_STAR, 'e', Blocks.END_STONE, 'p', Items.ENDER_EYE);
    	
    	GameRegistry.addRecipe(new ItemStack(corruption),
 			   "cc",
 			   "cc",
 			   'c', corruptionslime);
    	
    	GameRegistry.addRecipe(new ItemStack(corruptionslime, 4),
  			   "c",
  			   'c', corruption);
    	MinecraftForge.EVENT_BUS.register(new DDEventHandler());
    	proxy.init();
    }
    
    @EventHandler
    public void postinit(FMLPostInitializationEvent e){
    	proxy.postInit();
    }
    
    public static int getNextDimensionForWorld(World world){
    	File worldF = world.getSaveHandler().getWorldDirectory();
    	int next_dimension = 660;
    	
    	while(containsFolderWithName(worldF, "DIM" + next_dimension)){
    		next_dimension++;
    	}
    	current_dimension = next_dimension;
    	DimensionManager.registerDimension(current_dimension, new MonumentWorldProvider().getDimensionType());
    	return current_dimension;
    }
    
    private static boolean containsFolderWithName(File fold, String name){
    	for(File file : fold.listFiles()){
    		if(file.isDirectory() && file.getName().equals(name)){
    			return true;
    		}
    	}
    	return false;
    }
	
}
