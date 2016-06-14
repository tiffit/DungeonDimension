package tiffit.dungeondimension.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tiffit.dungeondimension.tileentity.TileEntityMonumentTeleporter;

public class MonumentTeleporterBlock extends Block implements ITileEntityProvider {

	private static final AxisAlignedBB BOX = new AxisAlignedBB(0.0D, 0.0D, 0D, 1.0D, .5D, 1.0D);
	private boolean broken;
	public MonumentTeleporterBlock(boolean broken) {
		super(Material.IRON);
		setSoundType(SoundType.STONE);
		this.broken = broken;
		setHardness(2F);
		setResistance(15F);
		setHarvestLevel("pickaxe", 1);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setUnlocalizedName("monumentteleporterblock" + (broken ? "broken" : ""));
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityMonumentTeleporter(broken);
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return 1 + (broken ? 0 : 1);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune){
		return Items.IRON_INGOT;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, BOX);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOX;
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
		return BOX;
	}
	
	@Override
	public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

}
