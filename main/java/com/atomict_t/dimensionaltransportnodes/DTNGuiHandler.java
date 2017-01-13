package com.atomict_t.dimensionaltransportnodes;

import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeContainer;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeGuiContainer;
import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.DTNodeTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class DTNGuiHandler implements IGuiHandler {
	
    public static final int DT_NODE_ITEM_GUI = 0;
    
	public DTNGuiHandler(){
		System.out.println("DTNGuiHandler");
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof DTNodeTileEntity) {
            return new DTNodeContainer(player.inventory, (DTNodeTileEntity) te);
        }
        return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof DTNodeTileEntity) {
        	DTNodeTileEntity containerTileEntity = (DTNodeTileEntity) te;
            return new DTNodeGuiContainer(containerTileEntity, new DTNodeContainer(player.inventory, containerTileEntity));
        }
        return null;
	}
}
