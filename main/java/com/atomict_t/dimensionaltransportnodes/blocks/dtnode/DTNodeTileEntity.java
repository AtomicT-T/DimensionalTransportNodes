package com.atomict_t.dimensionaltransportnodes.blocks.dtnode;

import java.util.ArrayList;

import com.atomict_t.dimensionaltransportnodes.blocks.dtnode.guiparts.FacesBar;
import com.atomict_t.dimensionaltransportnodes.utils.BlockFace;
import com.atomict_t.dimensionaltransportnodes.utils.__;

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

public class DTNodeTileEntity extends TileEntity implements INBTSerializable<NBTTagCompound>, ITickable {
	private boolean initialized = false;
	private int tickLimiter = 20;

	public static ArrayList<DTNodeTileEntity> nodes = new ArrayList<DTNodeTileEntity>();
	
    public static int size = 0;
    public int nodeId = size++;
    
    private DTNodeSideSettings[] settings = new DTNodeSideSettings[6];
        
    public DTNodeTileEntity() {
    	super();
    	for(int i = 0; i < 6; i++)
    		settings[i] = new DTNodeSideSettings();
		
    	nodes.add(this);
    }
    
    public DTNodeSideSettings getSide(int n){
    	if(n > 5 || n < 0) return null;
    	
    	return settings[n];
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
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		return serializeNBT(compound);
	}
	
	public NBTTagCompound serializeNBT(NBTTagCompound compound) {
    	for(int i = 0; i < settings.length; i++){
    		compound.setTag("side"+i, settings[i].serializeNBT());
    	}
    	
		return compound;
	}
	
	
	
	
    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }
    
    public boolean hasInventory(int direction){
    	TileEntity te = null;
    	te = worldObj.getTileEntity(pos.offset(BlockFace.toMCFacing(direction)));
    	if(te != null)
			if(te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
				return true;
    	return false;
    }

	@Override
	public void update() {
		if(worldObj == null || worldObj.isRemote) return;
		if(tickLimiter  < 0){
			for(int i = 0; i < 6; i++){
				settings[i].onTick(worldObj, pos.offset(BlockFace.toMCFacing(i)));
			}
			tickLimiter = 20;
		}
		tickLimiter--;
	}
}
