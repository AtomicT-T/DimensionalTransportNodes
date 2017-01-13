package com.atomict_t.dimensionaltransportnodes.blocks.dtnode;

import java.util.ArrayList;

import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts.FacesBar;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketMultiBlockChange.BlockUpdateData;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class DTNodeTileEntity extends TileEntity implements ITickable, INBTSerializable<NBTTagCompound> {

	public static ArrayList<DTNodeTileEntity> nodes = new ArrayList<DTNodeTileEntity>();
	
    public static int size = 0;
    public int nodeId = size++;
    
    private DTNodeSideSettings[] settings = new DTNodeSideSettings[6];
    
//    // This item handler will hold our nine inventory slots
//    private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
//        @Override
//        protected void onContentsChanged(int slot) {
//            // We need to tell the tile entity that something has changed so
//            // that the chest contents is persisted
//        	System.out.println("slot changed: " + slot);
//        }
//    };
    
    public DTNodeTileEntity() {
		settings[0] = new DTNodeSideSettings(pos.north());
		settings[1] = new DTNodeSideSettings(pos.south());
		settings[2] = new DTNodeSideSettings(pos.east());
		settings[3] = new DTNodeSideSettings(pos.west());
		settings[4] = new DTNodeSideSettings(pos.up());
		settings[5] = new DTNodeSideSettings(pos.down());
		
    	nodes.add(this);
    }
    
    public DTNodeSideSettings getSide(int n){
    	if(n > 5 || n < 0) return null;
    	
    	return settings[n];
    }
    
    @Override
    public void update() {
    	if(worldObj.isRemote){
//            IBlockState state = this.worldObj.getBlockState(pos);
//            this.worldObj.notifyBlockUpdate(pos, state, state, 3);
    	}
	}

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        
        deserializeNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    	compound = super.writeToNBT(compound);
        return serializeNBT(compound);
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
    
	@Override
	public void deserializeNBT(NBTTagCompound compound) {
    	for(int i = 0; i < settings.length; i++){
    		settings[i].deserializeNBT((NBTTagCompound) compound.getCompoundTag("side" + i));
    	}
//
//        if (compound.hasKey("items")) {
//        	itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
//        }
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		return serializeNBT(compound);
	}
	
	public NBTTagCompound serializeNBT(NBTTagCompound compound) {
    	for(int i = 0; i < settings.length; i++){
    		compound.setTag("side"+i, settings[i].serializeNBT());
//        	System.out.println("settings[" + i + "].isInput: " + (settings[i].isInput ? "true" : "false") + " (id:" + nodeId + ")");
    	}
    	
//        compound.setTag("items", itemStackHandler.serializeNBT());
		
		return compound;
	}
	
	
	
	
    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

//    @Override
//    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
//        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            return true;
//        }
//        return super.hasCapability(capability, facing);
//    }
//
//    @Override
//    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
//        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
//            return (T) itemStackHandler;
//        }
//        return super.getCapability(capability, facing);
//    }
    
    public boolean hasInventory(int direction){
    	TileEntity te = null;
    	switch(direction){
			case 0:
				te = worldObj.getTileEntity(pos.north());
				break;
			case 1:
				te = worldObj.getTileEntity(pos.south());
				break;
			case 2:
				te = worldObj.getTileEntity(pos.east());
				break;
			case 3:
				te = worldObj.getTileEntity(pos.west());
				break;
			case 4:
				te = worldObj.getTileEntity(pos.up());
				break;
			case 5:
				te = worldObj.getTileEntity(pos.down());
				break;
    	}
    	if(te != null)
			if(
//					te instanceof IInventory || 
					te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)
				)
				return true;
		
    	return false;
    }
}
