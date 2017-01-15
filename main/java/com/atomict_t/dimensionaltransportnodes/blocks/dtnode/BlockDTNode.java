package com.atomict_t.dimensionaltransportnodes.blocks.dtnode;

import javax.annotation.Nullable;

import com.atomict_t.dimensionaltransportnodes.Config;
import com.atomict_t.dimensionaltransportnodes.DTNGuiHandler;
import com.atomict_t.dimensionaltransportnodes.DimensionalTransportNodes;
import com.atomict_t.dimensionaltransportnodes.blocks.BlockTileEntity;
import com.atomict_t.dimensionaltransportnodes.blocks.datablock.DataTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDTNode extends Block implements ITileEntityProvider{
	public BlockDTNode() {
		super(Material.ROCK);
		setHardness(3f);
		setResistance(5f);
		setCreativeTab(CreativeTabs.MATERIALS);
		
		String name = "blockDTNode";
		
        setUnlocalizedName(name);
        setRegistryName(name);
        
		ItemBlock itemBlock = new ItemBlock(this);
		itemBlock.setRegistryName(getRegistryName());
		
		GameRegistry.register(this);
        GameRegistry.register(itemBlock);
		DimensionalTransportNodes.proxy.registerItemRenderer(itemBlock, 0, name);
		
        GameRegistry.registerTileEntity(DTNodeTileEntity.class, "DTNodeTileEntity");
	}

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
    
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		
		if(!worldIn.isRemote){
	        playerIn.openGui(DimensionalTransportNodes.instance, DTNGuiHandler.DT_NODE_ITEM_GUI, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		
		return true;
	}
	
	@Override
	public BlockDTNode setCreativeTab(CreativeTabs tab) {
		super.setCreativeTab(tab);
		return this;
	}
	

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.24f, 0.24f, 0.24f, 0.76f, 0.76f, 0.76f);
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new DTNodeTileEntity();
	}
	
	public DTNodeTileEntity getTE(World world, BlockPos pos) {
        return (DTNodeTileEntity) world.getTileEntity(pos);
    }

}
