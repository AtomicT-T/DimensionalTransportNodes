package com.atomict_t.dimensionaltransportnodes.blocks.dtnode;

import java.io.Serializable;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.codec.binary.Base64;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class DTNodeSideSettings implements INBTSerializable<NBTTagCompound>{
	public static final int MAX_SLOT_CHECKS_PER_TICK = 4;
	public static final int MAX_ITEM_PER_TICK = 4;
	private int currentSlot = 0;
	
	private int tickLimiter = 0;
    
	public boolean isInput = false;
	public boolean isInputWhitelist = true;
	
	public boolean isOutput = false;
	public boolean isOutputWhitelist = true;
	public BlockPos pos;
	private TileEntity inv;
	
	
	public DTNodeSideSettings(BlockPos pos) {
		this.pos = pos;
	}
	
	@Override
	public void deserializeNBT(NBTTagCompound compound) {
		this.isInput = compound.getBoolean("isInput");
		this.isInputWhitelist = compound.getBoolean("isInputWhitelist");
		this.isOutput = compound.getBoolean("isOutput");
		this.isOutputWhitelist = compound.getBoolean("isOutputWhitelist");
		this.pos = new BlockPos(
				compound.getInteger("posX"),
				compound.getInteger("posY"),
				compound.getInteger("posZ")
			);
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		compound.setBoolean("isInput", this.isInput);
		compound.setBoolean("isInputWhitelist", this.isInputWhitelist);
		compound.setBoolean("isOutput", this.isOutput);
		compound.setBoolean("isOutputWhitelist", this.isOutputWhitelist);
		compound.setInteger("posX", pos.getX());
		compound.setInteger("posY", pos.getY());
		compound.setInteger("posZ", pos.getZ());
		return compound;
	}
	
	public void onTick(World worldObj){
		if(tickLimiter < 0){
			if(isInput && !isInputWhitelist){
				getInventory(worldObj);
				transferItems();
			}
			tickLimiter = 20;
		}
		tickLimiter--;
	}
	
	public void transferItems(){
		IItemHandler itemstackHandler = inv.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		ItemStack stack;
		int slotsCheckPerTick = Math.min(itemstackHandler.getSlots(), MAX_SLOT_CHECKS_PER_TICK);
		
		for(int i = 0; i < slotsCheckPerTick; i++){
			if(currentSlot >= itemstackHandler.getSlots()) currentSlot = 0;
			
			stack = itemstackHandler.getStackInSlot(currentSlot);
			if(stack == null || stack.stackSize == 0)
				continue;
			ItemStack tempStack = stack.copy();
			moveToTargets(tempStack);
			
			currentSlot++;
		}
	}
	
	public void moveToTargets(ItemStack aStack){
		ItemStack itemCopy = aStack.copy();
		itemCopy.stackSize = Math.min(aStack.stackSize, MAX_ITEM_PER_TICK);
		
		for(DTNodeTileEntity te : DTNodeTileEntity.nodes){
			for(int i = 0; i < 6; i++){
				DTNodeSideSettings side = te.getSide(i);
				if(side == this) continue;
				
				if(side.isOutput && !side.isOutputWhitelist){
					// move items from this inventory to the next;
					side.getInventory(te.getWorld());
					if(side.inv == null) continue;
					
					mergeStackIntoInventory(side.inv, itemCopy);
					
					// if stack is empty return
				}
			}
		}
	}
	
	
	private int mergeStackIntoInventory(TileEntity inv, ItemStack itemCopy) {
		ItemStackHandler handler = (ItemStackHandler) inv.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		
		return 0;
	}

	public void getInventory(World worldObj){
		inv = worldObj.getTileEntity(pos);
		if(inv == null) return;
		if(!inv.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
			inv = null;
	}
	
	
}
