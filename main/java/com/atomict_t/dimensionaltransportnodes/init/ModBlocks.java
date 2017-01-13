package com.atomict_t.dimensionaltransportnodes.init;

import com.atomict_t.dimensionaltransportnodes.DimensionalTransportNodes;
import com.atomict_t.dimensionaltransportnodes.blocks.BlockBase;
import com.atomict_t.dimensionaltransportnodes.blocks.BlockTileEntity;
import com.atomict_t.dimensionaltransportnodes.blocks.datablock.DataBlock;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.BlockDTNode;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static BlockDTNode dtNode;
	public static DataBlock datablock;
	
	public static void init() {
		dtNode = new BlockDTNode();
//		datablock = new DataBlock();
	}

	
	private static <T extends Block> T register(T block, ItemBlock itemBlock) {
		GameRegistry.register(block);
		GameRegistry.register(itemBlock);
		
		if (block instanceof BlockBase) {
			((BlockBase)block).registerItemModel(itemBlock);
		}
		
		if (block instanceof BlockTileEntity) {
			GameRegistry.registerTileEntity(((BlockTileEntity<?>)block).getTileEntityClass(), block.getRegistryName().toString());
		}
		
		return block;
	}

	private static <T extends Block> T register(T block) {
		ItemBlock itemBlock = new ItemBlock(block);
		itemBlock.setRegistryName(block.getRegistryName());
		return register(block, itemBlock);
	}
}