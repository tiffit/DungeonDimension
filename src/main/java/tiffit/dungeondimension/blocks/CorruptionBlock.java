package tiffit.dungeondimension.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tiffit.dungeondimension.DungeonDimension;

public class CorruptionBlock extends Block {

	public CorruptionBlock() {
		super(Material.ROCK);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setUnlocalizedName("corruptionblock");
		setHarvestLevel("pickaxe", 0);
		setHardness(.75F);
		setResistance(.75F);
		setSoundType(SoundType.STONE);
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return 2 + (fortune > 0 ? 1 : 0) + (random.nextBoolean() ? 1 : 0);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return DungeonDimension.corruptionslime;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
		tooltip.add("Still not as corrupt as North Korea");
	}

}
